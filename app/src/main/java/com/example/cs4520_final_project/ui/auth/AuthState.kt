package com.example.cs4520_final_project.ui.auth

sealed class AuthState {
    data object Loading : AuthState()
    data object SignedOut : AuthState()
    data class SignedIn(val uid: String, val email: String?) : AuthState()
}