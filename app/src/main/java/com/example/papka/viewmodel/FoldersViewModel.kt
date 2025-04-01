package com.example.papka.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class FoldersViewModel : ViewModel() {
    // Список папок (будет сохраняться между переходами)
    val folders = mutableStateListOf("Папка 1", "Папка 2", "Папка 3")

    // Добавление папки
    fun addFolder(folderName: String) {
        if (folderName.isNotBlank()) {
            folders.add(folderName)
        }
    }
}
