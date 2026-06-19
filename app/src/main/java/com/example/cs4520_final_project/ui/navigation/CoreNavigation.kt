package com.example.cs4520_final_project.ui.navigation

import kotlinx.coroutines.withTimeoutOrNull
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.example.cs4520_final_project.data.CampusSwapRepository
import kotlinx.coroutines.launch
import com.example.cs4520_final_project.data.ListingInfo
import com.example.cs4520_final_project.data.ProfileInfo
import com.example.cs4520_final_project.ui.activities_page.ActivitiesPageScreen
import com.example.cs4520_final_project.ui.chat.ChatScreen
import com.example.cs4520_final_project.ui.create_listing.CreateListingScreen
import com.example.cs4520_final_project.ui.create_profile.CreateProfileScreen
import com.example.cs4520_final_project.ui.listings_list.ListingsListScreen
import com.example.cs4520_final_project.ui.profile.ProfileScreen
import com.example.cs4520_final_project.ui.view_listing.ViewListingScreen
import androidx.compose.runtime.mutableStateMapOf
import com.example.cs4520_final_project.data.CommentInfo
@Composable
fun CoreNavigation(isLoggedIn: Boolean) {
    val navController = rememberNavController()
    val startDestination = if (isLoggedIn) ListingsList else CreateProfile
    val currentDestination by navController.currentBackStackEntryAsState()
    val commentsByListing = remember { mutableStateMapOf<Long, List<CommentInfo>>() }

    var profileInfo by remember { mutableStateOf<ProfileInfo?>(null) }
    var currentUserId by remember { mutableStateOf("") }
    val listings = remember { mutableStateListOf<ListingInfo>() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

// Loads Firebase user, profile, and listings when the app starts.
    LaunchedEffect(Unit) {
        currentUserId = CampusSwapRepository.signInAnonymouslyIfNeeded()
        profileInfo = CampusSwapRepository.loadProfile()

        listings.clear()
        listings.addAll(CampusSwapRepository.loadAllListings())

        if (profileInfo != null) {
            navController.navigate(ListingsList) {
                popUpTo(CreateProfile) { inclusive = true }
            }
        }
    }

    val showBottomBar = currentDestination?.destination?.hasRoute<CreateProfile>() == false &&
            currentDestination?.destination?.hasRoute<CreateListing>() == false

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                Surface(shadowElevation = 16.dp, color = Color.White) {
                    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    Icons.Default.Home,
                                    contentDescription = "Home",
                                    modifier = Modifier.size(32.dp)
                                )
                            },
                            selected = currentDestination?.destination?.hasRoute<ListingsList>() == true,
                            onClick = { navController.navigate(ListingsList) }
                        )

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    modifier = Modifier.size(32.dp)
                                )
                            },
                            selected = false,
                            onClick = { navController.navigate(ListingsList) }
                        )

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add",
                                    modifier = Modifier.size(32.dp)
                                )
                            },
                            selected = false,
                            onClick = { navController.navigate(CreateListing) }
                        )

                        NavigationBarItem(
                            icon = {
                                BadgedBox(badge = { Badge { Text("5") } }) {
                                    Icon(
                                        Icons.Default.Notifications,
                                        contentDescription = "Activities",
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            },
                            selected = currentDestination?.destination?.hasRoute<ActivitiesPage>() == true,
                            onClick = { navController.navigate(ActivitiesPage) }
                        )

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Profile",
                                    modifier = Modifier.size(32.dp)
                                )
                            },
                            selected = currentDestination?.destination?.hasRoute<Profile>() == true,
                            onClick = { navController.navigate(Profile) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<ListingsList> {
                ListingsListScreen(
                    listings = listings,
                    onListingClick = { listingId ->
                        navController.navigate(ViewListing(listingId.toString()))
                    }
                )
            }

            composable<CreateProfile> {
                CreateProfileScreen { newProfile ->
                    coroutineScope.launch {
                        Toast.makeText(context, "Saving profile...", Toast.LENGTH_SHORT).show()

                        val firebaseSaved = withTimeoutOrNull(5000) {
                            CampusSwapRepository.saveProfile(newProfile)
                            true
                        } ?: false

                        profileInfo = newProfile

                        if (firebaseSaved) {
                            Toast.makeText(context, "Profile saved to Firebase", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Firebase unavailable. Profile saved locally for now.",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        navController.navigate(ListingsList) {
                            popUpTo(CreateProfile) { inclusive = true }
                        }
                    }
                }
            }

            composable<ActivitiesPage> {
                ActivitiesPageScreen()
            }

            composable<Chat> {
                ChatScreen()
            }

            composable<CreateListing> {
                CreateListingScreen(
                    onBack = { navController.popBackStack() },
                    onCreateListing = { newListing ->
                        coroutineScope.launch {
                            val firebaseSaved = withTimeoutOrNull(5000) {
                                CampusSwapRepository.saveListing(newListing)
                                true
                            } ?: false

                            if (firebaseSaved) {
                                listings.clear()
                                listings.addAll(CampusSwapRepository.loadAllListings())

                                Toast.makeText(
                                    context,
                                    "Listing saved to Firebase",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                listings.add(0, newListing)

                                Toast.makeText(
                                    context,
                                    "Firebase unavailable. Listing saved locally for now.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            navController.navigate(ListingsList) {
                                popUpTo(CreateListing) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable<Profile> {
                ProfileScreen(
                    profileInfo = profileInfo,
                    listings = listings.filter { it.sellerId == currentUserId },
                    onUpdateProfile = { updatedProfile ->
                        coroutineScope.launch {
                            CampusSwapRepository.saveProfile(updatedProfile)
                            profileInfo = updatedProfile
                        }
                    },
                    onUpdateListing = { updatedListing ->
                        coroutineScope.launch {
                            CampusSwapRepository.updateListing(updatedListing)

                            listings.clear()
                            listings.addAll(CampusSwapRepository.loadAllListings())
                        }
                    }
                )
            }

            composable<ViewListing> { backStackEntry ->
                val route = backStackEntry.arguments?.getString("listingId")
                val selectedListing = listings.firstOrNull { it.id.toString() == route }

                LaunchedEffect(selectedListing?.id) {
                    val listingId = selectedListing?.id

                    if (listingId != null) {
                        val loadedComments = withTimeoutOrNull(5000) {
                            CampusSwapRepository.loadCommentsForListing(listingId)
                        }

                        if (loadedComments != null) {
                            commentsByListing[listingId] = loadedComments
                        }
                    }
                }

                ViewListingScreen(
                    listing = selectedListing,
                    comments = selectedListing?.let { commentsByListing[it.id] } ?: emptyList(),
                    onBack = { navController.popBackStack() },
                    onMessageSeller = { navController.navigate(Chat("seller")) },
                    onAddComment = { commentText ->
                        val listing = selectedListing

                        if (listing != null) {
                            coroutineScope.launch {
                                val newComment = CommentInfo(
                                    listingId = listing.id,
                                    commenterId = currentUserId.ifBlank { "local-user" },
                                    commenterName = profileInfo?.fullName?.ifBlank { "CampusSwap User" } ?: "CampusSwap User",
                                    text = commentText
                                )

                                val firebaseSaved = withTimeoutOrNull(5000) {
                                    CampusSwapRepository.saveComment(newComment)
                                    true
                                } ?: false

                                val oldComments = commentsByListing[listing.id] ?: emptyList()
                                commentsByListing[listing.id] = oldComments + newComment

                                if (firebaseSaved) {
                                    Toast.makeText(context, "Comment saved to Firebase", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Firebase unavailable. Comment saved locally for now.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
