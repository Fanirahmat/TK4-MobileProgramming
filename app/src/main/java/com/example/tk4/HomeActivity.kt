package com.example.tk4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var textName: TextView
    lateinit var textEmail: TextView

    lateinit var btnLogout: Button

    private lateinit var vMap: GoogleMap

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
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        vMap = googleMap

        val city = LatLng(13.7563, 100.5018)
        vMap.addMarker(MarkerOptions()
            .position(city)
            .title("Marker in Bangkok"))
        vMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 5f))
    }
}