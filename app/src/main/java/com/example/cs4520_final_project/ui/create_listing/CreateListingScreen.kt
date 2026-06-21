package com.example.cs4520_final_project.ui.create_listing

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.cs4520_final_project.ui.common.DropdownOption
import com.example.cs4520_final_project.ui.common.ListingImage
import com.example.cs4520_final_project.ui.common.conditionOptions
import com.example.cs4520_final_project.ui.common.paymentOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListingScreen(
    onBack: () -> Unit, onCreateListing: (ListingInfo) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var payment by remember { mutableStateOf("") }

    var location by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }
    var listingPhotoUri by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    var showUrlDialog by remember { mutableStateOf(false) }
    var urlInput by remember { mutableStateOf("") }

    if (showUrlDialog) {
        AlertDialog(
            onDismissRequest = { showUrlDialog = false },
            title = { Text("Add Photo URL") },
            text = {
                OutlinedTextField(
                    value = urlInput,
                    onValueChange = { urlInput = it },
                    placeholder = { Text("https://...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    listingPhotoUri = urlInput.trim().ifBlank { null }
                    showUrlDialog = false
                    urlInput = ""
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showUrlDialog = false
                    urlInput = ""
                }) {
                    Text("Cancel")
                }
            })
    }

    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text("Missing Information") },
            text = { Text(errorMessage ?: "") },
            confirmButton = {
                TextButton(onClick = { errorMessage = null }) {
                    Text("OK")
                }
            })
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create Listing", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ListingTextField(
                value = title, onValueChange = { title = it }, placeholder = "Title"
            )

            ListingTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = "Description"
            )

            ListingTextField(
                value = price,
                onValueChange = { price = it },
                placeholder = "Price",
                keyboardType = KeyboardType.Number
            )
            ListingTextField(
                value = location, onValueChange = { location = it }, placeholder = "Location"
            )
            HorizontalDivider(Modifier, thickness = 0.5.dp, color = Color.LightGray)

            DropdownOption("Select Payment", paymentOptions, payment, { option ->
                if (option != null) {
                    payment = option
                }
            })
            DropdownOption("Select Condition", conditionOptions, condition, { option ->
                if (option != null) {
                    condition = option
                }
            })
            HorizontalDivider(Modifier, thickness = 0.5.dp, color = Color.LightGray)





            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .then(
                        if (listingPhotoUri != null) Modifier.fillMaxWidth().heightIn(max = 250.dp)
                        else Modifier.size(160.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .clickable { showUrlDialog = true }, contentAlignment = Alignment.Center
            ) {
                if (listingPhotoUri == null) {
                    Text("Add Photo", color = Color.DarkGray)
                } else {
                    ListingImage(listingPhotoUri, "")

                }
            }

            Spacer(modifier = Modifier.height(88.dp))

            Button(
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
                        onCreateListing(
                            ListingInfo(
                                title = title,
                                description = description,
                                price = price,
                                payment = payment,
                                location = location,
                                condition = condition,
                                photoUri = listingPhotoUri?.toString()
                            )
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    "Create Listing",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ListingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedIndicatorColor = Color.LightGray,
            focusedIndicatorColor = Color.LightGray
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
