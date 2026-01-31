package com.example.remidialucp2_038.data.repository

import androidx.room.withTransaction
import com.example.remidialucp2_038.data.local.AppDatabase
import com.example.remidialucp2_038.data.local.entity.AuditLogEntity
import com.example.remidialucp2_038.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

class LibraryRepository(
    private val db: AppDatabase
) {
    private val dao = db.libraryDao()

    fun getAllCategories(): Flow<List<CategoryEntity>> = dao.getAllCategories()

    suspend fun saveCategory(category: CategoryEntity, newParentId: Int?) {
        if (newParentId != null) {
            var currentParent = dao.getCategoryById(newParentId)
            while (currentParent != null) {
                if (currentParent.id == category.id) {
                    throw IllegalArgumentException("Error: Cyclic Reference terdeteksi! Kategori tidak bisa menjadi induk bagi dirinya sendiri.")
                }
                currentParent = currentParent.parentId?.let { dao.getCategoryById(it) }
            }
        }
        dao.insertCategory(category.copy(parentId = newParentId))
    }

    suspend fun deleteCategoryTransactional(categoryId: Int, deleteBooks: Boolean) {
        db.withTransaction {
            val books = dao.getBooksByCategory(categoryId)
            val bookIds = books.map { it.id }

            if (bookIds.isNotEmpty()) {
                val borrowedCount = dao.countBorrowedBooks(bookIds)
                if (borrowedCount > 0) {
                    throw IllegalStateException("GAGAL: Ada $borrowedCount buku yang sedang dipinjam. Tidak bisa menghapus kategori.")
                }
            }

            if (deleteBooks) {
                dao.softDeleteBooksByCategory(categoryId)
            } else {
                dao.releaseBooksFromCategory(categoryId)
            }

            dao.softDeleteCategory(categoryId)

            dao.insertAudit(
                AuditLogEntity(
                    action = "DELETE_CATEGORY",
                    description = "Menghapus Kategori ID: $categoryId. Opsi Hapus Buku: $deleteBooks"
                )
            )
        }
    }
}