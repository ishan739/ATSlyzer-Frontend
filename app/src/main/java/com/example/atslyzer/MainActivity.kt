package com.example.atslyzer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.atslyzer.ui.theme.ATSlyzerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        super.onCreate(savedInstanceState,)
        enableEdgeToEdge()
        setContent {
            ATSlyzerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ATSResumeCheckerScreen()
                }
            }
        }
    }
}
