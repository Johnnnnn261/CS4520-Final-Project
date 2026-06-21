package com.example.cs4520_final_project.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cs4520_final_project.data.ListingInfo
import com.example.cs4520_final_project.ui.common.DropdownOption
import com.example.cs4520_final_project.ui.common.conditionOptions
import com.example.cs4520_final_project.ui.common.paymentOptions

@Composable
fun EditListingDialog(
    listing: ListingInfo, onDismiss: () -> Unit, onSave: (ListingInfo) -> Unit
) {
    var title by remember { mutableStateOf(listing.title) }
    var description by remember { mutableStateOf(listing.description) }
    var price by remember { mutableStateOf(listing.price) }
    var payment by remember { mutableStateOf(listing.payment) }
    var location by remember { mutableStateOf(listing.location) }
    var photoUri by remember { mutableStateOf(listing.photoUri) }
    var condition by remember { mutableStateOf(listing.condition) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Edit Listing") }, text = {
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
                value = photoUri ?: "",
                onValueChange = { photoUri = it },
                label = { Text("Photo Url") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

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

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                singleLine = true
            )
        }
    }, confirmButton = {
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
                            condition = condition,
                            photoUri = photoUri
                        )
                    )
                }
            }) {
            Text("Save")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}