package com.example.furnichic.util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.furnichic.R
import com.example.furnichic.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigationView(){
    val bottomNavigationView =
        requireActivity().findViewById<BottomNavigationView>(
            R.id.bottomNavigation
        )
    bottomNavigationView.visibility =View.GONE
}

fun Fragment.showBottomNavigationView(){
    val bottomNavigationView =
        requireActivity().findViewById<BottomNavigationView>(
            R.id.bottomNavigation
        )
    bottomNavigationView.visibility =View.VISIBLE
}