package com.example.remidialucp2_038.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.remidialucp2_038.data.local.entity.*

@Dao
interface LibraryDao {
    @Query("SELECT * FROM categories WHERE isDeleted = 0")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Query("UPDATE categories SET isDeleted = 1 WHERE id = :id")
    suspend fun softDeleteCategory(id: Int)

    @Query("SELECT * FROM books WHERE categoryId = :categoryId AND isDeleted = 0")
    suspend fun getBooksByCategory(categoryId: Int): List<BookEntity>

    @Query("UPDATE books SET categoryId = NULL WHERE categoryId = :categoryId")
    suspend fun releaseBooksFromCategory(categoryId: Int)

    @Query("UPDATE books SET isDeleted = 1 WHERE categoryId = :categoryId")
    suspend fun softDeleteBooksByCategory(categoryId: Int)

    @Query("SELECT * FROM categories")
    fun getAllCategoriesList(): List<CategoryEntity>

    @Insert
    fun insertBook(book: BookEntity): Long

    @Insert
    fun insertPhysicalBook(phys: PhysicalBookEntity)
    @Query("""
        SELECT COUNT(*) FROM physical_books 
        WHERE bookId IN (:bookIds) AND status = 'DIPINJAM'
    """)
    suspend fun countBorrowedBooks(bookIds: List<Int>): Int
    @Insert
    suspend fun insertAudit(log: AuditLogEntity)
}