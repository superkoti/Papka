package com.example.papka.ui.screens

import android.net.Uri
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
import com.example.papka.ui.components.CommonTopBar


@Composable
fun HomeScreen(
    navController: NavController,
    foldersViewModel: FoldersViewModel = viewModel()
) {
    var folders by remember { mutableStateOf(foldersViewModel.getFolderContents("")) }
    var showAccordion by remember { mutableStateOf(false) }
    var newFolderName by remember { mutableStateOf(TextFieldValue("")) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Главная",
                showBackButton = false, // В Home кнопка назад не нужна
                onAddClick = { showAccordion = !showAccordion } // Переключаем аккордеон
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
                if (showAccordion) {
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
                                newFolderName = TextFieldValue("")
                                folders = foldersViewModel.getFolderContents("")
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Папка с таким именем уже существует или имя некорректно.",
                                        SnackbarDuration.Short.toString()
                                    )
                                }
                            }
                        }) {
                            Text("Добавить")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(folders) { file ->
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
                                    navController.navigate("folder_screen/${Uri.encode(file.name)}")
                                }
                            }
                        )
                    }
                }
                SnackbarHost(hostState = snackbarHostState)
            }
        }
    )
}
