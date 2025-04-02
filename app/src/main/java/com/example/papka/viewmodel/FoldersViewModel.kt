package com.example.papka.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.io.File

class FoldersViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    // Возвращает базовую директорию для всех папок
    private fun getBaseFolder(): File {
        val baseFolder = File(context.filesDir, "folders")
        if (!baseFolder.exists()) {
            baseFolder.mkdirs()
        }
        return baseFolder
    }

    // Чтение содержимого указанной папки с проверкой на существование
    fun getFolderContents(folderPath: String): List<File> {
        val folder = File(getBaseFolder(), sanitizePath(folderPath))
        if (!folder.exists() || !folder.isDirectory) {
            // Логируем ошибку или возвращаем пустой список, если папка недействительна
            return emptyList()
        }
        return folder.listFiles()?.toList() ?: emptyList()
    }

    // Проверка, является ли файл папкой
    fun isFolder(file: File): Boolean {
        return file.isDirectory
    }

    // Получаем полный путь до папки с проверкой
    fun getFullPath(folderPath: String): File {
        return File(getBaseFolder(), sanitizePath(folderPath))
    }

    // Добавление папки с проверкой
    fun addFolder(folderPath: String): Boolean {
        if (folderPath.isBlank()) return false

        val newFolder = File(getBaseFolder(), sanitizePath(folderPath))
        return if (!newFolder.exists()) {
            newFolder.mkdirs() // mkdirs для создания вложенных папок
        } else {
            false
        }
    }

    // Проверка и санитизация пути
    private fun sanitizePath(path: String): String {
        return path.trim().replace("..", "").replace("//", "/")
    }

}