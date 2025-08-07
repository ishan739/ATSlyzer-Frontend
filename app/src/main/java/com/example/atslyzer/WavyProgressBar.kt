package com.example.atslyzer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun WavyProgressBar(progress: Float, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing)
        )
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {
        val waveHeight = 10f
        val waveLength = size.width / 2f
        val path = Path()

        val progressWidth = size.width * 0.7f
        val offsetX = waveOffset * waveLength

        path.moveTo(-offsetX, size.height / 2)

        var x = -offsetX
        while (x < progressWidth + waveLength) {
            path.relativeQuadraticBezierTo(
                waveLength / 4, -waveHeight,
                waveLength / 2, 0f
            )
            path.relativeQuadraticBezierTo(
                waveLength / 4, waveHeight,
                waveLength / 2, 0f
            )
            x += waveLength
        }

        drawPath(
            path = path,
            color = Color(0xFF8A2BE2),
            style = Stroke(width = 8f, cap = StrokeCap.Round)
        )
    }
}
