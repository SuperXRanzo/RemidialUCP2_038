package com.example.remidialucp2_038

import android.app.Application
import com.example.remidialucp2_038.data.local.AppDatabase
import com.example.remidialucp2_038.data.repository.LibraryRepository

class BaseApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }

    val repository by lazy { LibraryRepository(database) }
}