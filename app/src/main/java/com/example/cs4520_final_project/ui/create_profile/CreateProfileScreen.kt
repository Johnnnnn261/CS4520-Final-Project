package com.example.cs4520_final_project.ui.create_profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.cs4520_final_project.data.ProfileInfo

@Composable
fun CreateProfileScreen(onCreateAccount: (ProfileInfo) -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var schoolEmail by remember { mutableStateOf("") }
    var campusArea by remember { mutableStateOf("") }
    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        profilePhotoUri = uri
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
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 64.dp, horizontal = 32.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CampusSwap",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Create Profile",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ProfileTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Full Name"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileTextField(
                value = schoolEmail,
                onValueChange = { schoolEmail = it },
                label = "School Email",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileTextField(
                value = campusArea,
                onValueChange = { campusArea = it },
                label = "Campus Area"
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Add Profile Photo", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { photoPickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profilePhotoUri == null) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Profile Photo",
                        modifier = Modifier.size(56.dp)
                    )
                } else {
                    Text("Photo\nSelected", color = Color.DarkGray)
                }
            }
        }

        Button(
            onClick = {
                errorMessage = when {
                    fullName.isBlank() -> "Full Name cannot be empty"
                    schoolEmail.isBlank() -> "School Email cannot be empty"
                    campusArea.isBlank() -> "Campus Area cannot be empty"
                    else -> null
                }

                if (errorMessage == null) {
                    onCreateAccount(
                        ProfileInfo(
                            fullName = fullName,
                            schoolEmail = schoolEmail,
                            campusArea = campusArea,
                            photoUri = profilePhotoUri?.toString()
                        )
                    )
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Create Account", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontWeight = FontWeight.Bold) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        ),
        textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.fillMaxWidth()
    )
}
