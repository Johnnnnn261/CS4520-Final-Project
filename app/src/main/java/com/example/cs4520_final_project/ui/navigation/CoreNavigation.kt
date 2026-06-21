package com.example.cs4520_final_project.ui.navigation

import kotlinx.coroutines.withTimeoutOrNull
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLayoutDirection
import com.example.cs4520_final_project.data.CommentInfo
import com.example.cs4520_final_project.ui.common.CircleLoader
import com.example.cs4520_final_project.ui.search_listings.SearchListingsScreen
import com.example.cs4520_final_project.ui.view_listing.CampusSwapViewModel

@Composable
fun CoreNavigation(currentUserId: String, campusSwapVm: CampusSwapViewModel) {
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()
    val profileInfo by campusSwapVm.profileInfo.collectAsState()
    val listings by campusSwapVm.listings.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val isLoading = profileInfo == null && listings.isEmpty()

    if (isLoading) {
        CircleLoader()
        return
    }
    LaunchedEffect(currentDestination) {
        if (currentDestination?.destination?.hasRoute<SearchListings>() == false) {
            campusSwapVm.clearFilters()
        }
    }
    val startDestination = if (profileInfo != null) ListingsList else CreateProfile

    val showBottomBar =
        currentDestination?.destination?.hasRoute<CreateProfile>() == false && currentDestination?.destination?.hasRoute<CreateListing>() == false

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),  // add this

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
                            onClick = { navController.navigate(ListingsList) })

                        NavigationBarItem(icon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier.size(32.dp)
                            )
                        }, selected = false, onClick = { navController.navigate(SearchListings) })

                        NavigationBarItem(icon = {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                modifier = Modifier.size(32.dp)
                            )
                        }, selected = false, onClick = { navController.navigate(CreateListing) })

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
                            onClick = { navController.navigate(ActivitiesPage) })

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Profile",
                                    modifier = Modifier.size(32.dp)
                                )
                            },
                            selected = currentDestination?.destination?.hasRoute<Profile>() == true,
                            onClick = { navController.navigate(Profile) })
                    }
                }
            }
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())  // don't want padding on top

        ) {
            composable<ListingsList> {
                ListingsListScreen(
                    listings = listings, onListingClick = { listingId ->
                        navController.navigate(ViewListing(listingId.toString()))
                    })
            }

            composable<CreateProfile> {
                CreateProfileScreen { newProfile ->
                    coroutineScope.launch {
                        campusSwapVm.updateProfile(newProfile)
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
                            campusSwapVm.addListing(newListing)
                            navController.navigate(ListingsList) {
                                popUpTo(CreateListing) { inclusive = true }
                            }
                        }
                    })
            }

            composable<Profile> {
                ProfileScreen(
                    profileInfo = profileInfo,
                    listings = listings.filter { it.sellerId == currentUserId },
                    onUpdateProfile = { updatedProfile ->
                        campusSwapVm.updateProfile(updatedProfile)
                    },
                    onSignOut = {
                        campusSwapVm.signOut()
                        navController.navigate(CreateProfile)
                    },
                    onUpdateListing = { updatedListing ->
                        campusSwapVm.updateListing(updatedListing)
                    },
                    onDeleteListing = { listingId ->
                        campusSwapVm.deleteListing(listingId)
                    })
            }

            composable<ViewListing> { backStackEntry ->
                val route = backStackEntry.arguments?.getString("listingId")
                val selectedListing = listings.firstOrNull { it.id.toString() == route }
                val comments by campusSwapVm.observeComments(selectedListing?.id ?: 0L)
                    .collectAsState(initial = emptyList())

                ViewListingScreen(
                    listing = selectedListing,
                    comments = comments,
                    onBack = { navController.popBackStack() },
                    onMessageSeller = { navController.navigate(Chat("seller")) },
                    onAddComment = { commentText ->
                        val listing = selectedListing
                        if (listing != null) {
                            campusSwapVm.saveComment(
                                CommentInfo(
                                listingId = listing.id,
                                commenterId = currentUserId.ifBlank { "local-user" },
                                commenterName = profileInfo?.fullName?.ifBlank { "CampusSwap User" }
                                    ?: "CampusSwap User",
                                text = commentText))
                        }
                    })
            }


            composable<SearchListings> {
                SearchListingsScreen(
                    listings = listings, onListingClick = { listingId ->
                        navController.navigate(ViewListing(listingId.toString()))
                    }, campusSwapVm
                )
            }
        }
    }
}
