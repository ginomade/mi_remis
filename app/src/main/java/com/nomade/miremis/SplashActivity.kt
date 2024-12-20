package com.nomade.miremis

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class SplashActivity  : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Simular una operación de carga, luego iniciar MainActivity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}