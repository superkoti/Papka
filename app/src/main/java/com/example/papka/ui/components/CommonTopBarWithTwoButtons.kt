package com.example.papka.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBarWithTwoButtons(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onAddFolderClick: (() -> Unit)? = null,
    onAddFileClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (showBackButton && onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            }
        },
        actions = {
            // Кнопка "Добавить папку"
            if (onAddFolderClick != null) {
                IconButton(onClick = onAddFolderClick) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = "Добавить папку"
                    )
                }
            }
            // Кнопка "Добавить файл"
            if (onAddFileClick != null) {
                IconButton(onClick = onAddFileClick) {
                    Icon(
                        imageVector = Icons.Default.FileUpload,
                        contentDescription = "Добавить файл"
                    )
                }
            }
        }
    )
}