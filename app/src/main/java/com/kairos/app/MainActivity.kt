package com.kairos.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kairos.app.ui.screens.auth.LoginScreen
import com.kairos.app.ui.screens.citas.CitasScreen
import com.kairos.app.ui.screens.home.HomeScreen
import com.kairos.app.ui.screens.progreso.ProgresoScreen
import com.kairos.app.ui.screens.routine.RoutineDetailScreen
import com.kairos.app.ui.theme.KairosTheme

// 1. DEFINICIÓN DE RUTAS (Para evitar errores de dedo)
object Destinos {
    const val LOGIN = "login"
    const val HOME = "home"
    const val ROUTINE = "routine"
    const val CITAS = "citas"
    const val PROGRESO = "progreso"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KairosTheme {
                // Iniciamos el flujo de navegación
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // 2. CONFIGURACIÓN DEL NAVHOST
    NavHost(
        navController = navController,
        startDestination = Destinos.LOGIN // Inicia en el Login
    ) {
        // Pantalla de Login
        composable(Destinos.LOGIN) {
            LoginScreen(onLoginSuccess = {
                // Al presionar "Ingresar", navegamos al Home
                navController.navigate(Destinos.HOME) {
                    // Limpiamos el historial para que no se pueda regresar al Login
                    popUpTo(Destinos.LOGIN) { inclusive = true }
                }
            })
        }

        // Pantalla Principal (Dashboard)
        composable(Destinos.HOME) {
            HomeScreen(navController = navController)
        }

        // Pantalla de Rutina Detallada
        composable(Destinos.ROUTINE) {
            RoutineDetailScreen(navController = navController)
        }

        // Pantalla de Citas
        composable(Destinos.CITAS) {
            CitasScreen(navController = navController)
        }

        // Pantalla de Progreso/Estadísticas
        composable(Destinos.PROGRESO) {
            ProgresoScreen(navController = navController)
        }
    }
}