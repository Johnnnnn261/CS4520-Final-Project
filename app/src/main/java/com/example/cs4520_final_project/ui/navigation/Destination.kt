package com.example.cs4520_final_project.ui.navigation

import kotlinx.serialization.Serializable


// page where user can view recent messages about their listings
@Serializable object ActivitiesPage

// page where user can chat with another user about their listing
@Serializable data class Chat(val userId: String)

// Page where user can create a listing
@Serializable object CreateListing

// page where user can create their profile
@Serializable object CreateProfile

// Page where user can view all the listings
@Serializable object ListingsList

// Page where user can view a specific listing including their own
@Serializable data class ViewListing(val listingId: String)






