package com.kairos.app.ui.screens.routine

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.kairos.app.R

@Composable
fun ExerciseSessionScreen(onFinish: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // El "Surface" donde se verá la cámara
    val previewView = remember { PreviewView(context) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // --- CÁMARA DE FONDO ---
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Inicialización de la cámara (CameraX)
        LaunchedEffect(Unit) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_FRONT_CAMERA,
                        preview
                    )
                } catch (e: Exception) {
                    Log.e("Camera", "Error al abrir cámara: ${e.message}")
                }
            }, ContextCompat.getMainExecutor(context))
        }

        // --- INTERFAZ SUPERPUESTA (UI) ---
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Rutina de movilidad de hombros",
                color = Color(0xFF1B4332), // Verde oscuro
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                // Indicador de Calidad IA
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(12.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(modifier = Modifier.fillMaxWidth().height(10.dp)) {
                            Box(Modifier.weight(1f).fillMaxHeight().background(Color.Red))
                            Box(Modifier.weight(1f).fillMaxHeight().background(Color.Yellow))
                            Box(Modifier.weight(1f).fillMaxHeight().background(Color(0xFF2D6A4F)))
                        }
                        Text("BIEN", color = Color.Yellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("CON CORRECCIONES", color = Color.Yellow, fontSize = 9.sp)
                    }
                }

                // Doctor Avatar
                Box(modifier = Modifier.size(100.dp).clip(CircleShape).border(2.dp, Color.White, CircleShape)) {
                    Image(
                        painter = painterResource(R.drawable.doctor_avatar),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Panel Inferior
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5).copy(alpha = 0.95f))
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Repeticiones: 8/15", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { /* Pausar */ }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
                            Text("Pausar", fontSize = 12.sp)
                        }
                        Button(onClick = onFinish, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                            Text("Finalizar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}