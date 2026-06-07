package com.turkcell.ticketapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.ticketapp.R
import com.turkcell.ticketapp.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onEventClick: (String) -> Unit,
    onTicketClick: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tabs = listOf(stringResource(R.string.tab_events), stringResource(R.string.tab_my_tickets))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_bar_title)) },
                actions = {
                    IconButton(onClick = { viewModel.logout() }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = stringResource(R.string.cd_logout))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = state.selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = state.selectedTab == index,
                        onClick = { viewModel.onTabSelected(index) },
                        text = { Text(title) }
                    )
                }
            }

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            when (state.selectedTab) {
                0 -> PullToRefreshBox(
                    isRefreshing = state.isEventsLoading,
                    onRefresh = { viewModel.loadEvent() },
                    modifier = Modifier.weight(1f)
                ) {
                    when{
                        state.isEventsLoading && state.events.isEmpty() -> {
                            CircularProgressIndicator()
                        }
                        state.events.isEmpty() -> {
                            Text(stringResource(R.string.empty_events))
                        }
                        else -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.events) { event ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onEventClick(event.id) }
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(event.name, style = MaterialTheme.typography.titleMedium)
                                            Text(event.place, style = MaterialTheme.typography.bodyMedium)
                                            Text(event.startsAt, style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                }
                            }
                        }

                    }

                }

                1 -> PullToRefreshBox(
                    isRefreshing = state.isTicketsLoading,
                    onRefresh = { viewModel.loadTicket() },
                    modifier = Modifier.weight(1f)
                ) {
                    when{
                        state.isTicketsLoading && state.tickets.isEmpty() -> {
                            CircularProgressIndicator()
                        }
                        state.tickets.isEmpty() -> {
                            Text(stringResource(R.string.empty_tickets))
                        }
                        else -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.tickets) { ticket ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onTicketClick(ticket.id) }
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(ticket.eventName, style = MaterialTheme.typography.titleMedium)
                                            Text(ticket.ticketTypeName, style = MaterialTheme.typography.bodyMedium)
                                            Text(ticket.status.name, style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                }
                            }
                        }

                    }



                }
            }
        }
    }
}
