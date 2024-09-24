package com.measify.kappmaker.presentation.screens.paywall

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.btn_restore_purchase
import com.measify.kappmaker.presentation.components.AgreePrivacyPolicyTermsConditionsText
import com.measify.kappmaker.presentation.components.AppToolbar
import com.measify.kappmaker.presentation.components.FullScreenLoading
import com.measify.kappmaker.presentation.components.MessageDialog
import com.measify.kappmaker.util.extensions.productDescription
import com.measify.kappmaker.util.extensions.productName
import com.revenuecat.purchases.kmp.Package
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaywallScreen(
    modifier: Modifier = Modifier,
    uiStateHolder: PaywallUiStateHolder,
    onDismiss: () -> Unit,
) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    uiState.message?.let {
        MessageDialog(
            text = it.value,
            onConfirm = {
                uiStateHolder.onMessageShown()
                onDismiss()
            }
        )
    }
    PaywallScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = uiStateHolder::onUiEvent,
        onDismiss = onDismiss
    )
}

@Composable
fun PaywallScreen(
    modifier: Modifier = Modifier,
    uiState: PaywallUiState,
    onUiEvent: (PaywallUiEvent) -> Unit,
    onDismiss: () -> Unit,
) {
    Column(modifier = modifier) {
        AppToolbar(
            title = "",
            navigationIcon = Icons.Default.Close,
            onNavigationIconClick = { onDismiss() }
        )

        if (uiState.isLoading) {
            FullScreenLoading()
        } else
            PaywallScreenData(
                modifier = modifier.padding(20.dp),
                uiState = uiState,
                onUiEvent = onUiEvent
            )
    }


}

@Composable
private fun PaywallScreenData(
    modifier: Modifier = Modifier,
    uiState: PaywallUiState,
    onUiEvent: (PaywallUiEvent) -> Unit
) {

    Column(modifier = modifier) {
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            item { PaywallTitle(text = uiState.title) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(uiState.features.size) {
                PaywallFeature(
                    text = uiState.features[it],
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(uiState.packages.size) {
                val rcPackage = uiState.packages[it]
                PackageItem(
                    rcPackage = rcPackage,
                    isSelected = rcPackage == uiState.selectedPackage,
                    onPackageSelected = {
                        onUiEvent(PaywallUiEvent.OnSelectPackage(uiState.packages[it]))
                    },
                )
            }

        }
        ElevatedButton(
            enabled = uiState.buyButtonEnabled,
            onClick = { onUiEvent(PaywallUiEvent.OnClickBuy) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
        ) {
            Text(
                text = uiState.buyButtonText,
                modifier = Modifier.padding(horizontal = 40.dp, vertical = 6.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { onUiEvent(PaywallUiEvent.OnClickRestore) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.btn_restore_purchase))
        }

        Spacer(modifier = Modifier.height(8.dp))
        AgreePrivacyPolicyTermsConditionsText(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun PaywallTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
private fun PaywallFeature(
    modifier: Modifier = Modifier,
    text: String
) {
    Row {
        Icon(
            Icons.Default.Check,
            contentDescription = "Check",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, modifier = modifier)
    }

}


@Composable
fun PackageItem(
    modifier: Modifier = Modifier,
    rcPackage: Package,
    isSelected: Boolean,
    onPackageSelected: () -> Unit
) {
    val shape = RoundedCornerShape(10.dp)
    val borderColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(shape)
            .clickable { onPackageSelected() },
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.contentColorFor(containerColor)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Box(
                    modifier = Modifier.size(24.dp).border(1.dp, borderColor, CircleShape)
                        .padding(4.dp)
                ) {
                    if (isSelected) Icon(Icons.Default.Check, contentDescription = "Check")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = rcPackage.productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = rcPackage.productDescription, style = MaterialTheme.typography.bodyMedium)
        }
    }
}