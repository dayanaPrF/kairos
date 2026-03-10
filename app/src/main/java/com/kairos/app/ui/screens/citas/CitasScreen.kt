package com.kairos.app.ui.screens.citas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kairos.app.R
import com.kairos.app.ui.screens.auth.BlurMancha
import com.kairos.app.ui.screens.auth.KairosColors
import com.kairos.app.ui.screens.home.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import androidx.navigation.NavHostController // ASEGÚRATE DE TENER ESTE IMPORT
import com.kairos.app.ui.screens.home.BottomNavBar // Importamos la barra general

@Composable
fun CitasScreen(
    navController: NavHostController, // 1. AGREGAMOS EL CONTROLADOR AQUÍ
    userName: String = "Juan Pérez",
    dateLabel: String = "Jueves, 26 de febrero, 2026"
) {
    var showChat by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showReprogramPopup by remember { mutableStateOf(false) }

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
                text = "CALENDARIO DE CITAS",
                modifier = Modifier.padding(horizontal = 24.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            CalendarWidget()

            Spacer(modifier = Modifier.height(24.dp))

            CitaInfoCard(onReprogramClick = { showReprogramPopup = true })

            Spacer(modifier = Modifier.height(150.dp))
        }

        // Popups
        if (showChat) ChatWindowPopup(onDismiss = { showChat = false })
        if (showNotifications) NotificationsWindowPopup(onDismiss = { showNotifications = false })
        if (showMenu) MenuWindowPopup(onDismiss = { showMenu = false })

        if (showReprogramPopup) {
            ReprogramAlertPopup(onDismiss = { showReprogramPopup = false })
        }

        // 2. USAMOS LA BARRA GENERAL QUE YA TIENE LA LÓGICA DE NAVEGACIÓN
        BottomNavBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun CalendarWidget() {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7
    val monthName = currentMonth.month.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
        .replaceFirstChar { it.uppercase() }

    Column(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$monthName ${currentMonth.year}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2D6A4F))
            Row {
                IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) { Icon(Icons.Default.ChevronLeft, null) }
                IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) { Icon(Icons.Default.ChevronRight, null) }
            }
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb").forEach { dia ->
                        Text(text = dia, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                }

                val totalSlots = firstDayOfMonth + daysInMonth
                val rows = (totalSlots + 6) / 7

                repeat(rows) { weekIndex ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                        repeat(7) { dayIndex ->
                            val slotIndex = weekIndex * 7 + dayIndex
                            val dayNumber = slotIndex - firstDayOfMonth + 1
                            if (dayNumber in 1..daysInMonth) {
                                DayCell(day = dayNumber.toString(), modifier = Modifier.weight(1f), isSelected = (dayNumber == 27 && currentMonth.monthValue == 2))
                            } else {
                                Box(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DayCell(day: String, modifier: Modifier, isSelected: Boolean) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color(0xFF80CED7) else Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(text = day, fontSize = 14.sp, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) Color.White else Color.Black)
    }
}

@Composable
fun CitaInfoCard(onReprogramClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = KairosColors.GreenLime.copy(alpha = 0.9f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("PRÓXIMA CITA: 27 febrero 2026", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.8f)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("10:00 AM - 11:00 AM", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color(0xFF005F73))
                    Text("Sesión de fisioterapia - Hombro", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onReprogramClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50)
            ) {
                Text("Reprogramar", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ReprogramAlertPopup(onDismiss: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
        Card(modifier = Modifier.padding(24.dp).fillMaxWidth(0.85f), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF74A12E))) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Para poder reprogramar debes consultar primero con tu fisioterapeuta.", color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.White), shape = RoundedCornerShape(50)) {
                    Text("Ir a chat", color = Color(0xFF74A12E), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}