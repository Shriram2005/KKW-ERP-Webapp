package com.kkwieer.erpwebapp

import android.app.Activity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kkwieer.erpwebapp.navigation.NavGraph
import com.kkwieer.erpwebapp.navigation.Screen
import com.kkwieer.erpwebapp.ui.theme.KKWIEERERPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KKWIEERERPTheme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route
                var lastBackPressTimestamp by remember { mutableStateOf(0L) }
                val context = LocalContext.current
                val activity = context as? Activity

                if (currentRoute == Screen.Home.route) {
                    BackHandler {
                        val now = SystemClock.elapsedRealtime()
                        if (now - lastBackPressTimestamp <= 2000L) {
                            activity?.finish()
                        } else {
                            lastBackPressTimestamp = now
                            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                NavGraph(navController = navController)
            }
        }
    }
}

