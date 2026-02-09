package com.example.psassignment.ui.screens.drivers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.psassignment.domain.model.Assignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriversListScreen(
    viewModel: DriversViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedAssignment by viewModel.selectedAssignment.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Driver Assignments") })
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = uiState) {
                is DriversViewModel.UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DriversViewModel.UiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DriversViewModel.UiState.Success -> {
                    DriverList(
                        assignments = state.assignments,
                        onDriverClick = { viewModel.onDriverSelected(it) }
                    )
                }
            }
        }
    }

    // Detail Dialog - Shows when a user taps a driver
    selectedAssignment?.let { assignment ->
        ShipmentDialog(
            assignment = assignment,
            onDismiss = { viewModel.onDismissDialog() }
        )
    }
}

@Composable
fun DriverList(
    assignments: List<Assignment>,
    onDriverClick: (Assignment) -> Unit
) {
    LazyColumn {
        items(assignments) { assignment ->
            ListItem(
                headlineContent = { Text(assignment.driverName) },
                leadingContent = {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Person,
                        contentDescription = "Driver Icon"
                    )
                },
                modifier = Modifier
                    .clickable { onDriverClick(assignment) }
                    .fillMaxWidth()
            )
            Divider()
        }
    }
}

@Composable
fun ShipmentDialog(
    assignment: Assignment,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Assignment Details") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Driver: ${assignment.driverName}", style = MaterialTheme.typography.titleMedium)
                Divider()
                Text(text = "Shipment Address:", style = MaterialTheme.typography.labelLarge)
                Text(text = assignment.shipmentAddress, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Suitability Score: ${assignment.suitabilityScore}", style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}