package com.example.cs4520_final_project.data

// Stores the profile information entered during account setup.
// Firebase needs default values so it can rebuild the object from Firestore.
data class ProfileInfo(
    val fullName: String = "",
    val schoolEmail: String = "",
    val campusArea: String = "",
    val photoUri: String? = null
)

// Stores one listing created by a user.
// sellerId connects the listing to the Firebase Auth user who created it.
data class ListingInfo(
    val id: Long = System.currentTimeMillis(),
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val payment: String = "",
    val location: String = "",
    val condition: String = "",
    val photoUri: String? = null,
    val sellerId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val status: String = "available"
)

// Stores one public comment under a listing.
// This supports basic multi-user interaction for each marketplace post.
data class CommentInfo(
    val id: Long = System.currentTimeMillis(),
    val listingId: Long = 0L,
    val commenterId: String = "",
    val commenterName: String = "",
    val text: String = "",
    val createdAt: Long = System.currentTimeMillis()
)