package com.example.cs4520_final_project.ui.search_listings


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cs4520_final_project.data.CampusSwapRepository
import com.example.cs4520_final_project.data.ListingInfo
import com.example.cs4520_final_project.ui.common.DropdownOption
import com.example.cs4520_final_project.ui.common.ListingCard
import com.example.cs4520_final_project.ui.common.conditionOptions
import com.example.cs4520_final_project.ui.common.paymentOptions
import com.example.cs4520_final_project.ui.view_listing.CampusSwapViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchListingsScreen(
    listings: List<ListingInfo>, onListingClick: (Long) -> Unit, vm: CampusSwapViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedPayment by remember { mutableStateOf<String?>(null) }

    var selectedCondition by remember { mutableStateOf<String?>(null) }
    var showFilters by remember { mutableStateOf(false) }

    val displayedListings = remember(listings, searchQuery, selectedPayment, selectedCondition) {
        val q = searchQuery.trim().lowercase()
        listings.filter {
            q.isBlank() || it.title.lowercase().contains(q) || it.description.lowercase()
                .contains(q)
        }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
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
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { showFilters = !showFilters }) {
                    Icon(
                        imageVector = if (showFilters) Icons.Default.FilterListOff else Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = if (showFilters || selectedPayment != null || selectedCondition != null) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }
            if (showFilters) {
                DropdownOption(
                    label = "Payment",
                    options = paymentOptions,
                    selected = selectedPayment,
                    onSelectionChange = {
                        selectedPayment = it
                        vm.setPaymentFilter(it)
                    },
                    allLabel = "All payment types",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp)
                )
                DropdownOption(
                    label = "Condition",
                    options = conditionOptions,
                    selected = selectedCondition,
                    onSelectionChange = {
                        selectedCondition = it
                        vm.setConditionFilter(it)
                    },
                    allLabel = "All Conditions",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp)
                )
            }

            if (displayedListings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (searchQuery.isBlank() && selectedPayment == null) "No listings yet. Tap + to create your first post."
                        else "No listings match your filters."
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