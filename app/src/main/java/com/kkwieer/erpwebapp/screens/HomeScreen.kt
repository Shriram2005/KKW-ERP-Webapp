package com.kkwieer.erpwebapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kkwieer.erpwebapp.R
import com.kkwieer.erpwebapp.ui.bounceClick

@Composable
fun HomeScreen(
    onNavigateToLMS: () -> Unit,
    onNavigateToMobileAppDev: () -> Unit,
    onNavigateToAERP: () -> Unit,
    onNavigateToDeveloperInfo: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1976D2), // Blue
                        Color(0xFF1565C0), // Darker Blue
                        Color(0xFF0D47A1)  // Deep Blue
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 2000f)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // College Logo
            Image(
                painter = painterResource(id = R.drawable.college_logo),
                contentDescription = "College Logo",
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "KKW ERP Portal",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Select a Portal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // LMS Portal Button
            Button(
                onClick = onNavigateToLMS,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .bounceClick(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF1976D2)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "LMS Portal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mobile App Development Course Button
            Button(
                onClick = onNavigateToMobileAppDev,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .bounceClick(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF1976D2)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Mobile App\nDevelopment Course",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AERP Login Button
            Button(
                onClick = onNavigateToAERP,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .bounceClick(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF1976D2)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "AERP Login",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Small Developer Info Button - positioned at bottom right, very unobtrusive
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onNavigateToDeveloperInfo,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White.copy(alpha = 0.6f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Developer Info",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Info",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
