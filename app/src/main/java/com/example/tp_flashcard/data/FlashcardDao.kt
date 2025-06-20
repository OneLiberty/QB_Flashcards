package com.example.tp_flashcard.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tp_flashcard.model.Flashcard

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards WHERE categoryId = :categoryId")
    suspend fun getFlashcardsByCategory(categoryId: Int): List<Flashcard>

    @Query("SELECT * FROM flashcards")
    suspend fun getAllFlashcards(): List<Flashcard>

    @Insert
    suspend fun insertFlashcard(flashcard: Flashcard)

    @Insert
    suspend fun insertFlashcards(flashcards: List<Flashcard>)
}
