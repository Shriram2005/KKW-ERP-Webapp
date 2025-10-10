package com.kkwieer.erpwebapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.kkwieer.erpwebapp.navigation.NavGraph
import com.kkwieer.erpwebapp.ui.theme.KKWIEERERPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KKWIEERERPTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}

