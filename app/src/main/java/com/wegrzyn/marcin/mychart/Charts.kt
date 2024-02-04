package com.wegrzyn.marcin.mychart

import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun RectChart(modifier: Modifier = Modifier, items: List<Float>) {

    val dark = isSystemInDarkTheme()

    val textStyle = if (dark) {
        TextStyle(color = Color.White)
    } else {
        TextStyle()
    }
    val colorLine = if (dark) {
        Color.White
    } else {
        Color.Black
    }


    val min = items.minOrNull() ?: 0f
    val max = items.maxOrNull() ?: 0f

    val delta = max - min
    val seek = delta / 5

    Log.d(TAG, "list: $items")
    val length = items.size
    val widthChart = 40.dp * length

    val tm = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .horizontalScroll(state = rememberScrollState())
            .width(widthChart)
            .fillMaxHeight()
            .padding(8.dp)
    ) {

        val leftMargin = 70.dp.toPx()

        val canvasHeight = size.height - 20.dp.toPx()
        val canvasWidth = size.width - leftMargin
        val separate = 5.dp.toPx()
        val w = (canvasWidth - separate) / length - separate

        val hDelta = canvasHeight / delta

        items.forEachIndexed { index, value ->
            val h = hDelta * (value - min)
            val x = leftMargin + 5f + separate + index * (w + separate)
            drawRoundRect(
                cornerRadius = CornerRadius(x = 10.dp.toPx()),
                brush = Brush.verticalGradient(colors = listOf(Color.Cyan, Color.Magenta)),
                size = Size(width = w, height = h),
                topLeft = Offset(y = canvasHeight - h, x = x)
            )

            drawText(
                style = textStyle,
                textMeasurer = tm,
                text = (index + 1).toString(),
                topLeft = Offset(y = size.height - 20.dp.toPx(), x = x + w / 3)
            )
        }


        drawLine(
            start = Offset(leftMargin + 5f, 0f),
            end = Offset(leftMargin + 5f, canvasHeight),
            color = colorLine,
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            start = Offset(x = leftMargin + 5f, y = canvasHeight),
            end = Offset(x = leftMargin + canvasWidth, y = canvasHeight),
            color = colorLine,
            strokeWidth = 2.dp.toPx()
        )
        repeat(6) {
            val yLabelValue = min + it * seek
            val yLabel = yLabelValue.toString()
            val y = (canvasHeight - canvasHeight / 5 * it)
            drawLine(
                start = Offset(leftMargin + 5f, y),
                end = Offset(leftMargin + canvasWidth, y),
                color = colorLine,
                pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(10f, 20f))
            )
            if (it == 0 || it == 5) {
                drawText(
                    style = textStyle,
                    textMeasurer = tm,
                    text = yLabel,
                    topLeft = Offset(x = 0f, y = y - 10.dp.toPx())
                )
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