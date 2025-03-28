package com.example.travelapp.screens;

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.registeruser.screens.RegisterUserScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggedScreen(
    onBack: () -> Unit
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val backStack = navController.currentBackStackEntryAsState();
                val currentDestination = backStack.value?.destination
                BottomNavigationItem(selected = currentDestination?.hierarchy?.any { it.route == "HomeScreen" } == true,
                    onClick = { navController.navigate("HomeScreen") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                        )
                    })
                BottomNavigationItem(
                    selected = currentDestination?.hierarchy?.any { it.route == "AboutScreen" } == true,
                    onClick = { navController.navigate("AboutScreen") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "AboutScreen",
                        )
                    })
                BottomNavigationItem(
                    selected = currentDestination?.hierarchy?.any { it.route == "Screen1" } == true,
                    onClick = { navController.navigate("Screen1") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Screen1",
                        )
                    })
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = "HomeScreen"
            ) {
                composable(route = "HomeScreen") {
                    HomeScreen(
                        onNavigateTo = { navController.navigate(it) },
                        onBack = { onBack })
                }
                composable(route = "AboutScreen") {
                    AboutScreen()
                }
                composable(route = "Screen1") {
                    Screen1()
                }
            }
        }
    }

}
