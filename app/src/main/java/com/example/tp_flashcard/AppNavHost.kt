package com.example.tp_flashcard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tp_flashcard.model.FlashcardCategory
import com.example.tp_flashcard.model.FlashcardUiState
import kotlinx.coroutines.launch

@Composable
fun FlashcardNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
           HomeScreen(
               onCategoryClick = { category ->
                    navController.navigate("revision/${category.categoryId}")
                }
           )
        }
        composable("revision/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
            FlashCardScreen(
                categoryId = categoryId,
                navController = navController
            )
        }
    }
}

// Composable gérant l'affichage d'une catégorie
@Composable
fun CategoryItem(
    category: FlashcardCategory,
    modifier: Modifier = Modifier,
    onItemClick: (FlashcardCategory) -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onItemClick(category) }
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = category.categoryName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel(), onCategoryClick: (FlashcardCategory) -> Unit) {
    val categories by homeViewModel.categories.collectAsState()

    if (categories.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Flashcards",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            CircularProgressIndicator()
            Text(
                text = "Chargement en cours...",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Flashcards",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Des cartes pour réviser",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 48.dp),
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(), 
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(categories.size) { index ->
                    CategoryItem(
                        category = categories[index],
                        onItemClick = { selectedCategory ->
                            onCategoryClick(selectedCategory)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FlashCardScreen(flashcardViewModel: FlashcardViewModel = viewModel(), categoryId: Int?, navController: NavHostController? = null) {
    LaunchedEffect(categoryId) {
        flashcardViewModel.loadFlashcards(categoryId)
    }

    val uiState by flashcardViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (uiState.cardList.isEmpty() && !uiState.isRevisionDone) {
            // si on a un soucis de chargement (serveur lent ...)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Chargement en cours des cartes...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else if (uiState.isRevisionDone || uiState.currentCard == null) {
            // Ecran de fin de révision
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Fin de la révision",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                ElevatedButton(
                    onClick = {
                        navController?.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Retour à l'accueil")
                }
            }
        } else {
            // Ecran de révision
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top
            ) {
                LinearProgressIndicator(
                    progress = { (uiState.currentFlashCardIndex + 1).toFloat() / uiState.cardList.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(8.dp)
                )
                FlashCardContent(
                    uiState = uiState,
                    flashcardViewModel = flashcardViewModel
                )
            }

            // bouton de navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ElevatedButton(
                    onClick = {
                        flashcardViewModel.showPreviousCard()
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.weight(1f),
                    enabled = uiState.currentFlashCardIndex > 0,
                    elevation = ButtonDefaults.buttonElevation(3.dp),
                ) {
                    Text(text = "Précédent")
                }

                ElevatedButton(
                    onClick = {
                        flashcardViewModel.showNextCard()
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.weight(1f),
                    enabled = uiState.currentFlashCardIndex < uiState.cardList.size,
                    elevation = ButtonDefaults.buttonElevation(3.dp)
                ) {
                    Text(text = if (uiState.currentFlashCardIndex == uiState.cardList.size - 1) "Terminer" else "Suivant")
                }
            }
        }
    }
}

// Composable régulant une flashcard
@Composable
fun FlashCardContent(
    uiState: FlashcardUiState,
    flashcardViewModel: FlashcardViewModel? = null
) {
    val currentCard = uiState.currentCard ?: return
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val rotation = remember { Animatable(0f) }
    val isShowingAnswer = rotation.value > 90f

    LaunchedEffect(uiState.currentFlashCardIndex) {
        rotation.snapTo(0f)
        flashcardViewModel?.resetAnswer()
    }

    // Animation de rotation pour montrer/cacher la réponse
    LaunchedEffect(uiState.isShowingAnswer) {
        if (uiState.isShowingAnswer) {
            rotation.animateTo(
                targetValue = 180f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
        } else {
            rotation.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    AnimatedContent(
        targetState = uiState.currentFlashCardIndex,
        transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally { it } + fadeIn() togetherWith 
                slideOutHorizontally { -it } + fadeOut()
            } else {
                slideInHorizontally { -it } + fadeIn() togetherWith 
                slideOutHorizontally { it } + fadeOut()
            }
        },
        label = "card_transition"
    ) { cardIndex ->
        val animatedCard = uiState.cardList.getOrNull(cardIndex) ?: currentCard
        OutlinedCard(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 320.dp)
                .graphicsLayer {
                    rotationY = rotation.value
                    cameraDistance = 8 * density.density
                },
            onClick = {
                coroutineScope.launch {
                    if (rotation.value < 90f) {
                        flashcardViewModel?.showAnswer()
                    } else {
                        flashcardViewModel?.hideAnswer()
                    }
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) { 
                Text(
                    text = if (isShowingAnswer) "Réponse" else "Question",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .graphicsLayer { rotationY = if (isShowingAnswer) 180f else 0f }
                )

                Text(
                    text = if (isShowingAnswer) animatedCard.answer else animatedCard.question,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .graphicsLayer { rotationY = if (isShowingAnswer) 180f else 0f }
                )

                if (!isShowingAnswer) {
                    Text(
                        text = "Appuyez pour voir la réponse",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            }
        }
    }
}