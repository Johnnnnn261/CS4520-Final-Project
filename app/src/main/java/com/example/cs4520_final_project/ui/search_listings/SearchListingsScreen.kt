package com.example.cs4520_final_project.ui.search_listings


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cs4520_final_project.data.CampusSwapRepository
import com.example.cs4520_final_project.data.ListingInfo
import com.example.cs4520_final_project.ui.common.ListingCard
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchListingsScreen(
    listings: List<ListingInfo>, onListingClick: (Long) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedPayment by remember { mutableStateOf<String?>(null) }
    var paymentDropdownExpanded by remember { mutableStateOf(false) }
    val paymentOptions = listOf("Zelle", "Venmo", "Cash", "PayPal")

    val displayedListings = remember(listings, searchQuery, selectedPayment) {
        val q = searchQuery.trim().lowercase()
        listings
            .filter { q.isBlank() || it.title.lowercase().contains(q) || it.description.lowercase().contains(q) }
            .filter { selectedPayment == null || it.payment == selectedPayment }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listings") }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search listings...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            ExposedDropdownMenuBox(
                expanded = paymentDropdownExpanded,
                onExpandedChange = { paymentDropdownExpanded = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp)
            ) {
                OutlinedTextField(
                    value = selectedPayment ?: "All payment types",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Payment") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = paymentDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = paymentDropdownExpanded,
                    onDismissRequest = { paymentDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All payment types") },
                        onClick = {
                            selectedPayment = null
                            paymentDropdownExpanded = false
                        }
                    )
                    paymentOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedPayment = option
                                paymentDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            if (displayedListings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (searchQuery.isBlank() && selectedPayment == null)
                            "No listings yet. Tap + to create your first post."
                        else
                            "No listings match your filters."
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(displayedListings) { listing ->
                        ListingCard(listing = listing, onClick = { onListingClick(listing.id) })
                    }
                }
            }
        }
    }
}