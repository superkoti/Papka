package com.example.papka.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(navController: NavController, folderName: String) {
    var description by remember { mutableStateOf("Описание папки...") }

    val subFolders = listOf("Подпапка 1", "Подпапка 2")
    val files = listOf("Файл 1.txt", "Файл 2.pdf", "Файл 3.jpg")

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = folderName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
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
                Row {
                    Button(onClick = { /* Добавить папку */ }) {
                        Text("Добавить папку")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { /* Добавить файлы */ }) {
                        Text("Добавить файлы")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Описание:", style = MaterialTheme.typography.bodyMedium)

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Подпапки:", style = MaterialTheme.typography.titleMedium)
                LazyColumn {
                    items(subFolders) { subFolder ->
                        Column {
                            Text(
                                text = subFolder,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navController.navigate("folder/$subFolder") }
                                    .padding(8.dp)
                            )
                            Divider()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Файлы:", style = MaterialTheme.typography.titleMedium)
                LazyColumn {
                    items(files) { file ->
                        Column {
                            Text(
                                text = file,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { /* Открытие файла */ }
                                    .padding(8.dp)
                            )
                            Divider()
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