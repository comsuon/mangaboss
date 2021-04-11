package com.comsuon.trumtruyentranh.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.comsuon.trumtruyentranh.R
import com.comsuon.trumtruyentranh.ui.viewmodels.MangaViewModel

class MainActivity : AppCompatActivity() {
    val vm: MangaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
        title = getString(R.string.app_name)
//        observeData()

    }

    /*private fun observeData() {
        vm.networkState.observe(this, Observer { state ->
            when (state) {

            }
        })
    }*/
}