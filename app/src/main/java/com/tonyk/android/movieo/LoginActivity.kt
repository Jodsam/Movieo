package com.tonyk.android.movieo

import AuthenticationViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signInButton: Button = findViewById(R.id.btnSignIn)
        signInButton.setOnClickListener {
            authenticateWithGoogle()
        }

        lifecycleScope.launch {
            authenticationViewModel.authenticationState.collect { state ->
                when (state) {
                    is AuthenticationViewModel.AuthenticationState.Initial -> {
                        // Check if the user is already authenticated
                        val firebaseUser = FirebaseAuth.getInstance().currentUser
                        if (firebaseUser != null) {
                            // User is authenticated, navigate to the main activity
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    is AuthenticationViewModel.AuthenticationState.Authenticated -> {
                        // User has been authenticated, navigate to the main activity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is AuthenticationViewModel.AuthenticationState.Error -> {
                        val errorMessage = state.message
                        // Handle error case, show error message to the user
                    }
                    // Handle other authentication states if needed
                    else -> {}
                }
            }
        }
    }

    private fun authenticateWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    GlobalScope.launch(Dispatchers.Main) {
                        authenticationViewModel.authenticateWithGoogle(idToken)
                    }
                } else {
                    // Handle error case, ID token is null
                }
            } catch (e: ApiException) {
                // Handle error case, Google sign-in failed
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}
