package com.kairos.app.ui.screens.progreso

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController // Importación necesaria para la navegación
import com.kairos.app.R
import com.kairos.app.ui.screens.auth.BlurMancha
import com.kairos.app.ui.screens.auth.KairosColors
import com.kairos.app.ui.screens.home.*

@Composable
fun ProgresoScreen(
    navController: NavHostController, // Agregamos el parámetro para que MainActivity no de error
    userName: String = "Juan Pérez",
    dateLabel: String = "Jueves, 26 de febrero, 2026"
) {
    var showChat by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Fondos decorativos
        BlurMancha(color = KairosColors.Teal.copy(alpha = 0.15f), size = 600f, offset = Offset(-100f, -100f))
        BlurMancha(color = KairosColors.GreenLime.copy(alpha = 0.1f), size = 500f, offset = Offset(150f, 600f), alignEnd = true)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopBar(
                userName = userName,
                dateLabel = dateLabel,
                onSend = { showChat = !showChat; showNotifications = false; showMenu = false },
                onNotifications = { showNotifications = !showNotifications; showChat = false; showMenu = false },
                onMenu = { showMenu = !showMenu; showChat = false; showNotifications = false }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Estadísticas de progreso",
                modifier = Modifier.padding(horizontal = 24.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            MainStatsContainer()

            Spacer(modifier = Modifier.height(150.dp))
        }

        // Popups
        if (showChat) ChatWindowPopup(onDismiss = { showChat = false })
        if (showNotifications) NotificationsWindowPopup(onDismiss = { showNotifications = false })
        if (showMenu) MenuWindowPopup(onDismiss = { showMenu = false })

        // USAMOS LA BARRA GENERAL: Ahora los clics en los iconos funcionarán
        BottomNavBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun MainStatsContainer() {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF80CED7).copy(alpha = 0.7f))
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Evolución de movilidad del hombro (mes)", fontWeight = FontWeight.Bold, color = Color(0xFF005F73), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Surface(modifier = Modifier.fillMaxWidth().height(200.dp), shape = RoundedCornerShape(24.dp), color = Color.White.copy(alpha = 0.6f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(12.dp)) {
                    Text("Grafica de evolución", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Spacer(Modifier.weight(1f))
                    // Icono temporal que representa el progreso
                    Icon(painterResource(R.drawable.ic_progress), contentDescription = null, modifier = Modifier.size(100.dp), tint = Color(0xFF005F73))
                    Spacer(Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatMiniCard(title = "Cumplimiento de rutina (esta semana)", modifier = Modifier.weight(1f)) {
                    Text("4 de 7", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFF005F73))
                    Text("sesiones\ncompletadas", fontSize = 10.sp, textAlign = TextAlign.Center)
                }
                StatMiniCard(title = "Escala de dolor (EVN)", modifier = Modifier.weight(1f)) {
                    Box(modifier = Modifier.width(40.dp).height(60.dp).background(Color(0xFF005F73).copy(0.3f), RoundedCornerShape(4.dp)))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatPill(icon = "🔥", title = "¡4 días seguidos!", subtitle = "Sigue así", modifier = Modifier.weight(1f))
                StatPill(icon = "⏰", title = "4.5 Horas", subtitle = "Tiempo total", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun StatMiniCard(title: String, modifier: Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier.height(140.dp), shape = RoundedCornerShape(30.dp), color = Color.White.copy(alpha = 0.5f)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, lineHeight = 13.sp)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun StatPill(icon: String, title: String, subtitle: String, modifier: Modifier) {
    Surface(modifier = modifier.height(70.dp), shape = RoundedCornerShape(35.dp), color = Color.White.copy(alpha = 0.8f)) {
        Row(modifier = Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 24.sp)
            Spacer(Modifier.width(8.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF005F73))
                Text(subtitle, fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}