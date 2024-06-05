package com.example.thingy.ui.activity

import StreamScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.thingy.BaseApplication
import com.example.thingy.ui.screens.axonlist.AxonScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var app: BaseApplication

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {

                    AxonScreen(
                        navController = navController,
                    )

                }
                composable(
                    "details/{axonId}",
                    arguments = listOf(navArgument("axonId") { type = NavType.StringType })
                ) {

                    StreamScreen(navController)
                }
            }
        }
    }
}



