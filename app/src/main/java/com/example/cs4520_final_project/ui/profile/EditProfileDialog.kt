package com.example.cs4520_final_project.ui.profile

import android.R.attr.label
import android.R.attr.singleLine
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
import com.example.cs4520_final_project.data.ProfileInfo

@Composable
fun EditProfileDialog(
    profileInfo: ProfileInfo, onDismiss: () -> Unit, onSave: (ProfileInfo) -> Unit
) {
    var fullName by remember { mutableStateOf(profileInfo.fullName) }
    var schoolEmail by remember { mutableStateOf(profileInfo.schoolEmail) }
    var campusArea by remember { mutableStateOf(profileInfo.campusArea) }
    var photoUri by remember { mutableStateOf(profileInfo.photoUri) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Edit Profile") }, text = {
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
                value = photoUri ?: "",
                onValueChange = { photoUri = it },
                label = { Text("Photo Url") },
                singleLine = true
            )
            OutlinedTextField(
                value = campusArea,
                onValueChange = { campusArea = it },
                label = { Text("Campus Area") },
                singleLine = true
            )
        }
    }, confirmButton = {
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
                            campusArea = campusArea,
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