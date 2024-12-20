package com.nomade.miremis.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RoundedRectanglesRow(s1: Boolean, s2: Boolean, s3: Boolean) {


    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        // Primer rectángulo
        RoundedRectangle(
            isActive = s1
        )
        Spacer(modifier = Modifier.width(10.dp))
        // Segundo rectángulo
        RoundedRectangle(
            isActive = s2
        )
        Spacer(modifier = Modifier.width(10.dp))
        // Tercer rectángulo
        RoundedRectangle(
            isActive = s3
        )
        Spacer(modifier = Modifier.width(5.dp))
    }
}

@Composable
fun RoundedRectangle(isActive: Boolean) {
    val color = if (isActive) Color.Cyan else Color.Gray
    Box(
        modifier = Modifier
            .size(80.dp, 10.dp)
            .background(color = color, shape = RoundedCornerShape(8.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun RoundedRectanglesRowPreview() {
    Column {
        RoundedRectanglesRow(true, false, false)
    }
}
