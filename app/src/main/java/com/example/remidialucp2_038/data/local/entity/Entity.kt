package com.example.remidialucp2_038.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val parentId: Int?,
    val isDeleted: Boolean = false
)

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val categoryId: Int?,
    val isDeleted: Boolean = false
)

@Entity(
    tableName = "physical_books",
    foreignKeys = [ForeignKey(
        entity = BookEntity::class,
        parentColumns = ["id"],
        childColumns = ["bookId"]
    )]
)
data class PhysicalBookEntity(
    @PrimaryKey val uniquePhysicalId: String,
    val bookId: Int,
    val status: String
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val action: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)