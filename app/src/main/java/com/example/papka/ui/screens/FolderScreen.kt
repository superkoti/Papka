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


@Composable
fun FolderScreen(
    navController: NavController,
    folderPath: String,
    foldersViewModel: FoldersViewModel = viewModel()
) {
    // Используем Uri.decode для декодирования пути
    val decodedFolderPath = Uri.decode(folderPath)

    // Проверка пути и получение содержимого
    val contents: List<File> = remember {
        val fullPath = foldersViewModel.getFullPath(decodedFolderPath)
        if (fullPath.exists() && fullPath.isDirectory) {
            foldersViewModel.getFolderContents(decodedFolderPath)
        } else {
            emptyList() // Если папка не существует, возвращаем пустой список
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Верхняя панель с навигацией
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
                            // Кодируем путь перед передачей в навигацию
                            navController.navigate("folder_screen/${Uri.encode(file.name)}")
                        }
                    }
                )
            }
        }
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