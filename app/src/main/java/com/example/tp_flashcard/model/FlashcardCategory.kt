package com.example.tp_flashcard.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class FlashcardCategory(
    @PrimaryKey(autoGenerate = true) val categoryId: Int = 0,
    val categoryName: String,
)