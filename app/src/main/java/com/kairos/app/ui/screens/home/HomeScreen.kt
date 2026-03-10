package com.kairos.app.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kairos.app.R
import com.kairos.app.ui.screens.auth.BlurMancha
import com.kairos.app.ui.screens.auth.KairosColors

// ─────────────────────────────────────────────
// 1. MODELOS DE DATOS
// ─────────────────────────────────────────────

data class DayProgress(
    val label: String,
    val shortLabel: String,
    val percent: Int,
    val isToday: Boolean = false
)

data class AppointmentInfo(
    val date: String,
    val modality: String
)

data class PhysioNote(
    val patientName: String,
    val noteText: String,
    val doctorName: String,
    val doctorDegree: String,
    val doctorUniversity: String
)

// ─────────────────────────────────────────────
// 2. HOME SCREEN PRINCIPAL
// ─────────────────────────────────────────────

@Composable
fun HomeScreen(
    navController: NavHostController,
    userName: String = "Juan Pérez",
    dateLabel: String = "Jueves, 26 de febrero, 2026",
    routineTitle: String = "RUTINA DE MOVILIDAD DE\nHOMBRO (20 minutos)",
    weekProgress: List<DayProgress> = sampleWeekProgress(),
    physioNote: PhysioNote = samplePhysioNote(),
    appointment: AppointmentInfo = sampleAppointment(),
    onViewMore: () -> Unit = {},
    onViewAppointment: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    var showChat by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        BlurMancha(color = KairosColors.Teal.copy(alpha = 0.3f), size = 500f, offset = Offset(-120f, -100f))
        BlurMancha(color = KairosColors.GreenLime.copy(alpha = 0.25f), size = 450f, offset = Offset(80f, 600f), alignEnd = true)

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            TopBar(
                userName = userName,
                dateLabel = dateLabel,
                onSend = { showChat = !showChat; showNotifications = false; showMenu = false },
                onNotifications = { showNotifications = !showNotifications; showChat = false; showMenu = false },
                onMenu = { showMenu = !showMenu; showChat = false; showNotifications = false }
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                pageSpacing = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> GoalCard(routineTitle, onStart = { navController.navigate("routine") })
                    1 -> StatsCard(onViewStats = { navController.navigate("progreso") })
                    2 -> WebsiteCard()
                }
            }

            PagerIndicators(pagerState.currentPage)
            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = "Resumen rápido", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                WeekProgressRow(days = weekProgress)
                Spacer(modifier = Modifier.height(32.dp))
                PhysioNoteCard(note = physioNote, onViewMore = onViewMore)
                Spacer(modifier = Modifier.height(16.dp))
                AppointmentCard(appointment = appointment, onViewDetails = { navController.navigate("citas") })
                Spacer(modifier = Modifier.height(110.dp))
            }
        }

        if (showChat) ChatWindowPopup(onDismiss = { showChat = false })
        if (showNotifications) NotificationsWindowPopup(onDismiss = { showNotifications = false })
        if (showMenu) MenuWindowPopup(onDismiss = { showMenu = false })

        BottomNavBar(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

// ─────────────────────────────────────────────
// 3. COMPONENTES DE VENTANAS (POPUPS)
// ─────────────────────────────────────────────

@Composable
fun ChatWindowPopup(onDismiss: () -> Unit) {
    Popup(alignment = Alignment.TopEnd, offset = IntOffset(-20, 220), onDismissRequest = onDismiss, properties = PopupProperties(focusable = true)) {
        Card(modifier = Modifier.width(280.dp).shadow(12.dp, RoundedCornerShape(24.dp)), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF80CED7))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(R.drawable.doctor_avatar), contentDescription = null, modifier = Modifier.size(40.dp).clip(CircleShape))
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Dr. Sebastian Rosales Quintana", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Última vez: Hoy 10:30 AM", fontSize = 9.sp, color = Color.DarkGray)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Box(modifier = Modifier.fillMaxWidth().background(Color.White.copy(0.4f), RoundedCornerShape(12.dp)).padding(10.dp)) {
                    Text("Hola Juan, ¿cómo vas con los ejercicios de hombro?", fontSize = 11.sp)
                }
                Spacer(Modifier.height(12.dp))
                Text("Ver todos los mensajes", textAlign = TextAlign.Center, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clickable { onDismiss() })
            }
        }
    }
}

