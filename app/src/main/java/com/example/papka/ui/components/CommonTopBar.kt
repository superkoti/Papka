package com.example.papka.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(
    title: String,
    showAddButton: Boolean = true,
    onAddClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            if (showAddButton && onAddClick != null) {
                IconButton(onClick = onAddClick) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = "Добавить"
                    )
                }
            }
        }
    )
}