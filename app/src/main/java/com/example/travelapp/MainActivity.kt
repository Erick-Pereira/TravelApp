package com.example.travelapp

import AdjustPromptScreen
import ShowScriptScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.registeruser.screens.RegisterUserScreen
import com.example.travelapp.screens.AboutScreen
import com.example.travelapp.screens.LoginUserScreen
import com.example.travelapp.screens.HomeScreen
import com.example.travelapp.screens.LoggedScreen
import com.example.travelapp.screens.RegisterTravelScreen
import com.example.travelapp.screens.Screen1
import com.example.travelapp.ui.theme.TravelAppTheme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.example.travelapp.viewmodel.RegisterTravelListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Activity()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Activity() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "TravelApp") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = "LoginUserScreen"
            ) {
                composable(route = "LoginUserScreen") {
                    LoginUserScreen(onNavigateTo = { navController.navigate(it) })
                }
                composable(route = "RegisterUserScreen") {
                    RegisterUserScreen(onNavigateTo = { navController.navigate(it) })
                }
                composable(route = "LoggedScreen") {
                    LoggedScreen(onBack = { navController.navigate("LoginUserScreen") })
                }
                composable(route = "HomeScreen") {
                    HomeScreen(
                        onNavigateTo = { navController.navigate(it) },
                        onBack = { navController.navigate("LoginUserScreen") }
                    )
                }
                composable(route = "AboutScreen") {
                    AboutScreen()
                }
                composable(route = "Screen1") {
                    Screen1()
                }
                composable(
                    route = "form?id={id}",
                    arguments = listOf(navArgument("id") {
                        type = NavType.IntType
                        defaultValue = -1
                    })
                ) { navBackStackEntry ->
                    val id = navBackStackEntry.arguments?.getInt("id") ?: -1
                    val registerTravelViewModel: RegisterTravelListViewModel = viewModel()
                    RegisterTravelScreen(
                        travelId = id,
                        onNavigateBack = { navController.navigate("TravelListScreen") },
                        onNavigateToShowScript = { prompt, travelId ->
                            registerTravelViewModel.roteiroTemp = prompt
                            navController.navigate("ShowScriptScreen?travelId=$travelId")
                        }
                    )
                }
                composable(
                    route = "RegisterTravelScreen?travelId={travelId}",
                    arguments = listOf(navArgument("travelId") {
                        type = NavType.IntType
                        defaultValue = -1
                    })
                ) { navBackStackEntry ->
                    val travelId = navBackStackEntry.arguments?.getInt("travelId") ?: -1
                    val registerTravelViewModel: RegisterTravelListViewModel = viewModel()
                    RegisterTravelScreen(
                        travelId = travelId,
                        onNavigateBack = { navController.navigate("TravelListScreen") },
                        onNavigateToShowScript = { roteiro, travelId ->
                            registerTravelViewModel.roteiroTemp = roteiro
                            navController.navigate("ShowScriptScreen?travelId=$travelId")
                        }
                    )
                }
                composable(
                    route = "ShowScriptScreen?travelId={travelId}",
                    arguments = listOf(
                        navArgument("travelId") { type = NavType.IntType; defaultValue = -1 }
                    )
                ) { navBackStackEntry ->
                    val travelId = navBackStackEntry.arguments?.getInt("travelId") ?: -1
                    val registerTravelViewModel: RegisterTravelListViewModel = viewModel()
                    val script = registerTravelViewModel.roteiroTemp ?: ""
                    ShowScriptScreen(
                        script = script,
                        onAccept = {
                            // Salvar o roteiro no banco de dados
                            // Exemplo: registerTravelViewModel.updateTravelScript(travelId, script)
                            // Voltar para a lista ou tela anterior
                            navController.navigate("TravelListScreen")
                        },
                        onReject = {
                            // Voltar para a tela de ajuste
                            navController.navigate("TravelListScreen")
                        },
                        onAdjust = {
                            // Voltar para a tela de ajuste
                            navController.navigate("AdjustPromptScreen?travelId=$travelId")
                        }
                    )
                }
            }
        }
    }
}

