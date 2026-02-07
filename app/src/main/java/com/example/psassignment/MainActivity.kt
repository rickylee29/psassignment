package com.example.psassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.psassignment.ui.screens.drivers.DriversListScreen
import com.example.psassignment.ui.theme.PSAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PSAssignmentTheme {
                DriversListScreen()
            }
        }
    }
}