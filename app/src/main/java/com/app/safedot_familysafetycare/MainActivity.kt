package com.app.safedot_familysafetycare

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.safedot_familysafetycare.databinding.ActivityMainBinding
import com.app.safedot_familysafetycare.fragments.DashboardFragment
import com.app.safedot_familysafetycare.fragments.GuardFragment
import com.app.safedot_familysafetycare.fragments.HomeFragment
import com.app.safedot_familysafetycare.fragments.MapsFragment
import com.app.safedot_familysafetycare.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_CONTACTS
    )

    val permissionCode = 78

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        askForPermission()

        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.home -> {
                    inflateFragment(HomeFragment())
                }

                R.id.dashboard -> {
                    inflateFragment(MapsFragment())
                }

                R.id.guard -> {
                    inflateFragment(GuardFragment())
                }

                R.id.profile -> {
                    inflateFragment(ProfileFragment())
                }
            }

            true
        }

        binding.bottomNavigation.selectedItemId = R.id.home
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
                // openCamera()
            } else {

            }
        }
    }

    /*private fun openCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }*/

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