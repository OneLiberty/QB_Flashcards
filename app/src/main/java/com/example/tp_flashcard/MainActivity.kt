package com.example.tp_flashcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tp_flashcard.data.FlashcardDatabase
import com.example.tp_flashcard.ui.theme.TP_FlashcardTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TP_FlashcardTheme {
                var isDataInitialized by remember { mutableStateOf(false) }
                
                LaunchedEffect(Unit) {
                    val database = FlashcardDatabase.getDatabase(this@MainActivity)
                    val repository = FlashcardRepository(database)
                    repository.initializeData()
                    isDataInitialized = true
                }

                if (isDataInitialized) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        FlashcardNavHost(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                } else {
                    // Ecran de chargement
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(60.dp),
                                strokeWidth = 4.dp
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Initialisation des données...",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}