package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlin.math.PI
import kotlin.math.sin
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.ui.theme.UnionGoldAccent
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary

// Dynamic Union Flag Graphic from res/drawable
@Composable
fun UnionFlagBadge(
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
    width: Dp = 72.dp
) {
    Image(
        painter = painterResource(id = R.drawable.ic_tnpa_flag),
        contentDescription = "TNPA Union Flag",
        modifier = modifier
            .size(width = width, height = height)
            .clip(RoundedCornerShape(6.dp))
            .border(1.5.dp, UnionGoldBright, RoundedCornerShape(6.dp))
            .shadow(2.dp, RoundedCornerShape(6.dp))
    )
}

// Dynamic Union Logo Graphic from res/drawable
@Composable
fun UnionLogoBadge(
    modifier: Modifier = Modifier,
    size: Dp = 60.dp
) {
    Image(
        painter = painterResource(id = R.drawable.ic_tnpa_logo),
        contentDescription = "TNPA Union Official Logo",
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(1.5.dp, UnionGoldBright, CircleShape)
            .shadow(2.dp, CircleShape)
    )
}

// Live News Ticker / Running Announcement Bar
@Composable
fun RunningNewsTickerBar(
    newsText: String,
    modifier: Modifier = Modifier,
    isTamil: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ticker")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 300f,
        targetValue = -600f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tickerOffset"
    )

    Surface(
        color = UnionRedDark,
        contentColor = Color.White,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(UnionGoldAccent, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Live Ticker",
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isTamil) "முக்கிய செய்தி" else "LIVE NEWS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Text(
                    text = newsText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            }
        }
    }
}

// Custom QR Code Graphic Generator via Compose Canvas
@Composable
fun CanvasQrCode(
    data: String,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(Color.White)
            .border(2.dp, UnionRedPrimary, RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size - 16.dp)) {
            val canvasSize = this.size.width
            val gridCount = 10
            val cellSize = canvasSize / gridCount
            val hash = data.hashCode()

            // Draw Finder Patterns (Corners)
            fun drawFinder(x: Float, y: Float) {
                drawRect(color = Color.Black, topLeft = Offset(x, y), size = Size(cellSize * 3, cellSize * 3))
                drawRect(color = Color.White, topLeft = Offset(x + cellSize, y + cellSize), size = Size(cellSize, cellSize))
            }

            drawFinder(0f, 0f)
            drawFinder(cellSize * 7, 0f)
            drawFinder(0f, cellSize * 7)

            // Dynamic Data Grid Pattern based on hash
            for (row in 0 until gridCount) {
                for (col in 0 until gridCount) {
                    // Skip finder corners
                    if ((row < 3 && col < 3) || (row < 3 && col > 6) || (row > 6 && col < 3)) continue
                    val cellHash = (hash xor (row * 31 + col * 17)) % 2 == 0
                    if (cellHash) {
                        drawRect(
                            color = UnionRedDark,
                            topLeft = Offset(col * cellSize, row * cellSize),
                            size = Size(cellSize * 0.9f, cellSize * 0.9f)
                        )
                    }
                }
            }
        }
    }
}

// Emergency Helpline Card
@Composable
fun EmergencyHelplineCard(
    onCallClick: () -> Unit,
    modifier: Modifier = Modifier,
    isTamil: Boolean = true
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = UnionRedDark),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(UnionGoldAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Emergency Call",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isTamil) "24x7 அவசர உதவி எண்" else "24x7 Emergency Helpline",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = UnionGoldBright
                )
                Text(
                    text = "1800-425-TNPA2 (86722)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = if (isTamil) "விபத்து & தொழிலாளர் உரிமை உதவிக்கு" else "For Accidents & Labor Safety Support",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                onClick = onCallClick,
                shape = RoundedCornerShape(8.dp),
                color = UnionGoldBright,
                contentColor = Color.Black
            ) {
                Text(
                    text = if (isTamil) "அழைக்க" else "CALL",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

// Background animation composable that simulates a realistic waving union flag using Animatable
@Composable
fun WavingUnionFlagBackground(
    modifier: Modifier = Modifier,
    alpha: Float = 0.35f,
    content: @Composable () -> Unit = {}
) {
    val waveAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            waveAnim.animateTo(
                targetValue = (2 * PI).toFloat(),
                animationSpec = tween(
                    durationMillis = 3600,
                    easing = LinearEasing
                )
            )
            waveAnim.snapTo(0f)
        }
    }

    Box(modifier = modifier) {
        val phase = waveAnim.value
        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height

            if (width <= 0f || height <= 0f) return@Canvas

            val waveAmplitude = height * 0.08f
            val waveFrequency = (2f * PI.toFloat()) / width

            val colors = listOf(
                UnionRedDark.copy(alpha = alpha),
                Color.White.copy(alpha = alpha * 0.85f),
                UnionGoldBright.copy(alpha = alpha)
            )

            val stripeHeight = height / 3f

            for (i in 0..2) {
                val path = Path()
                val topYBase = i * stripeHeight
                val bottomYBase = (i + 1) * stripeHeight

                path.moveTo(0f, topYBase + waveAmplitude * sin(phase + i * 0.4f))

                var x = 0f
                val step = 12f
                while (x <= width) {
                    val waveY = topYBase + waveAmplitude * sin(x * waveFrequency + phase + i * 0.4f)
                    path.lineTo(x, waveY)
                    x += step
                }

                path.lineTo(width, bottomYBase + waveAmplitude * sin(width * waveFrequency + phase + i * 0.4f))

                x = width
                while (x >= 0f) {
                    val waveY = bottomYBase + waveAmplitude * sin(x * waveFrequency + phase + i * 0.4f)
                    path.lineTo(x, waveY)
                    x -= step
                }

                path.close()
                drawPath(path = path, color = colors[i])
            }

            // Depth/fabric shade gradient overlay
            val highlightPath = Path()
            highlightPath.moveTo(0f, 0f)
            var x = 0f
            val step = 12f
            while (x <= width) {
                val waveY = waveAmplitude * sin(x * waveFrequency + phase)
                highlightPath.lineTo(x, waveY)
                x += step
            }
            highlightPath.lineTo(width, height)
            highlightPath.lineTo(0f, height)
            highlightPath.close()

            drawPath(
                path = highlightPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.08f),
                        Color.Black.copy(alpha = 0.15f)
                    )
                )
            )
        }

        content()
    }
}
