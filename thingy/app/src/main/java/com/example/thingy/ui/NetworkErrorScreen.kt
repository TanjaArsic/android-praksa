package com.example.thingy.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thingy.R

@Composable
fun NetworkErrorScreen(errorText: String?, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img),
            contentDescription = "Network Connectivity Error",
            modifier = Modifier.size(500.dp)
        )
        Text(
            text = "Network Error",
            fontSize = 35.sp,
            fontWeight = FontWeight.SemiBold,
            color = Colors.ERROR.color
        )
        Spacer(modifier = Modifier.size(10.dp))
        if (errorText != null) {
            Text(
                text = "There was an error connecting. Please check your internet.",
                fontSize = 20.sp,
                color = Colors.ERROR.color,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            ElevatedButton(
                modifier = Modifier.size(width = 120.dp, height = 50.dp),
                onClick = {
                    onClick()
                }) {
                Text("Retry", color = Colors.ERROR.color, fontSize = 17.sp)
            }
        }
    }
}
