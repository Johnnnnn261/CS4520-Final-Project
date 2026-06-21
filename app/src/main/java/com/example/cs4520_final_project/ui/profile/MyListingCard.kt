package com.example.cs4520_final_project.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cs4520_final_project.data.ListingInfo

@Composable
fun MyListingCard(
    listing: ListingInfo, onEditListing: () -> Unit
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
                    Text(
                        "Listed for $${listing.price}", style = MaterialTheme.typography.titleMedium
                    )
                }

                Text("Available", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("${listing.location} • ${listing.condition}", color = Color.Gray)
            Text("Payment: ${listing.payment}", color = Color.Gray)
            Text("0 views • 0 interested", color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onEditListing, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Listing")
            }
        }
    }
}