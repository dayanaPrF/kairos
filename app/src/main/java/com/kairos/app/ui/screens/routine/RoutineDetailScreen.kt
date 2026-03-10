package com.kairos.app.ui.screens.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kairos.app.R
import com.kairos.app.ui.screens.auth.BlurMancha
import com.kairos.app.ui.screens.auth.KairosColors
import com.kairos.app.ui.screens.home.*

@Composable
fun RoutineDetailScreen(
    navController: NavHostController, // Agregado para navegación
    routineTitle: String = "RUTINA DE MOVILIDAD DE HOMBRO\n(20 minutos)",
) {
    var isSessionActive by remember { mutableStateOf(false) }

    if (isSessionActive) {
        // Asumiendo que ExerciseSessionScreen está en tu proyecto
        ExerciseSessionScreen(onFinish = { isSessionActive = false })
    } else {
        var showPhaseDialog by remember { mutableStateOf(false) }
        var showStartAllDialog by remember { mutableStateOf(false) }
        var showChat by remember { mutableStateOf(false) }
        var showNotifications by remember { mutableStateOf(false) }
        var showMenu by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            // Fondos decorativos
            BlurMancha(color = KairosColors.Teal.copy(alpha = 0.2f), size = 600f, offset = Offset(-150f, -100f))
            BlurMancha(color = KairosColors.GreenLime.copy(alpha = 0.15f), size = 500f, offset = Offset(100f, 550f), alignEnd = true)

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            ) {
                TopBar(
                    userName = "Juan Pérez",
                    dateLabel = "Jueves, 26 de febrero, 2026",
                    onSend = { showChat = !showChat; showNotifications = false; showMenu = false },
                    onNotifications = { showNotifications = !showNotifications; showChat = false; showMenu = false },
                    onMenu = { showMenu = !showMenu; showChat = false; showNotifications = false }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Banner de título
                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).clip(RoundedCornerShape(30.dp))
                        .background(KairosColors.Teal.copy(alpha = 0.85f)).padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = routineTitle, color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
                }

                Spacer(modifier = Modifier.height(25.dp))

                Text(text = "Detalles de la rutina", modifier = Modifier.padding(horizontal = 28.dp), fontSize = 21.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(12.dp))

                // Card de Fases
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF80CED7).copy(alpha = 0.92f))
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        PhaseItem(fase = "Fase 1", titulo = "Calentamiento Dinámico", tiempo = "5 min", onPlayClick = { showPhaseDialog = true })
                        PhaseItem(fase = "Fase 2", titulo = "Movilidad Activa", tiempo = "10 min", onPlayClick = { showPhaseDialog = true })
                        PhaseItem(fase = "Fase 3", titulo = "Estiramiento y Relajación", tiempo = "5 min", onPlayClick = { showPhaseDialog = true })

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { showStartAllDialog = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(22.dp)
                        ) {
                            Text("COMENZAR TODO", color = Color(0xFF005F73), fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(120.dp))
            }

            // Popups de UI
            if (showChat) ChatWindowPopup(onDismiss = { showChat = false })
            if (showNotifications) NotificationsWindowPopup(onDismiss = { showNotifications = false })
            if (showMenu) MenuWindowPopup(onDismiss = { showMenu = false })

            // Diálogos de instrucciones
            if (showPhaseDialog) {
                RoutineInstructionsDialog(
                    title = "Antes de comenzar",
                    description = "El objetivo es aumentar el flujo sanguíneo y lubricar la articulación.",
                    instructions = listOf("Círculos de hombros: 2 series.", "Cruce de brazos.", "Rotación de cuello."),
                    onConfirm = { showPhaseDialog = false; isSessionActive = true },
                    onDismiss = { showPhaseDialog = false }
                )
            }

            if (showStartAllDialog) {
                RoutineInstructionsDialog(
                    title = "Rutina Completa",
                    description = "Vas a iniciar las 3 fases seguidas. Prepárate para 20 minutos.",
                    instructions = listOf("Respira fluido.", "Si hay dolor, para.", "Ten agua cerca."),
                    onConfirm = { showStartAllDialog = false; isSessionActive = true },
                    onDismiss = { showStartAllDialog = false }
                )
            }

            // BARRA DE NAVEGACIÓN GLOBAL
            BottomNavBar(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun RoutineInstructionsDialog(title: String, description: String, instructions: List<String>, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF80CED7)), shape = RoundedCornerShape(16.dp)) {
                Text("COMENZAR", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(32.dp),
        containerColor = Color.White,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(title, color = Color(0xFF80CED7), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("El médico indicó:", fontSize = 14.sp, color = Color.Gray)
            }
        },
        text = {
            Column {
                Text(description, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))
                instructions.forEach { Text("• $it", fontSize = 14.sp) }
            }
        }
    )
}

@Composable
fun PhaseItem(fase: String, titulo: String, tiempo: String, onPlayClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Color.White.copy(alpha = 0.28f)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(fase, color = Color.White.copy(0.9f), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(titulo, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            Text(tiempo, color = Color.White.copy(0.85f), fontSize = 13.sp)
        }
        Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = Color.White, shadowElevation = 2.dp) {
            IconButton(onClick = onPlayClick) { Icon(Icons.Default.PlayArrow, null, tint = Color(0xFF005F73)) }
        }
    }
}