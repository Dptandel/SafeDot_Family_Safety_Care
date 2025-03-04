package com.app.safedot_familysafetycare

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.safedot_familysafetycare.databinding.ActivityMainBinding
import com.app.safedot_familysafetycare.fragments.SecurityFragment
import com.app.safedot_familysafetycare.fragments.HomeFragment
import com.app.safedot_familysafetycare.fragments.MapsFragment
import com.app.safedot_familysafetycare.fragments.ProfileFragment
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_CONTACTS
    )

    private val permissionCode = 78

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isAllPermissionsGranted()) {
            performInitialSetup()
        } else {
            askForPermission()
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    inflateFragment(HomeFragment())
                }

                R.id.dashboard -> {
                    inflateFragment(MapsFragment())
                }

                R.id.security -> {
                    inflateFragment(SecurityFragment())
                }

                R.id.profile -> {
                    inflateFragment(ProfileFragment())
                }
            }
            true
        }

        binding.bottomNavigation.selectedItemId = R.id.home

        val currentUser = FirebaseAuth.getInstance().currentUser
        val name = currentUser?.displayName.toString()
        val email = currentUser?.email.toString()
        val phoneNumber = currentUser?.phoneNumber.toString()
        val imageUrl = currentUser?.photoUrl.toString()

        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "imageUrl" to imageUrl
        )

        val db = Firebase.firestore

        db.collection("users")
            .document(email)
            .set(user)
            .addOnSuccessListener {
                Log.d("TAG", "onCreate: User Added Successfully!!!")
            }
            .addOnFailureListener {
                Log.d("TAG", "onCreate: User Not Added!!!")
            }
    }

    private fun performInitialSetup() {
        if (isLocationEnabled(this)) {
            setUpLocationListener()
        } else {
            showGPSNotEnabledDialog(this)
        }
    }

    @SuppressLint("VisibleForTests")
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        Log.d("CurrentLocation", "onLocationResult: Latitude${location.latitude}")
                        Log.d("CurrentLocation", "onLocationResult: Longitude${location.longitude}")

                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val email = currentUser?.email.toString()

                        val locationData = mutableMapOf<String, Any>(
                            "latitude" to location.latitude.toString(),
                            "longitude" to location.longitude.toString()
                        )

                        val db = Firebase.firestore

                        db.collection("users")
                            .document(email)
                            .update(locationData)
                            .addOnSuccessListener {
                                Log.d("TAG", "onCreate: Location Updated Successfully!!!")
                            }
                            .addOnFailureListener {
                                Log.d("TAG", "onCreate: Location Not Updated!!!")
                            }
                    }
                }
            },
            Looper.myLooper()
        )
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showGPSNotEnabledDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Enable GPS")
            .setMessage("Required for Safety")
            .setCancelable(false)
            .setPositiveButton("Enable Now") { _, _ ->
                context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }

    private fun isAllPermissionsGranted(): Boolean {
        for (item in permissions) {
            if (ContextCompat
                    .checkSelfPermission(
                        this,
                        item
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun askForPermission() {
        ActivityCompat.requestPermissions(this, permissions, permissionCode)
    }

    private fun inflateFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == permissionCode) {
            if (allPermissionGranted()) {
                performInitialSetup()
            } else {
                Toast.makeText(
                    this,
                    "Permissions are required for this app to function correctly.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun allPermissionGranted(): Boolean {
        for (item in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    item
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}