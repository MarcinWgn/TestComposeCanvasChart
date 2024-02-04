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

        val min = 5
        val max = 10
        val delta = max - min
        val size = 50

        val list = List(size = size) { min + Random.nextFloat() * delta }

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
                        RectChart(items = list)
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




val TAG = "TESTTAG"