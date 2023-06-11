package com.tonyk.android.movieo

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthenticationViewModel : ViewModel() {
    private val _authenticationState: MutableStateFlow<AuthenticationState> =
        MutableStateFlow(AuthenticationState.Initial)
    val authenticationState: StateFlow<AuthenticationState> = _authenticationState

    fun authenticateWithGoogle(idToken: String) {
        _authenticationState.value = AuthenticationState.Loading

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            // User is already authenticated, update the authentication state
            _authenticationState.value = AuthenticationState.Authenticated
        } else {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnSuccessListener {
                    _authenticationState.value = AuthenticationState.Authenticated
                }
                .addOnFailureListener { exception ->
                    _authenticationState.value = AuthenticationState.Error(exception.message)
                }
        }
    }

    sealed class AuthenticationState {
        object Initial : AuthenticationState()
        object Loading : AuthenticationState()
        object Authenticated : AuthenticationState()
        data class Error(val message: String?) : AuthenticationState()
    }
}
