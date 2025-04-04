package com.example.papka.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(
    title: String,
    showAddButton: Boolean = true, // Показывать кнопку "Добавить"
    onAddClick: (() -> Unit)? = null, // Логика для кнопки "Добавить"
    onSelectClick: (() -> Unit)? = null, // Логика активации режима удаления
    onCancelClick: (() -> Unit)? = null, // Логика для кнопки "Отменить"
    onDeleteClick: (() -> Unit)? = null, // Логика для кнопки "Удалить"
) {
    // Состояние, которое отслеживает активен ли режим удаления
    var isDeleteMode by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = title) },
        actions = {
            // Управляем отображением кнопок в зависимости от состояния
            if (isDeleteMode) {
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
            } else {
                // Обычный режим, отображаем стандартные действия
                if (showAddButton && onAddClick != null) {
                    IconButton(onClick = onAddClick) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = "Добавить"
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
            }
        }
    )
}