@Composable
fun NotificationsWindowPopup(onDismiss: () -> Unit) {
    Popup(alignment = Alignment.TopCenter, offset = IntOffset(0, 220), onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth(0.9f).shadow(12.dp, RoundedCornerShape(28.dp)), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFA7C957))) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Notificaciones", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                repeat(2) {
                    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(8.dp).background(Color.White, CircleShape))
                        Spacer(Modifier.width(12.dp))
                        Box(Modifier.fillMaxWidth().height(15.dp).background(Color.Black.copy(0.1f), RoundedCornerShape(4.dp)))
                    }
                }
            }
        }
    }
}

@Composable
fun MenuWindowPopup(onDismiss: () -> Unit) {
    Popup(alignment = Alignment.TopEnd, offset = IntOffset(-20, 220), onDismissRequest = onDismiss) {
        Card(modifier = Modifier.width(220.dp).shadow(12.dp, RoundedCornerShape(24.dp)), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF80CED7))) {
            Column(modifier = Modifier.padding(8.dp)) {
                MenuItem(icon = Icons.Default.AccountCircle, label = "Mi Perfil")
                MenuItem(icon = Icons.Default.MedicalServices, label = "Mi fisioterapeuta")
                MenuItem(icon = Icons.Default.Settings, label = "Ajustes")
                HorizontalDivider(color = Color.White.copy(0.3f), modifier = Modifier.padding(horizontal = 8.dp))
                MenuItem(icon = Icons.Default.Logout, label = "Cerrar Sesión")
            }
        }
    }
}

@Composable
fun MenuItem(icon: ImageVector, label: String) {
    Row(modifier = Modifier.fillMaxWidth().clickable { }.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.White, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Text(label, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

// ─────────────────────────────────────────────
// 4. COMPONENTES DE LA INTERFAZ
// ─────────────────────────────────────────────

@Composable
fun TopBar(userName: String, dateLabel: String, onSend: () -> Unit, onNotifications: () -> Unit, onMenu: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 45.dp, bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painterResource(id = R.drawable.user_avatar), contentDescription = "User", contentScale = ContentScale.Crop, modifier = Modifier.size(55.dp).clip(RoundedCornerShape(12.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Buenos días ☀️", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(userName, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(dateLabel, fontSize = 11.sp, color = Color.Gray)
        }
        IconButton(onClick = onSend) { Icon(Icons.Outlined.Send, null) }
        IconButton(onClick = onNotifications) { Icon(Icons.Outlined.Notifications, null) }
        IconButton(onClick = onMenu) { Icon(Icons.Default.Menu, null) }
    }
}

@Composable
fun GoalCard(title: String, onStart: () -> Unit) {
    Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2)), modifier = Modifier.fillMaxWidth().height(260.dp)) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Metas diarias", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Tu objetivo de hoy:", color = Color.Gray, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(title, fontWeight = FontWeight.Black, fontSize = 22.sp, lineHeight = 28.sp)
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onStart, colors = ButtonDefaults.buttonColors(containerColor = KairosColors.GreenLime), shape = RoundedCornerShape(50), modifier = Modifier.fillMaxWidth().height(54.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("COMENZAR RUTINA", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.PlayCircleFilled, null, modifier = Modifier.size(28.dp))
                }
            }
        }
    }
}

