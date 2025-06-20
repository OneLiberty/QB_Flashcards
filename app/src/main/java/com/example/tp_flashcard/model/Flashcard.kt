package com.example.tp_flashcard.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val cardId: Int = 0, // Auto-incrémentation de l'ID
    val categoryId: Int,
    val question: String, // La question posée sur la carte
    val answer: String, // La réponse à la question
)