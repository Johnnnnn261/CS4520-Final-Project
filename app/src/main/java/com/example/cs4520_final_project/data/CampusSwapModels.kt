package com.example.cs4520_final_project.data

// Stores the profile information entered during account setup.
data class ProfileInfo(
    val fullName: String,
    val schoolEmail: String,
    val campusArea: String,
    val photoUri: String? = null
)

// Stores one listing created by the current user.
data class ListingInfo(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val description: String,
    val price: String,
    val payment: String,
    val location: String,
    val condition: String,
    val photoUri: String? = null
)
