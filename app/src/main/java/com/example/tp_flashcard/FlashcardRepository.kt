package com.example.tp_flashcard

import com.example.tp_flashcard.data.FlashcardDatabase
import com.example.tp_flashcard.model.Flashcard
import com.example.tp_flashcard.model.FlashcardCategory

class FlashcardRepository(private val database: FlashcardDatabase) {
    
    suspend fun getCategories(): List<FlashcardCategory> {
        return database.categoryDao().getAllCategories()
    }
    
    suspend fun getFlashcardsForCategory(categoryId: Int): List<Flashcard> {
        return if (categoryId == 1) {
            database.flashcardDao().getAllFlashcards().shuffled()
        } else {
            database.flashcardDao().getFlashcardsByCategory(categoryId)
        }
    }
    
    suspend fun initializeData() {
        // Si la base de données est vide, insérer les catégories et les flashcards par défaut
        if (database.categoryDao().getAllCategories().isEmpty()) {
            val categories = listOf(
                FlashcardCategory(categoryName = "Toutes les catégories"),
                FlashcardCategory(categoryName = "Mathématiques"),
                FlashcardCategory(categoryName = "Français"),
                FlashcardCategory(categoryName = "Géographie"),
                FlashcardCategory(categoryName = "Anglais")
            )
            database.categoryDao().insertCategories(categories)

            val flashcards: List<Flashcard> = listOf(
                // Mathématiques (12 cartes)
                Flashcard(categoryId = 2, question = "2 + 2", answer = "4"),
                Flashcard(categoryId = 2, question = "7 × 8", answer = "56"),
                Flashcard(categoryId = 2, question = "√16", answer = "4"),
                Flashcard(categoryId = 2, question = "15 - 9", answer = "6"),
                Flashcard(categoryId = 2, question = "12 ÷ 3", answer = "4"),
                Flashcard(categoryId = 2, question = "3²", answer = "9"),
                Flashcard(categoryId = 2, question = "25% de 80", answer = "20"),
                Flashcard(categoryId = 2, question = "Sin(90°)", answer = "1"),
                Flashcard(categoryId = 2, question = "2⁴", answer = "16"),
                Flashcard(categoryId = 2, question = "π (approximatif)", answer = "3.14"),
                Flashcard(categoryId = 2, question = "5! (factorielle)", answer = "120"),
                Flashcard(categoryId = 2, question = "log₁₀(100)", answer = "2"),

                // Français (12 cartes)
                Flashcard(categoryId = 3, question = "Conjugue 'être' à la 1ère personne du singulier au présent", answer = "Je suis"),
                Flashcard(categoryId = 3, question = "Pluriel de 'cheval'", answer = "chevaux"),
                Flashcard(categoryId = 3, question = "Synonyme de 'rapide'", answer = "vite"),
                Flashcard(categoryId = 3, question = "Auteur des Misérables", answer = "Victor Hugo"),
                Flashcard(categoryId = 3, question = "Nature du mot 'lentement'", answer = "adverbe"),
                Flashcard(categoryId = 3, question = "Féminin de 'acteur'", answer = "actrice"),
                Flashcard(categoryId = 3, question = "Participe passé de 'prendre'", answer = "pris"),
                Flashcard(categoryId = 3, question = "Complément d'objet direct dans 'Je mange une pomme'", answer = "une pomme"),
                Flashcard(categoryId = 3, question = "Figure de style : 'Il pleut des cordes'", answer = "métaphore"),
                Flashcard(categoryId = 3, question = "Antonyme de 'grand'", answer = "petit"),
                Flashcard(categoryId = 3, question = "Temps de 'j'avais mangé'", answer = "plus-que-parfait"),
                Flashcard(categoryId = 3, question = "Auteur de L'Étranger", answer = "Albert Camus"),

                // Histoire (13 cartes)
                Flashcard(categoryId = 4, question = "Quelle est la capitale de la France ?", answer = "Paris"),
                Flashcard(categoryId = 4, question = "En quelle année a eu lieu la Révolution française ?", answer = "1789"),
                Flashcard(categoryId = 4, question = "Qui a découvert l'Amérique ?", answer = "Christophe Colomb"),
                Flashcard(categoryId = 4, question = "Première guerre mondiale : années", answer = "1914-1918"),
                Flashcard(categoryId = 4, question = "Empereur français célèbre", answer = "Napoléon Bonaparte"),
                Flashcard(categoryId = 4, question = "Chute du mur de Berlin", answer = "1989"),
                Flashcard(categoryId = 4, question = "Roi Soleil", answer = "Louis XIV"),
                Flashcard(categoryId = 4, question = "Bataille de Waterloo", answer = "1815"),
                Flashcard(categoryId = 4, question = "Déclaration d'indépendance américaine", answer = "1776"),
                Flashcard(categoryId = 4, question = "Guerre de Cent Ans : adversaires", answer = "France et Angleterre"),
                Flashcard(categoryId = 4, question = "Première République française", answer = "1792"),
                Flashcard(categoryId = 4, question = "Conquête de la Gaule par", answer = "Jules César"),
                Flashcard(categoryId = 4, question = "Traité de Versailles", answer = "1919"),

                // Anglais (12 cartes)
                Flashcard(categoryId = 5, question = "Chat", answer = "Cat"),
                Flashcard(categoryId = 5, question = "Maison", answer = "House"),
                Flashcard(categoryId = 5, question = "Eau", answer = "Water"),
                Flashcard(categoryId = 5, question = "Rouge", answer = "Red"),
                Flashcard(categoryId = 5, question = "Famille", answer = "Family"),
                Flashcard(categoryId = 5, question = "École", answer = "School"),
                Flashcard(categoryId = 5, question = "Temps (météo)", answer = "Weather"),
                Flashcard(categoryId = 5, question = "Nourriture", answer = "Food"),
                Flashcard(categoryId = 5, question = "Voiture", answer = "Car"),
                Flashcard(categoryId = 5, question = "Livre", answer = "Book"),
                Flashcard(categoryId = 5, question = "Ami", answer = "Friend"),
                Flashcard(categoryId = 5, question = "Travail", answer = "Work"),
                Flashcard(categoryId = 5, question = "Traduit : Bonjour", answer = "Hello"),
            )
            database.flashcardDao().insertFlashcards(flashcards)
        }
    }
}