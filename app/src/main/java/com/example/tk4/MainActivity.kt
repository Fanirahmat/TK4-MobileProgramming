package com.example.tk4

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnSignIn: Button
    lateinit var btnSignInGoogle: Button
    lateinit var textSignUp: TextView
    lateinit var progressDialog: ProgressDialog

    lateinit var googleSignInClien: GoogleSignInClient

    var firebaseAuth = FirebaseAuth.getInstance()

    companion object {
        private const val RC_SIGN_IN = 1001
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEmail = findViewById(R.id.etEmailLogin)
        etPassword = findViewById(R.id.etPasswordLogin)
        btnSignIn = findViewById(R.id.btnSignIn)
        textSignUp = findViewById(R.id.textSignUp)
        btnSignInGoogle = findViewById(R.id.btnSignInGoogle)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Please wait....")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClien = GoogleSignIn.getClient(this, gso)

        btnSignInGoogle.setOnClickListener {
            val signInIntent = googleSignInClien.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        textSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        btnSignIn.setOnClickListener {
            if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()) {
                processLogin()
            } else {
                Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processLogin() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    // Sign in berhasil, masuk ke aplikasi (opsional)
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    // Sign in gagal, tampilkan pesan kesalahan (opsional)
                    Toast.makeText(this, "gagal login", Toast.LENGTH_SHORT).show()
                    Log.e("FirebaseAuth", "signInWithCredential:failure ${task.exception}")
                }
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {

                Log.e("FirebaseAuth", "signInWithCredential:failure ${e}")
                Toast.makeText(this, "testt: " + e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        progressDialog.show()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    // Sign in berhasil, masuk ke aplikasi (opsional)
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    // Sign in gagal, tampilkan pesan kesalahan (opsional)
                    Toast.makeText(this, "gagal login", Toast.LENGTH_SHORT).show()
                    Log.e("FirebaseAuth", "signInWithCredential:failure ${task.exception}")
                }
            }
    }
}