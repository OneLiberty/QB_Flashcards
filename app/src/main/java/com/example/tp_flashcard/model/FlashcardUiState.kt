package com.example.tp_flashcard.model

data class FlashcardUiState(
    val currentFlashCardIndex: Int = 0,
    val cardList: List<Flashcard> = emptyList(),
    val isRevisionDone: Boolean = false,
    val isShowingAnswer: Boolean = false
) {
    val isSessionFinished: Boolean
        get() = cardList.isNotEmpty() && currentFlashCardIndex >= cardList.size
    val currentCard: Flashcard?
        get() = cardList.getOrNull(currentFlashCardIndex)
}