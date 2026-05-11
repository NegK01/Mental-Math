package com.negk01.mentalmath

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.negk01.mentalmath.navigation.AppNavigation
import com.negk01.mentalmath.ui.utils.ImmersiveAppContent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (resources.configuration.smallestScreenWidthDp < 600) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }
        enableEdgeToEdge()

        setContent {
            ImmersiveAppContent { // Envolver AppNavigation con ImmersiveAppContent
                AppNavigation()
            }
        }
    }
}
