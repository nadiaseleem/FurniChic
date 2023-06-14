package com.example.furnichic.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bumptech.glide.Glide
import com.example.furnichic.databinding.ActivitySplashScreenBinding
import com.example.furnichic.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel>()
    private lateinit var binding: ActivitySplashScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this)
            .load(getImage("splash_screen"))
            .into(binding.imgview)


        val isUserSignedIn = loginViewModel.isUserSignedIn()
        Log.d("@@@","$isUserSignedIn")
        if (isUserSignedIn) {
            val intent = Intent(this, ShoppingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            Handler().postDelayed({
                startActivity(intent)
            }, 1500)
        } else
            Handler().postDelayed({
                val intent = Intent(this,LoginRegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)

            }, 1500)

    }

    fun getImage(imageName: String?): Int {
        return resources.getIdentifier(imageName, "drawable", this.packageName)
    }



}