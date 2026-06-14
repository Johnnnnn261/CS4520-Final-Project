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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
    onUpdateListing: (ListingInfo) -> Unit
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
            }
        )
    }

    if (listingBeingEdited != null) {
        EditListingDialog(
            listing = listingBeingEdited!!,
            onDismiss = { listingBeingEdited = null },
            onSave = { updatedListing ->
                onUpdateListing(updatedListing)
                listingBeingEdited = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileInfoCard(
                    profileInfo = profileInfo,
                    onEditProfile = {
                        if (profileInfo != null) {
                            showEditProfileDialog = true
                        }
                    }
                )
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
                    MyListingCard(
                        listing = listing,
                        onEditListing = {
                            listingBeingEdited = listing
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoCard(
    profileInfo: ProfileInfo?,
    onEditProfile: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (profileInfo?.photoUri == null) "No\nPhoto" else "Photo")
                }

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(profileInfo?.fullName ?: "No profile name", fontWeight = FontWeight.Bold)
                    Text(profileInfo?.schoolEmail ?: "No school email", color = Color.Gray)
                    Text(profileInfo?.campusArea ?: "No campus area", color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onEditProfile,
                enabled = profileInfo != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Profile")
            }
        }
    }
}

@Composable
private fun MyListingCard(
    listing: ListingInfo,
    onEditListing: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(listing.title, fontWeight = FontWeight.Bold)
                    Text("Listed for $${listing.price}", style = MaterialTheme.typography.titleMedium)
                }

                Text("Available", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("${listing.location} • ${listing.condition}", color = Color.Gray)
            Text("Payment: ${listing.payment}", color = Color.Gray)
            Text("0 views • 0 interested", color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onEditListing,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Listing")
            }
        }
    }
}

@Composable
private fun EditProfileDialog(
    profileInfo: ProfileInfo,
    onDismiss: () -> Unit,
    onSave: (ProfileInfo) -> Unit
) {
    var fullName by remember { mutableStateOf(profileInfo.fullName) }
    var schoolEmail by remember { mutableStateOf(profileInfo.schoolEmail) }
    var campusArea by remember { mutableStateOf(profileInfo.campusArea) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (errorMessage != null) {
                    Text(errorMessage ?: "", color = Color.Red)
                }

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = schoolEmail,
                    onValueChange = { schoolEmail = it },
                    label = { Text("School Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                OutlinedTextField(
                    value = campusArea,
                    onValueChange = { campusArea = it },
                    label = { Text("Campus Area") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    errorMessage = when {
                        fullName.isBlank() -> "Full Name cannot be empty"
                        schoolEmail.isBlank() -> "School Email cannot be empty"
                        campusArea.isBlank() -> "Campus Area cannot be empty"
                        else -> null
                    }

                    if (errorMessage == null) {
                        onSave(
                            profileInfo.copy(
                                fullName = fullName,
                                schoolEmail = schoolEmail,
                                campusArea = campusArea
                            )
                        )
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun EditListingDialog(
    listing: ListingInfo,
    onDismiss: () -> Unit,
    onSave: (ListingInfo) -> Unit
) {
    var title by remember { mutableStateOf(listing.title) }
    var description by remember { mutableStateOf(listing.description) }
    var price by remember { mutableStateOf(listing.price) }
    var payment by remember { mutableStateOf(listing.payment) }
    var location by remember { mutableStateOf(listing.location) }
    var condition by remember { mutableStateOf(listing.condition) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Listing") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (errorMessage != null) {
                    Text(errorMessage ?: "", color = Color.Red)
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = payment,
                    onValueChange = { payment = it },
                    label = { Text("Payment") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = condition,
                    onValueChange = { condition = it },
                    label = { Text("Condition") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    errorMessage = when {
                        title.isBlank() -> "Title cannot be empty"
                        description.isBlank() -> "Description cannot be empty"
                        price.isBlank() -> "Price cannot be empty"
                        payment.isBlank() -> "Payment cannot be empty"
                        location.isBlank() -> "Location cannot be empty"
                        condition.isBlank() -> "Condition cannot be empty"
                        else -> null
                    }

                    if (errorMessage == null) {
                        onSave(
                            listing.copy(
                                title = title,
                                description = description,
                                price = price,
                                payment = payment,
                                location = location,
                                condition = condition
                            )
                        )
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}