package com.negk01.mentalmath

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.negk01.mentalmath.navigation.AppNavigation
import com.negk01.mentalmath.ui.utils.ImmersiveAppContent // Importar ImmersiveAppContent

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ImmersiveAppContent { // Envolver AppNavigation con ImmersiveAppContent
                AppNavigation()
            }
        }
    }
}
