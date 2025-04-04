package com.example.papka.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBarWithTwoButtons(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onAddFolderClick: (() -> Unit)? = null,
    onAddFileClick: (() -> Unit)? = null,
    onSelectClick: (() -> Unit)? = null,
    onCancelClick: (() -> Unit)? = null, // Логика для кнопки "Отменить"
    onDeleteClick: (() -> Unit)? = null, // Логика для кнопки "Удалить"
) {
    // Состояние, которое отслеживает активен ли режим удаления
    var isDeleteMode by remember { mutableStateOf(false) }

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
            if (!isDeleteMode) {
                // Кнопка "Добавить файл"
                if (onAddFileClick != null) {
                    IconButton(onClick = onAddFileClick) {
                        Icon(
                            imageVector = Icons.Default.FileUpload,
                            contentDescription = "Добавить файл"
                        )
                    }
                }
                // Кнопка "Добавить папку"
                if (onAddFolderClick != null) {
                    IconButton(onClick = onAddFolderClick) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = "Добавить папку"
                        )
                    }
                }
                if (onSelectClick != null) {
                    IconButton(onClick = {
                        isDeleteMode = true // Переходим в режим удаления
                        onSelectClick() // Выполнить логику при выборе режима удаления
                    }) {
                        Icon(
                            imageVector = Icons.Default.Checklist,
                            contentDescription = "Режим выделения"
                        )
                    }
                }
            } else{
                // Показываем кнопки "Отменить" и "Удалить" только в режиме удаления
                if (onCancelClick != null) {
                    IconButton(onClick = {
                        isDeleteMode = false // Возврат к исходному состоянию
                        onCancelClick() // Выполнить логику отмены, если есть
                    }) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Кнопка отменить"
                        )
                    }
                }
                if (onDeleteClick != null) {
                    IconButton(onClick = {
                        isDeleteMode = false // Возврат к исходному состоянию после удаления
                        onDeleteClick() // Выполнить логику удаления
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Кнопка удалить"
                        )
                    }
                }
            }
        }
    )
}