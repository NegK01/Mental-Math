package com.negk01.mentalmath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.negk01.mentalmath.ui.theme.MentalMathTheme
import com.negk01.mentalmath.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MentalMathTheme {
                AppNavigation()
            }
        }
    }
}