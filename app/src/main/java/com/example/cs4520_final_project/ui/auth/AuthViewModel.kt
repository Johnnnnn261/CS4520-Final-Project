package com.example.cs4520_final_project.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs4520_final_project.data.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    // The signed-in/out state, observed by MainActivity to choose the app shell.
    val authState: StateFlow<AuthState> = repo.observeAuthState().map { user ->
            if (user == null) AuthState.SignedOut
            else AuthState.SignedIn(user.uid, user.email)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AuthState.Loading
        )

    // Surface the last sign-in/sign-up failure as a user-facing message so AuthScreen
    // can render it inline below the password field. Reset on the next keystroke.
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun clearError() {
        _error.value = null
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            repo.signIn(email, password).onFailure { _error.value = it.message }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            repo.signUp(email, password).onFailure { _error.value = it.message }
        }
    }

    fun signOut() = repo.signOut()
}