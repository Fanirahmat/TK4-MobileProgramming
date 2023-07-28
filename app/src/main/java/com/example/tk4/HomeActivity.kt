package com.example.tk4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    lateinit var textName: TextView
    lateinit var textEmail: TextView

    lateinit var btnLogout: Button

    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        textName = findViewById(R.id.textName)
        textEmail = findViewById(R.id.textEmail)
        btnLogout = findViewById(R.id.btnLogout)

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            textEmail.text = currentUser.email
            textName.text = currentUser.displayName
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}