package com.example.tp_flashcard.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tp_flashcard.model.FlashcardCategory

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<FlashcardCategory>

    @Insert
    suspend fun insertCategories(categories: List<FlashcardCategory>)
}