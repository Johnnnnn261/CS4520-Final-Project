package com.example.cs4520_final_project.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cs4520_final_project.data.ListingInfo
import com.example.cs4520_final_project.data.ProfileInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileInfo: ProfileInfo?,
    listings: List<ListingInfo>,
    onUpdateProfile: (ProfileInfo) -> Unit,
    onUpdateListing: (ListingInfo) -> Unit,
    onSignOut: () -> Unit,
    onDeleteListing: (listingId: Long) -> Unit,
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var listingBeingEdited by remember { mutableStateOf<ListingInfo?>(null) }

    if (showEditProfileDialog && profileInfo != null) {
        EditProfileDialog(
            profileInfo = profileInfo,
            onDismiss = { showEditProfileDialog = false },
            onSave = { updatedProfile ->
                onUpdateProfile(updatedProfile)
                showEditProfileDialog = false
            })
    }

    if (listingBeingEdited != null) {
        EditListingDialog(
            listing = listingBeingEdited!!,
            onDismiss = { listingBeingEdited = null },
            onSave = { updatedListing ->
                onUpdateListing(updatedListing)
                listingBeingEdited = null
            })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ), actions = {
                    IconButton(onClick = onSignOut) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Sign Out")
                    }
                })
        }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileInfoCard(
                    profileInfo = profileInfo, onEditProfile = {
                        if (profileInfo != null) {
                            showEditProfileDialog = true
                        }
                    })
            }

            item {
                Text(
                    "My Listings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            if (listings.isEmpty()) {
                item {
                    Text("You have not created any listings yet.", color = Color.Gray)
                }
            } else {
                items(listings) { listing ->
                    MyListingCard(listing = listing, onEditListing = {
                        listingBeingEdited = listing
                    }, onDeleteListing)
                }
            }
        }
    }
}
