package com.example.papka.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.papka.viewmodel.FoldersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun FolderScreen(
    navController: NavController,
    folderPath: String,
    foldersViewModel: FoldersViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sanitizedFolderPath = foldersViewModel.getFullPath(folderPath)

    // Состояние для содержимого папки, чтобы обновлять список динамически
    var folderContents by remember { mutableStateOf(foldersViewModel.getFolderContents(folderPath)) }

    // Состояния для пользовательского ввода и диалогов
    var newFolderName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // Remember launcher for selecting multiple files or images
    val multipleImagesPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        uris?.let {
            coroutineScope.launch {
                copyFilesToFolder(context, it, sanitizedFolderPath)
                folderContents = foldersViewModel.getFolderContents(folderPath) // Обновляем содержимое папки
            }
        }
    }

    // Remember launcher for selecting a single file
    val singleFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            coroutineScope.launch {
                copyFilesToFolder(context, listOf(it), sanitizedFolderPath)
                folderContents = foldersViewModel.getFolderContents(folderPath) // Обновляем содержимое папки
            }
        }
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Путь: $folderPath") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // Поле и кнопка для создания подпапки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = newFolderName,
                    onValueChange = { newFolderName = it },
                    label = { Text("Название папки") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                Button(onClick = {
                    if (newFolderName.isNotEmpty()) {
                        val newFolderPath = "${folderPath.trimEnd('/')}/$newFolderName".replace("//", "/")
                        val isCreated = foldersViewModel.addFolder(newFolderPath)
                        if (isCreated) {
                            folderContents = foldersViewModel.getFolderContents(folderPath) // Обновляем содержимое папки
                            newFolderName = ""
                        }
                    }
                }) {
                    Text("Создать")
                }
            }

            // Список папок и файлов
            LazyColumn {
                items(folderContents.sortedWith(compareBy({ !it.isDirectory }, { it.name }))) { file ->
                    if (file.isDirectory) {
                        Text(
                            text = "📁 ${file.name}",
                            modifier = Modifier
                                .clickable {
                                    // Открытие подпапки (передаем путь через navController)
                                    navController.navigate("folder_screen/${Uri.encode("$folderPath/${file.name}")}")
                                }
                                .padding(8.dp)
                        )
                    } else {
                        Text(
                            text = "📄 ${file.name}",
                            modifier = Modifier
                                .clickable {
                                    // Логика нажатия на файл, если нужно
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddFileDialog(
            onDismiss = { showDialog = false },
            onItemSelected = { isPhoto ->
                showDialog = false
                if (isPhoto) {
                    multipleImagesPickerLauncher.launch(arrayOf("image/*"))
                } else {
                    singleFilePickerLauncher.launch(arrayOf("*/*"))
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit
) {
    TopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions
    )
}

@Composable
fun AddFileDialog(
    onDismiss: () -> Unit,
    onItemSelected: (isPhoto: Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Добавить файл") },
        text = {
            Column {
                Text(
                    text = "Выбрать Фото",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemSelected(true) }
                        .padding(8.dp)
                )
                Text(
                    text = "Выбрать Файл",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemSelected(false) }
                        .padding(8.dp)
                )
            }
        }
    )
}

suspend fun copyFilesToFolder(context: Context, uris: List<Uri>, folder: File) {
    withContext(Dispatchers.IO) {
        uris.forEach { uri ->
            val inputStream = context.contentResolver.openInputStream(uri)
            val displayName: String = context.contentResolver.query(uri, null, null, null, null)
                ?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    cursor.getString(nameIndex)
                } ?: "file_${System.currentTimeMillis()}"

            inputStream?.use { input ->
                val newFile = File(folder, displayName)
                newFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
}