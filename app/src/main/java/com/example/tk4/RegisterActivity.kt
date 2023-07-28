package com.example.tk4

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnSignUp: Button
    lateinit var textSignIn: TextView

    lateinit var progressDialog: ProgressDialog

    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etNameRegister)
        etEmail = findViewById(R.id.etEmailRegister)
        etPassword = findViewById(R.id.etPasswordRegister)
        btnSignUp = findViewById(R.id.btnSignUp)
        textSignIn = findViewById(R.id.textSignIn)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Register")
        progressDialog.setMessage("Please wait....")

        textSignIn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnSignUp.setOnClickListener {
            if (etName.text.isNotEmpty() && etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()) {
                processRegister()
            } else {
                Toast.makeText(this, "Lengkapi semua data", LENGTH_SHORT).show()
            }
        }

    }

    private fun processRegister() {
        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userUpdateProfile = userProfileChangeRequest {
                    displayName = name
                }
                val user = task.result.user
                user?.updateProfile(userUpdateProfile)?.addOnCompleteListener {
                    progressDialog.dismiss()
                    startActivity(Intent(this, HomeActivity::class.java))
                }?.addOnFailureListener { error ->
                    Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
                }
            } else {
                progressDialog.dismiss()
            }
        }.addOnFailureListener { error ->
            Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
        }
    }
}