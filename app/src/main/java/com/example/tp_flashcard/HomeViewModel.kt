package com.example.tp_flashcard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp_flashcard.data.FlashcardDatabase
import com.example.tp_flashcard.model.FlashcardCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FlashcardRepository(FlashcardDatabase.getDatabase(application))
    
    private val _categories = MutableStateFlow<List<FlashcardCategory>>(emptyList())
    val categories: StateFlow<List<FlashcardCategory>> = _categories

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val categories = repository.getCategories()
            _categories.value = categories
        }
    }
}