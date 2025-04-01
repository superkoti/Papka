package com.example.papka.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.papka.viewmodel.FoldersViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.focus.focusRequester


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(
    navController: NavController,
    folderName: String,
    foldersViewModel: FoldersViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = folderName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
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
                var folderNameInput by remember { mutableStateOf(TextFieldValue("")) }
                val focusRequester = remember { FocusRequester() }

                // Кнопка добавления папки
                Button(
                    onClick = {
                        isAccordionExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (isAccordionExpanded) "Закрыть" else "Добавить папку")
                }

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
                            OutlinedTextField(
                                value = folderNameInput,
                                onValueChange = { folderNameInput = it },
                                label = { Text("Введите название папки") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = {
                                        if (folderNameInput.text.isNotBlank()) {
                                            foldersViewModel.addFolder(folderNameInput.text)
                                            folderNameInput = TextFieldValue("")
                                            isAccordionExpanded = false
                                        }
                                    }
                                ) {
                                    Text("ОК")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = {
                                    folderNameInput = TextFieldValue("")
                                    isAccordionExpanded = false
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Отмена")
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBar(title: @Composable () -> Unit, navigationIcon: @Composable () -> Unit) {
    TopAppBar(
        title = title,
        navigationIcon = navigationIcon
    )
}