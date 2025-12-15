package com.example.rec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.rec.navigation.AppNav
import com.example.rec.ui.theme.RecTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RecTheme {
                AppNav()
            }
        }
    }
}
