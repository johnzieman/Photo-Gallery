package com.ziemapp.johnzieman.photogallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation

class PhotoGalleryActivity : AppCompatActivity() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)
        navController = Navigation.findNavController(this, R.id.nav_host)
    }
}