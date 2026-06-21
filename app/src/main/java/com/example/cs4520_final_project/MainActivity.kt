package com.example.cs4520_final_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs4520_final_project.ui.auth.AuthScreen
import com.example.cs4520_final_project.ui.auth.AuthState
import com.example.cs4520_final_project.ui.auth.AuthViewModel
import com.example.cs4520_final_project.ui.common.CircleLoader
import com.example.cs4520_final_project.ui.create_profile.CreateProfileScreen
import com.example.cs4520_final_project.ui.navigation.CoreNavigation
import com.example.cs4520_final_project.ui.theme.CS4520FinalProjectTheme
import com.example.cs4520_final_project.ui.view_listing.CampusSwapViewModel

class MainActivity : ComponentActivity() {
        private val campusSwapVm: CampusSwapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CS4520FinalProjectTheme {
                val authVm: AuthViewModel = viewModel()
                val state by authVm.authState.collectAsStateWithLifecycle()
                when (state) {
                    AuthState.Loading -> CircleLoader()
                    AuthState.SignedOut -> {
                        AuthScreen()
                    }

                    is AuthState.SignedIn -> CoreNavigation((state as AuthState.SignedIn).uid, campusSwapVm)
                }
            }
        }
    }
}


