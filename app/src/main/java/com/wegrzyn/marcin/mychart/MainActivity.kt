package com.wegrzyn.marcin.mychart

import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.wegrzyn.marcin.mychart.ui.theme.MyChartTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntent(intent)
        addDynamicShortcut()
        setContent {
            MyChartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        LineChart()
                        RectChart()
                    }

                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun addDynamicShortcut(){
        val shortcut = ShortcutInfoCompat.Builder(applicationContext, "dynamic")
            .setShortLabel("show toast")
            .setIcon(IconCompat.createWithResource(applicationContext,R.drawable.message_24))
            .setIntent(Intent(applicationContext, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                putExtra("shortcut_id","Hello")
            })
            .build()
        val shortcut2 = ShortcutInfoCompat.Builder(applicationContext, "dynamic")
            .setShortLabel("show toast")
            .setIcon(IconCompat.createWithResource(applicationContext,R.drawable.message_24))
            .setIntent(Intent(applicationContext, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                putExtra("shortcut_id","Hello2")
            })
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(applicationContext,shortcut)
    }
    private fun handleIntent(intent: Intent?){
        intent?.let {
            val msg =intent.getStringExtra("shortcut_id")
            msg?.let {
                Toast.makeText(applicationContext,it, Toast.LENGTH_SHORT).show()
            }

        }
    }
}


@Composable
fun LineChart() {

    var running by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val points = remember {
        mutableStateListOf<PointF>(PointF(0f, 0f))
    }
    val len = 100
    val max = 500

    fun add() {
        if (points.size < len) {
            val p2 = PointF(points.size.toFloat(), Random.nextInt(0, max).toFloat())
            points.add(p2)
            Log.d(TAG, "size: ${points.size}")
        } else {
            points.removeFirst()
            points.forEach {
                it.x -= 1
            }
            Log.d(TAG, "size: ${points.size}")
        }

    }

    fun clear() {
        points.clear()
    }

    fun doData() {
        scope.launch {
            while (running) {
                add()
                delay(200)
            }
        }.start()
    }
    Column {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(MaterialTheme.colorScheme.inversePrimary)
        )
        {

            val offsetList = mutableListOf<Offset>()
            points.forEachIndexed { i, point ->
                offsetList.add(
                    index = i,
                    Offset(
                        x = point.x / (len) * size.width,
                        y = size.height - (point.y / max * size.height)
                    )
                )
            }

            drawPoints(
                strokeWidth = 2.dp.toPx(),
                color = Color.Red,
                points = offsetList,
                pointMode = PointMode.Polygon
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(modifier = Modifier.padding(8.dp),
                onClick = {
                    add()
                }) {
                Text(text = "add")
            }
            Button(modifier = Modifier.padding(8.dp), onClick = {
                clear()
            }) {
                Text(text = "clear")

            }
            Button(modifier = Modifier.padding(8.dp), onClick = {
                running = !running
                doData()
            }) {
                if (running) {
                    Text(text = "stop")
                } else {
                    Text(text = "start")
                }
            }
        }
    }
}

val TAG = "TESTTAG"