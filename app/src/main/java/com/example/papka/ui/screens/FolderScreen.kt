package com.example.papka.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.papka.viewmodel.FoldersViewModel
import java.io.File
import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.launch


@Composable
fun FolderScreen(
    navController: NavController,
    folderPath: String,
    foldersViewModel: FoldersViewModel = viewModel()
) {
    val decodedFolderPath = Uri.decode(folderPath)
    var contents by remember {
        mutableStateOf(foldersViewModel.getFolderContents(decodedFolderPath))
    }
    var newFolderName by remember { mutableStateOf(TextFieldValue("")) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        SmallTopAppBar(
            title = { Text(decodedFolderPath) },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле для добавления новой папки
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            TextField(
                value = newFolderName,
                onValueChange = { newFolderName = it },
                placeholder = { Text("Имя папки") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val result = foldersViewModel.addFolder("$decodedFolderPath/${newFolderName.text}")
                if (result) {
                    newFolderName = TextFieldValue("")
                    contents = foldersViewModel.getFolderContents(decodedFolderPath) // Обновляем содержимое
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Папка с таким именем уже существует или имя некорректно.",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }) {
                Text("Добавить")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Вывод содержимого папки
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(contents) { file ->
                ListItem(
                    headlineContent = { Text(file.name) },
                    leadingContent = {
                        if (foldersViewModel.isFolder(file)) {
                            Icon(Icons.Default.Email, contentDescription = "Папка")
                        } else {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Файл")
                        }
                    },
                    modifier = Modifier.clickable {
                        if (foldersViewModel.isFolder(file)) {
                            navController.navigate("folder_screen/${Uri.encode("$decodedFolderPath/${file.name}")}")
                        }
                    }
                )
            }
        }

        // Snackbar для уведомлений
        SnackbarHost(hostState = snackbarHostState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBar(title: @Composable () -> Unit, navigationIcon: @Composable () -> Unit) {
    TopAppBar(
        title = title,
        navigationIcon = navigationIcon
    )
}