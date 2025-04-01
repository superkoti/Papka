package com.example.papka.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.papka.viewmodel.FoldersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    foldersViewModel: FoldersViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Домашний экран") },
                navigationIcon = { Spacer(modifier = Modifier.width(16.dp)) }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                var isAccordionExpanded by remember { mutableStateOf(false) }
                var folderName by remember { mutableStateOf(TextFieldValue("")) }
                val focusRequester = remember { FocusRequester() }

                // Кнопка для открытия аккордиона
                Button(
                    onClick = {
                        isAccordionExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (isAccordionExpanded) "Закрыть" else "Добавить папку")
                }

                // Аккордион с полем ввода
                if (isAccordionExpanded) {
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            // Поле ввода
                            OutlinedTextField(
                                value = folderName,
                                onValueChange = { folderName = it },
                                label = { Text("Введите название папки") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        if (folderName.text.isNotBlank()) {
                                            foldersViewModel.addFolder(folderName.text)
                                            folderName = TextFieldValue("")
                                            isAccordionExpanded = false
                                        }
                                    }
                                ) {
                                    Text("ОК")
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    onClick = {
                                        folderName = TextFieldValue("")
                                        isAccordionExpanded = false
                                    }
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Отмена")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Список папок
                LazyColumn {
                    items(foldersViewModel.folders) { folder ->
                        Card(
                            onClick = { navController.navigate("folder/$folder") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Text(
                                text = folder,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    )
}