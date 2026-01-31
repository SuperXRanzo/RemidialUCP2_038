package com.example.remidialucp2_038.data.repository

import androidx.room.withTransaction
import com.example.remidialucp2_038.data.local.AppDatabase
import com.example.remidialucp2_038.data.local.entity.AuditLogEntity
import com.example.remidialucp2_038.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

class LibraryRepository(private val db: AppDatabase) {
    private val dao = db.libraryDao()

    fun getAllCategories(): Flow<List<CategoryEntity>> = dao.getAllCategories()

    suspend fun saveCategory(category: CategoryEntity, newParentId: Int?) {
        dao.insertCategory(category.copy(parentId = newParentId))
    }

    suspend fun deleteCategoryTransactional(categoryId: Int, deleteBooks: Boolean) {
        db.withTransaction {
        }
    }
}