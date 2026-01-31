package com.example.remidialucp2_038

import android.app.Application
import com.example.remidialucp2_038.data.local.AppDatabase
import com.example.remidialucp2_038.data.local.entity.BookEntity
import com.example.remidialucp2_038.data.local.entity.CategoryEntity
import com.example.remidialucp2_038.data.local.entity.PhysicalBookEntity
import com.example.remidialucp2_038.data.repository.LibraryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BaseApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { LibraryRepository(database) }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            val dao = database.libraryDao()

            if (dao.getAllCategoriesList().isEmpty()) {
                val catId = dao.insertCategory(CategoryEntity(name = "Fiksi Ilmiah", parentId = null)).toInt()

                val bookId = dao.insertBook(BookEntity(title = "Dune", categoryId = catId)).toInt()

                dao.insertPhysicalBook(PhysicalBookEntity(uniquePhysicalId = "FISIK-001", bookId = bookId, status = "DIPINJAM"))
            }
        }
    }
}