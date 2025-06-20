package com.example.tp_flashcard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp_flashcard.data.FlashcardDatabase
import com.example.tp_flashcard.model.FlashcardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class FlashcardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FlashcardRepository(FlashcardDatabase.getDatabase(application))

    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    fun loadFlashcards(categoryId: Int?) {
        viewModelScope.launch {
            val cards = if (categoryId != null) {
                repository.getFlashcardsForCategory(categoryId)
            } else {
                emptyList()
            }

            _uiState.update { currentState ->
                currentState.copy(
                    cardList = cards,
                    currentFlashCardIndex = 0,
                    isRevisionDone = cards.isEmpty(),
                    isShowingAnswer = false
                )
            }
        }
    }

    fun showAnswer() {
        _uiState.update { currentState ->
            currentState.copy(isShowingAnswer = true)
        }
    }

    fun hideAnswer() {
        _uiState.update { currentState ->
            currentState.copy(isShowingAnswer = false)
        }
    }

    fun resetAnswer() {
        _uiState.update { currentState ->
            currentState.copy(isShowingAnswer = false)
        }
    }

    fun showNextCard() {
        _uiState.update { currentState ->
            val nextIndex = currentState.currentFlashCardIndex + 1
            currentState.copy(
                currentFlashCardIndex = nextIndex,
                isRevisionDone = nextIndex >= currentState.cardList.size,
                isShowingAnswer = false
            )
        }
    }

    fun showPreviousCard() {
        _uiState.update { currentState ->
            val previousIndex = (currentState.currentFlashCardIndex - 1).coerceAtLeast(0)
            currentState.copy(
                currentFlashCardIndex = previousIndex,
                isRevisionDone = false,
                isShowingAnswer = false
            )
        }
    }
}