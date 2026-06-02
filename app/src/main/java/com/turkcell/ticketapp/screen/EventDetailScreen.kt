package com.turkcell.ticketapp.screen

import android.R.attr.bottom
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.core.util.formatEventDate
import com.turkcell.ticketapp.navigation.EventDetail
import com.turkcell.ticketapp.screen.components.BottomPurchaseBar
import com.turkcell.ticketapp.screen.components.TicketCard
import com.turkcell.ticketapp.viewmodel.EventDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    id: String,
    viewModel: EventDetailViewModel = koinViewModel { parametersOf(id) },
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onPurchaseSuccess: () -> Unit = {}
) {


    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isPaid) {
        if (state.isPaid) onPurchaseSuccess()
    }

    if (state.pendingPurchaseId != null) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Ödeme Onayı") },
            text = { Text("Toplam: ₺${"%.2f".format(state.totalCents / 100.0)}. Onaylıyor musun?") },
            confirmButton = {
                Button(onClick = { viewModel.pay(state.pendingPurchaseId!!) }) {
                    Text("Onayla")
                }
            },
            dismissButton = {
                TextButton(onClick = {}) {
                    Text("Vazgeç")
                }
            }
        )
    }

    if (state.isLoading) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (state.errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = state.errorMessage!!)
        }
        return
    }

    val event = state.event ?: return


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Etkinlik Detay", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onShareClick) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
            )
        },
        bottomBar = {
            BottomPurchaseBar(
                totalPrice = state.totalCents /100.0,
                {viewModel.createPurchase()}
            )
        }

    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(event.name, style = MaterialTheme.typography.headlineSmall)
                    Text(event.place, style = MaterialTheme.typography.bodyMedium)
                    Text(formatEventDate(event.startsAt), style = MaterialTheme.typography.bodySmall)

                    Spacer(Modifier.height(16.dp))
                    Text("Açıklama", style = MaterialTheme.typography.titleMedium)
                    Text(event.description, style = MaterialTheme.typography.bodyMedium)

                    Spacer(Modifier.height(16.dp))
                    Text("Bilet Seçimi", style = MaterialTheme.typography.titleMedium)
                }
            }

            items(event.ticketTypes) { ticketType ->
                TicketCard(
                    ticket = ticketType,
                    quantity = state.ticketQuantities[ticketType.id] ?: 0,
                    onIncrease = { viewModel.increaseTicket(ticketType) },
                    onDecrease = { viewModel.decreaseTicket(ticketType) }
                )
            }



        }

    }

}

