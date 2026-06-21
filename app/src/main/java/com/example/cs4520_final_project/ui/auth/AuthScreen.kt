package com.example.cs4520_final_project.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthScreen(
    vm: AuthViewModel = viewModel(),
) {
    var email by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf(AuthMode.SignIn) }
    val error by vm.error.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "CampusSwap",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = when (mode) {
                    AuthMode.SignIn -> "Sign in to view listings"
                    AuthMode.SignUp -> "Create a CampusSwap account"
                },
                style = MaterialTheme.typography.bodyLarge,
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; vm.clearError() },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = pw,
                onValueChange = { pw = it; vm.clearError() },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
            )

            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Button(
                onClick = {
                    when (mode) {
                        AuthMode.SignIn -> vm.signIn(email, pw)
                        AuthMode.SignUp -> vm.signUp(email, pw)
                    }
                },
                enabled = email.isNotBlank() && pw.length >= 6,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (mode == AuthMode.SignIn) "Sign in" else "Sign up")
            }

            TextButton(onClick = {
                mode = if (mode == AuthMode.SignIn) AuthMode.SignUp else AuthMode.SignIn
            }) {
                Text(
                    if (mode == AuthMode.SignIn) "Need an account? Sign up"
                    else "Have an account? Sign in"
                )
            }
        }
    }
}

private enum class AuthMode { SignIn, SignUp }