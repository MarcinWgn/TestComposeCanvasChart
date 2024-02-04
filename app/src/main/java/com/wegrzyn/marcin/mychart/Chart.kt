package com.wegrzyn.marcin.mychart

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun RectChart(modifier: Modifier = Modifier) {

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

    val min = 4.043f
    val max = 4.051f

    val delta = max - min
    val seek = delta / 5

    val list = List(40) { min + Random.nextFloat() * delta }

    Log.d(TAG, "list: $list")
    val length = list.size
    val widthChart = 40.dp * length

    val tm = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .horizontalScroll(state = rememberScrollState())
            .width(widthChart)
            .fillMaxHeight()
            .padding(8.dp)
    ) {

        val leftMargin = 50.dp.toPx()

        val canvasHeight = size.height - 20.dp.toPx()
        val canvasWidth = size.width - leftMargin
        val separate = 5.dp.toPx()
        val w = (canvasWidth - separate) / length - separate

        val hDelta = canvasHeight / delta

        list.forEachIndexed { index, value ->
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