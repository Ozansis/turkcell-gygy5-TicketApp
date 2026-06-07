package com.turkcell.ticketapp.screen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.turkcell.core.domain.event.TicketType
import com.turkcell.ticketapp.R

@SuppressLint("DefaultLocale")
@Composable
fun TicketCard(
    ticket: TicketType,
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sol taraf: isim ve kalan
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ticket.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(R.string.ticket_remaining, ticket.remaining, ticket.capacity),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Sağ taraf: fiyat ve kontrol
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₺${String.format("%.2f", ticket.priceCents.toDouble() / 100.0)}",
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))

                if (ticket.remaining==0L) {
                    Text(
                        text = stringResource(R.string.ticket_sold_out),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    QuantitySelector(
                        quantity = quantity,
                        onDecrease = onDecrease,
                        onIncrease = onIncrease
                    )
                }
            }
        }
    }
}
