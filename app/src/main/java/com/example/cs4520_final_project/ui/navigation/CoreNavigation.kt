package com.example.cs4520_final_project.ui.navigation


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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.cs4520_final_project.ui.activities_page.ActivitiesPageScreen
import com.example.cs4520_final_project.ui.chat.ChatScreen
import com.example.cs4520_final_project.ui.create_listing.CreateListingScreen
import com.example.cs4520_final_project.ui.create_profile.CreateProfileScreen
import com.example.cs4520_final_project.ui.listings_list.ListingsListScreen
import com.example.cs4520_final_project.ui.view_listing.ViewListingScreen
import androidx.navigation.NavDestination.Companion.hasRoute

@Composable
fun CoreNavigation(
    isLoggedIn: Boolean
) {
    val navController = rememberNavController()
    val startDestination = if (isLoggedIn) ListingsList else CreateProfile
    val currentDestination by navController.currentBackStackEntryAsState()
    val showBottomBar = currentDestination?.destination?.hasRoute<CreateProfile>() == false && currentDestination?.destination?.hasRoute<CreateListing>() == false
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                Surface(
                    shadowElevation = 16.dp, color = Color.White
                ) {
                    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                        NavigationBarItem(icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(32.dp)
                            )
                        }, selected = true, onClick = { navController.navigate(ListingsList) })
                        NavigationBarItem(icon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier.size(32.dp)
                            )
                        }, selected = false, onClick = { navController.navigate(ListingsList) })
                        NavigationBarItem(icon = {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                modifier = Modifier.size(32.dp)
                            )
                        }, selected = false, onClick = { navController.navigate(CreateListing) })
                        NavigationBarItem(icon = {
                            BadgedBox(badge = { Badge { Text("5") } }) {
                                Icon(
                                    Icons.Default.Notifications,
                                    contentDescription = "Activities",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }, selected = false, onClick = { navController.navigate(ActivitiesPage) })
                        NavigationBarItem(icon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(32.dp)
                            )
                        }, selected = false, onClick = { })
                    }
                }
            }
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<ListingsList> {
                ListingsListScreen({})
            }
            composable<CreateProfile> {
                CreateProfileScreen({ navController.navigate(ListingsList) })
            }
            composable<ActivitiesPage> {
                ActivitiesPageScreen()
            }
            composable<Chat> {
                ChatScreen()
            }
            composable<CreateListing> {
                CreateListingScreen(onBack = { navController.popBackStack() }, {})
            }
            composable<ViewListing> {
                ViewListingScreen()
            }
        }
    }
}
