package com.kairos.app.ui.screens.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kairos.app.R

// ─────────────────────────────────────────────
//  COLORES DE LA APP
// ─────────────────────────────────────────────
object KairosColors {
    val Teal        = Color(0xFF4BBFBF)
    val GreenLime   = Color(0xFF8DC63F)
    val DarkText    = Color(0xFF4B4B4B)
    val HintText    = Color(0xFFAAAAAA)
    val InputBorder = Color(0xFF8DC63F)
}

// ─────────────────────────────────────────────
//  LOGIN SCREEN
// ─────────────────────────────────────────────
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit, // Este callback te lleva al Home
    onGoogleLogin: () -> Unit = {},
    onFacebookLogin: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onRegister: () -> Unit = {}
) {
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Fondos decorativos
        BlurMancha(color = KairosColors.Teal, size = 350f, offset = Offset(50f, 50f))
        BlurMancha(color = KairosColors.GreenLime, size = 300f, offset = Offset(x = 1000f, y = 0f), alignEnd = true)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(72.dp))
            KairosLogo()
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text  = "Movimiento medido, progreso real",
                color = KairosColors.GreenLime,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(36.dp))
            Text(text = "¡Bienvenido!", color = KairosColors.DarkText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(28.dp))

            // Inputs (Aunque no se validen ahora, guardan el texto que escribas)
            KairosInputField(
                value = email,
                onValueChange = { email = it },
                hint = "Correo electrónico o usuario",
                icon = Icons.Outlined.Person,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            KairosInputField(
                value = password,
                onValueChange = { password = it },
                hint = "Contraseña",
                icon = Icons.Outlined.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = null,
                            tint = KairosColors.HintText
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN DE INGRESAR (Puente directo al Home)
            Button(
                onClick = {
                    // No hay validación, entra directo al presionar
                    onLoginSuccess()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape  = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = KairosColors.Teal,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Ingresar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                Text(text = "¿Olvidaste tu contraseña?  ", fontSize = 12.sp, modifier = Modifier.clickable { onForgotPassword() })
                Text(text = "¿No tienes cuenta?", color = KairosColors.GreenLime, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, modifier = Modifier.clickable { onRegister() })
            }

            Spacer(modifier = Modifier.height(20.dp))
            OrDivider()
            Spacer(modifier = Modifier.height(20.dp))

            SocialButton(label = "Iniciar sesión con Google", iconResId = R.drawable.logo_google, onClick = onGoogleLogin)
            Spacer(modifier = Modifier.height(12.dp))
            SocialButton(label = "Iniciar sesión con Facebook", iconResId = R.drawable.logo_facebook, onClick = onFacebookLogin)

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "www.ourwebsite.com", color = Color(0xFFBBBBBB), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ─────────────────────────────────────────────
// COMPONENTES AUXILIARES
// ─────────────────────────────────────────────

@Composable
fun BlurMancha(color: Color, size: Float, offset: Offset, alignEnd: Boolean = false) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val xPos = if (alignEnd) this.size.width - (size / 2.5f) else offset.x
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color.copy(alpha = 0.4f), Color.Transparent),
                center = Offset(xPos, offset.y),
                radius = size
            ),
            radius = size,
            center = Offset(xPos, offset.y)
        )
    }
}

@Composable
fun KairosLogo() {
    Image(painter = painterResource(id = R.drawable.logo_kairos), contentDescription = null, modifier = Modifier.size(160.dp))
}

@Composable
fun KairosInputField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(42.dp).clip(CircleShape).background(KairosColors.Teal), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(hint, fontSize = 13.sp) },
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = trailingIcon,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = KairosColors.Teal,
                unfocusedBorderColor = KairosColors.InputBorder
            )
        )
    }
}

@Composable
fun OrDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFDDDDDD))
        Text("  o  ", color = Color.Gray, fontSize = 14.sp)
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFDDDDDD))
    }
}

@Composable
fun SocialButton(label: String, iconResId: Int, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .clip(RoundedCornerShape(25.dp))
        .background(Color(0xFFF5F5F5))
        .border(1.dp, Color(0xFFDDDDDD), RoundedCornerShape(25.dp))
        .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = iconResId), contentDescription = null, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}