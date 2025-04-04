package com.example.papka.ui.screens

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
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
    var isSelectionMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Мои Папки",
                showAddButton = true,
                onAddClick = { showAccordion = !showAccordion },
                onSelectClick = { isSelectionMode = true },
                onDeleteClick = {
                    foldersViewModel.deleteSelected()
                    isSelectionMode = false // Завершаем режим выделения
                    folders = foldersViewModel.getFolderContents("")
                },
                onCancelClick = {
                    foldersViewModel.clearSelection()
                    isSelectionMode = false // Завершаем режим выделения
                }

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
                                    Icon(Icons.Default.FolderOpen, contentDescription = "Папка")
                                }
                            },
                            modifier = Modifier.clickable {
                                if (foldersViewModel.isFolder(file)) {
                                    foldersViewModel.clearSelection()
                                    navController.navigate("folder_screen/${Uri.encode(file.name)}")
                                }
                            },
                            trailingContent = { // Переносим чекбоксы сюда
                                if (isSelectionMode) {
                                    Checkbox(
                                        checked = foldersViewModel.selectedItems.contains(file.name),
                                        onCheckedChange = { isChecked ->
                                            if (isChecked) {
                                                foldersViewModel.selectItem(file.name)
                                            } else {
                                                foldersViewModel.deselectItem(file.name)
                                            }
                                        }
                                    )
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
