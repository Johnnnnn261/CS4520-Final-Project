package com.example.cs4520_final_project.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DropdownOption(
    label: String,
    options: List<String>,
    selected: String?,
    onSelectionChange: (String?) -> Unit,
    allLabel: String? = null,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = true }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = selected ?: allLabel ?: "Select $label",
                    color = if (selected == null) Color.Gray else Color.Black
                )
                Icon(
                    Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray
                )
            }
        }

        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            if (allLabel != null) {
                DropdownMenuItem(text = { Text(allLabel) }, onClick = {
                    onSelectionChange(null)
                    expanded = false
                })
            }
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onSelectionChange(option)
                    expanded = false
                })
            }
        }
    }
}