@Composable
fun StatsCard(onViewStats: () -> Unit) {
    Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2)), modifier = Modifier.fillMaxWidth().height(260.dp)) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Consulta tu avance", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Conoce tu progreso de tratamiento diariamente.", fontSize = 15.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onViewStats, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67C5CD)), shape = RoundedCornerShape(50), modifier = Modifier.fillMaxWidth().height(54.dp)) {
                Text("VER ESTADÍSTICAS", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WebsiteCard() {
    Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2)), modifier = Modifier.fillMaxWidth().height(260.dp)) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Más información", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Consulta artículos sobre diversas afecciones en nuestro sitio web.", fontSize = 15.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = KairosColors.GreenLime), shape = RoundedCornerShape(50), modifier = Modifier.fillMaxWidth().height(54.dp)) {
                Text("IR AL SITIO", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PagerIndicators(currentPage: Int) {
    Row(Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.Center) {
        repeat(3) { index ->
            val color = if (index == currentPage) KairosColors.GreenLime else Color.LightGray
            Box(Modifier.size(12.dp).clip(CircleShape).background(color))
            if (index < 2) Spacer(Modifier.width(8.dp))
        }
    }
}

@Composable
fun WeekProgressRow(days: List<DayProgress>) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        days.forEach { day ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp)) {
                    Canvas(Modifier.size(48.dp)) {
                        drawArc(Color(0xFFEEEEEE), -90f, 360f, false, style = Stroke(8f))
                        drawArc(KairosColors.Teal, -90f, 360f * (day.percent/100f), false, style = Stroke(8f, cap = StrokeCap.Round))
                    }
                    Text("${day.percent}%", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(4.dp))
                Text(day.label, fontSize = 10.sp, color = Color.Gray)
                if(day.isToday) { Text(day.shortLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
fun PhysioNoteCard(note: PhysioNote, onViewMore: () -> Unit) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF80CED7).copy(alpha = 0.9f)), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp)) {
            Column(Modifier.weight(1f)) {
                Text("Notas del Fisioterapeuta", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(Modifier.height(4.dp))
                Text(note.noteText, fontSize = 13.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
                Text("Ver más...", color = Color(0xFF005F73), fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onViewMore() })
            }
            Spacer(Modifier.width(8.dp))
            Image(painterResource(R.drawable.doctor_avatar), null, Modifier.size(70.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
        }
    }
}

@Composable
fun AppointmentCard(appointment: AppointmentInfo, onViewDetails: () -> Unit) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = KairosColors.GreenLime.copy(alpha = 0.9f)), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Citas programadas", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text("Próxima cita: ${appointment.date}", fontSize = 14.sp)
            Text("Ver detalles...", fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onViewDetails() })
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController, modifier: Modifier = Modifier) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(modifier = modifier, containerColor = Color(0xFFE0E0E0)) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { if (currentRoute != "home") navController.navigate("home") },
            icon = { Icon(painterResource(R.drawable.ic_home), null, Modifier.size(28.dp)) },
            label = { Text("Principal") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF4F772D), indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = currentRoute == "routine",
            onClick = { if (currentRoute != "routine") navController.navigate("routine") },
            icon = { Icon(painterResource(R.drawable.ic_routine), null, Modifier.size(28.dp)) },
            label = { Text("Rutina") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF4F772D), indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = currentRoute == "citas",
            onClick = { if (currentRoute != "citas") navController.navigate("citas") },
            icon = { Icon(painterResource(R.drawable.ic_calendar), null, Modifier.size(28.dp)) },
            label = { Text("Citas") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF4F772D), indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = currentRoute == "progreso",
            onClick = { if (currentRoute != "progreso") navController.navigate("progreso") },
            icon = { Icon(painterResource(R.drawable.ic_progress), null, Modifier.size(28.dp)) },
            label = { Text("Progreso") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF4F772D), indicatorColor = Color.Transparent)
        )
    }
}

// ─────────────────────────────────────────────
// 5. DATOS DE PRUEBA
// ─────────────────────────────────────────────

fun sampleWeekProgress() = listOf(DayProgress("Dom", "", 95), DayProgress("Lun", "", 100), DayProgress("Mar", "", 100), DayProgress("Mié", "", 87), DayProgress("Jue", "26", 0, true), DayProgress("Vie", "", 0), DayProgress("Sáb", "", 0))
fun samplePhysioNote() = PhysioNote("Juan", "Recuerda realizar las elevaciones laterales de forma lenta.", "Dr. Sebastian", "Lic.", "UNAM")
fun sampleAppointment() = AppointmentInfo("Viernes 27 de febrero", "Presencial")

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val context = LocalContext.current
    HomeScreen(navController = NavHostController(context))
}