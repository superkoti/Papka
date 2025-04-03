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
import com.example.papka.ui.components.CommonTopBarWithTwoButtons
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
    var folderContents by remember { mutableStateOf(foldersViewModel.getFolderContents(folderPath)) }
    var showAccordion by remember { mutableStateOf(false) } // Поле для добавления папки
    var newFolderName by remember { mutableStateOf("") }
    var showAddFileDialog by remember { mutableStateOf(false) } // Диалог для добавления файла
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Лаунчер выбора документа
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            coroutineScope.launch {
                val folder = foldersViewModel.getFullPath(folderPath)
                copyFilesToFolder(context, uris, folder)
                folderContents = foldersViewModel.getFolderContents(folderPath) // Обновляем список после копирования
            }
        }
    }

    Scaffold(
        topBar = {
            CommonTopBarWithTwoButtons(
                title = folderPath,
                showBackButton = true,
                onBackClick = { navController.popBackStack() },
                onAddFolderClick = { showAccordion = !showAccordion }, // Показываем/скрываем поле для папки
                onAddFileClick = { showAddFileDialog = true } // Показываем диалог для добавления файла
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Поле для ввода нового имени папки
                if (showAccordion) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        TextField(
                            value = newFolderName,
                            onValueChange = { newFolderName = it },
                            placeholder = { Text("Введите имя папки") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            if (foldersViewModel.addFolder("$folderPath/$newFolderName")) {
                                folderContents = foldersViewModel.getFolderContents(folderPath) // Обновляем список
                                newFolderName = ""
                                showAccordion = false
                            }
                        }) {
                            Text("Добавить")
                        }
                    }
                }

                // Отображение содержимого папки
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(folderContents) { file ->
                        ListItem(
                            headlineContent = { Text(file.name) },
                            leadingContent = {
                                if (foldersViewModel.isFolder(file)) {
                                    Icon(Icons.Default.Add, contentDescription = "Папка")
                                } else {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Файл")
                                }
                            },
                            modifier = Modifier.clickable {
                                if (foldersViewModel.isFolder(file)) {
                                    navController.navigate("folder_screen/${Uri.encode("$folderPath/${file.name}")}")
                                }
                            }
                        )
                    }
                }

                // Диалог для добавления файла
                if (showAddFileDialog) {
                    AddFileDialog(
                        onDismiss = { showAddFileDialog = false },
                        onItemSelected = { isPhoto ->
                            if (isPhoto) {
                                filePickerLauncher.launch(arrayOf("image/*"))
                            } else {
                                filePickerLauncher.launch(arrayOf("*/*"))
                            }
                            showAddFileDialog = false
                        }
                    )
                }
            }
        }
    )
}

// Диалог для добавления файла
@Composable
fun AddFileDialog(
    onDismiss: () -> Unit,
    onItemSelected: (isPhoto: Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить файл") },
        confirmButton = {
            TextButton(onClick = { onItemSelected(true) }) {
                Text("Фото")
            }
        },
        dismissButton = {
            TextButton(onClick = { onItemSelected(false) }) {
                Text("Файл")
            }
        }
    )
}

// Копирование файлов в папку
suspend fun copyFilesToFolder(context: Context, uris: List<Uri>, folder: File) {
    withContext(Dispatchers.IO) {
        uris.forEach { uri ->
            val fileName = context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(nameIndex)
            } ?: "unknown_file"
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputFile = File(folder, fileName)
            inputStream?.use { input ->
                outputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
}