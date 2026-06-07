package com.turkcell.ticketapp.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.turkcell.ticketapp.R
import com.turkcell.ticketapp.screen.components.CameraPermissionHandler
import com.turkcell.ticketapp.viewmodel.StaffViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffScreen(
    viewModel: StaffViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val scanPrompt = stringResource(R.string.staff_qr_scan_prompt)

    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        result.contents?.let { viewModel.scan(it) }
    }

    val requestCameraPermission = CameraPermissionHandler(
        onPermissionGranted = {
            scanLauncher.launch(
                ScanOptions().apply {
                    setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                    setPrompt(scanPrompt)
                    setBeepEnabled(true)
                    setOrientationLocked(false)
                }
            )
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.staff_screen_title)) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = requestCameraPermission,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.staff_btn_scan_qr))
            }

            state.scanResult?.let { result ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            stringResource(R.string.staff_checkin_success),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(stringResource(R.string.staff_label_ticket, result.ticketType))
                        Text(stringResource(R.string.staff_label_event, result.eventName))
                        Text(stringResource(R.string.staff_label_venue, result.venue))
                        Text(stringResource(R.string.staff_label_checkin_time, result.checkedInAt))
                    }
                }
            }

            state.errorMessage?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error)
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Text(
                stringResource(R.string.staff_assigned_events_title),
                style = MaterialTheme.typography.titleMedium
            )

            when {
                state.isLoading && state.events.isEmpty() -> {
                    CircularProgressIndicator()
                }
                state.events.isEmpty() -> {
                    Text(stringResource(R.string.staff_empty_events))
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.events) { event ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(event.name, style = MaterialTheme.typography.bodyLarge)
                                    Text(event.place, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
