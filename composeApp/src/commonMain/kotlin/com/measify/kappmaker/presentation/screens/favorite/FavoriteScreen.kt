package com.measify.kappmaker.presentation.screens.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.measify.kappmaker.presentation.components.PrimaryButton

@Composable
fun FavoriteScreen(modifier: Modifier = Modifier, onPaymentRequired: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Favorite Screen")
        PrimaryButton(text = "Get Premium Content") {
            onPaymentRequired()
        }
    }
}