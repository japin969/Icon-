package com.test.iconcustom

import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import java.io.File

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      var selected by remember { mutableStateOf(0) }
      var fpsText by remember { mutableStateOf("Test Lag...") }
      var lastTime by remember { mutableStateOf(System.nanoTime()) }
      var frames by remember { mutableStateOf(0) }

      // FPS counter buat test lag
      LaunchedEffect(Unit) {
        while(true) {
          kotlinx.coroutines.delay(1000)
          val now = System.nanoTime()
          val fps = frames * 1_000_000_000L / (now - lastTime)
          fpsText = "FPS: $fps | Frames: $frames | ${if(fps >= 55) "✅ LANCAR" else if(fps >= 30) "⚠️ MAYAN" else "❌ LAG"}"
          frames = 0; lastTime = now
        }
      }

      Box(Modifier.fillMaxSize().background(Color(0xFFEEF2FF))) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
          Text("ICON_CUSTOM - TEST APK", fontSize = 22.sp, fontWeight = FontWeight.Black)
          Text(fpsText, fontSize = 14.sp, color = Color.Gray)
          Spacer(Modifier.height(12.dp))
          Text("Ini project TEST, bukan utama.\nKalo lag di sini, berarti di utama juga lag.\nKalo lancar di sini, aman gas ke utama.", fontSize = 13.sp, color = Color.DarkGray)
          Spacer(Modifier.height(12.dp))
          // Card dummy biar ada scroll load
          repeat(6) {
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White).padding(16.dp)) {
              Text("Card Dummy $it - Scroll test", fontSize = 14.sp)
            }
            Spacer(Modifier.height(10.dp))
          }
        }

        // ===== NAV TEST - TANPA LIPAT =====
        Row(
          Modifier.align(Alignment.BottomCenter).padding(start = 14.dp, end = 14.dp, bottom = 20.dp).fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // NAV 1 LEMBAR UTUH + VIDEO BG
          Box(
            Modifier.weight(1f).height(64.dp).clip(RoundedCornerShape(22.dp)).background(Color.Black)
          ) {
            // VIDEO BG - dari assets, bukan dari Download
            AndroidView(
              factory = { ctx ->
                VideoView(ctx).apply {
                  val f = File(ctx.filesDir, "nav_bg.mp4")
                  if (!f.exists()) {
                    ctx.assets.open("nav_bg.mp4").use { input ->
                      f.outputStream().use { input.copyTo(it) }
                    }
                  }
                  setVideoURI(Uri.fromFile(f))
                  setOnPreparedListener { mp ->
                    mp.isLooping = true
                    mp.setVolume(0f, 0f)
                    start()
                  }
                }
              },
              modifier = Modifier.fillMaxSize()
            )
            // Overlay biar ikon kebaca
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.18f)))

            // 5 Item - 1 ROW, GA ADA LIPAT
            Row(Modifier.fillMaxSize().padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
              val items = listOf("Chat","Calls","Music","Hub","Settings")
              items.forEachIndexed { i, label ->
                Box(
                  Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(16.dp))
                    .background(if (selected == i) Color.White.copy(alpha = 0.28f) else Color.Transparent)
                    .clickable { selected = i; frames += 5 },
                  contentAlignment = Alignment.Center
                ) {
                  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                      when(i){0->"💬";1->"📞";2->"🎵";3->"⊞";4->"⚙️"; else->""},
                      fontSize = 20.sp
                    )
                    Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                  }
                }
              }
            }
          }

          Spacer(Modifier.width(4.dp))

          // ===== AI ∞ NGALIR, BUKAN MUTER =====
          // Icon DIAM, garisnya yang jalan pakai dash + phase
          val infinite = rememberInfiniteTransition(label = "flow")
          val phase by infinite.animateFloat(0f, 40f, infiniteRepeatable(tween(1100, easing = LinearEasing), RepeatMode.Restart), label = "phase")
          // Hitung FPS juga dari sini
          LaunchedEffect(phase) { frames++ }

          Box(
            Modifier.size(64.dp).clip(CircleShape).clickable { frames += 10 },
            contentAlignment = Alignment.Center
          ) {
            Canvas(Modifier.size(64.dp)) {
              val path = Path().apply {
                moveTo(size.width * 0.20f, size.height * 0.50f)
                cubicTo(size.width*0.20f, size.height*0.31f, size.width*0.39f, size.height*0.29f, size.width*0.50f, size.height*0.50f)
                cubicTo(size.width*0.60f, size.height*0.70f, size.width*0.79f, size.height*0.68f, size.width*0.79f, size.height*0.50f)
                cubicTo(size.width*0.79f, size.height*0.31f, size.width*0.60f, size.height*0.29f, size.width*0.50f, size.height*0.50f)
                cubicTo(size.width*0.39f, size.height*0.70f, size.width*0.20f, size.height*0.68f, size.width*0.20f, size.height*0.50f)
                close()
              }
              // Layer 1: base samar, DIAM
              drawPath(path, Color.Black.copy(alpha = 0.25f), style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
              // Layer 2: flow ngalir - dash jalan, icon TIDAK muter
              drawPath(
                path, Color.Black,
                style = Stroke(
                  width = 3.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round,
                  pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 22f), phase)
                )
              )
            }
          }
        }
      }
    }
  }
}
