package com.example.papka.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
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

    // Удаление файлов и папок (включая рекурсивное удаление папок)
    fun deleteFileOrFolder(path: String): Boolean {
        val target = File(getBaseFolder(), sanitizePath(path))
        println("Попытка удалить файл или папку: $path. Полный путь: ${target.absolutePath}")

        if (!target.exists()) {
            // Логируем или возвращаем false, если путь не существует
            return false
        }

        return if (target.isDirectory) {
            deleteFolderRecursively(target) // Рекурсивное удаление папки
        } else {
            target.delete() // Удаление файла
        }
    }

    // Рекурсивное удаление папки и её содержимого
    private fun deleteFolderRecursively(folder: File): Boolean {
        folder.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                deleteFolderRecursively(file) // Рекурсивное удаление вложенной папки
            } else {
                file.delete() // Удаление файла
            }
        }
        return folder.delete() // Удаляем саму папку после очистки
    }

    // Проверка и санитизация пути
    private fun sanitizePath(path: String): String {
        return path.trim().replace("..", "").replace("//", "/")
    }

    // Список выделенных путей
    private val _selectedItems = mutableStateListOf<String>()
    val selectedItems: List<String> get() = _selectedItems

    // Добавление пути в список выделенных
    fun selectItem(path: String) {
        if (!_selectedItems.contains(path)) {
            _selectedItems.add(path)
        }
    }

    // Удаление пути из списка выделенных
    fun deselectItem(path: String) {
        _selectedItems.remove(path)
    }

    // Сброс списка выделенных
    fun clearSelection() {
        _selectedItems.clear()
    }

    // Удаление всех выделенных
    fun deleteSelected() {
        _selectedItems.forEach { path ->
            deleteFileOrFolder(path)
        }
        clearSelection() // Очищаем список после удаления
    }


}