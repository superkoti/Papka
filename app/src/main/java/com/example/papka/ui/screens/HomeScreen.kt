package com.example.papka.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.papka.viewmodel.FoldersViewModel
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    navController: NavController,
    foldersViewModel: FoldersViewModel = viewModel()
) {
    val folders = foldersViewModel.getFolderContents("") // Содержимое базовой папки
    var newFolderName by remember { mutableStateOf(TextFieldValue("")) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Заголовок
        Text(
            text = "Главная",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Поле добавления новой папки
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
                val result = foldersViewModel.addFolder(newFolderName.text)
                if (result) {
                    // Обновляем список папок
                    newFolderName = TextFieldValue("")
                } else {
                    // Здесь вызываем показ Snackbar через корутину
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

        // Список содержимого
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(folders) { file ->

                // Используем ту же структуру, что и в FolderScreen
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
                            navController.navigate("folder_screen/${file.name}")
                        }
                    }
                )
            }
        }
        SnackbarHost(hostState = snackbarHostState)
    }
}
