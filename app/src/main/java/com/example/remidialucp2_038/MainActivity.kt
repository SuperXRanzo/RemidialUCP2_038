package com.example.remidialucp2_038.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import com.example.remidialucp2_038.data.local.entity.CategoryEntity
import com.example.remidialucp2_038.data.repository.LibraryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: LibraryRepository) : ViewModel() {

    val categories: StateFlow<List<CategoryEntity>> = repository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCategory(name: String, parentId: Int?, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val newCategory = CategoryEntity(name = name, parentId = parentId)
                repository.saveCategory(newCategory, parentId)
            } catch (e: Exception) {
                onError(e.message ?: "Gagal menyimpan kategori")
            }
        }
    }

    fun deleteCategory(
        categoryId: Int,
        deleteBooks: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.deleteCategoryTransactional(categoryId, deleteBooks)
                onSuccess()
            } catch (e: IllegalStateException) {
                onError(e.message ?: "Gagal menghapus: Masih ada buku dipinjam!")
            } catch (e: Exception) {
                onError("Terjadi kesalahan sistem: ${e.message}")
            }
        }
    }
}