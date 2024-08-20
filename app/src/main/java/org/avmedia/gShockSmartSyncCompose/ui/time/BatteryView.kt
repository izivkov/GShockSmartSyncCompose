package org.avmedia.gShockSmartSyncCompose.ui.time

import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gshockapi.WatchInfo.hasBatteryLevel

@Composable
fun BatteryView(
    modifier: Modifier = Modifier,
    batteryLevel: Int = 0,  // Percentage of battery
    hasBatteryLevel: Boolean = true,
    isConnected: Boolean = false,
    isNormalButtonPressed: Boolean = false,
    onBatteryLevelUpdated: (Int) -> Unit = {}
) {
    if (!hasBatteryLevel) {
        return
    }

    // Initialize required variables
    var radius by remember { mutableFloatStateOf(0f) }
    var borderStroke by remember { mutableFloatStateOf(0f) }
    val topRect = android.graphics.Rect()
    val borderRect = RectF()
    val percentRect = RectF()
    var percentRectTopMin by remember { mutableFloatStateOf(0f) }

    // Load stripe bitmap
    val context = LocalContext.current

    fun getBitmap(
        drawableId: Int,
        desireWidth: Int? = null,
        desireHeight: Int? = null
    ): Bitmap? {
        val drawable = AppCompatResources.getDrawable(context, drawableId) ?: return null
        val bitmap = Bitmap.createBitmap(
            desireWidth ?: drawable.intrinsicWidth,
            desireHeight ?: drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    val stripeBitmap = remember { getBitmap(R.drawable.stripes) }

    Canvas(modifier = modifier) {
        val measureWidth = size.width
        val measureHeight = size.height

        radius = borderStroke / 2
        borderStroke = (4 * measureWidth) / 50

        // Top
        val topLeft = measureWidth * ((100 - 50) / 2) / 100
        val topRight = measureWidth - topLeft
        val topBottom = 8 * measureHeight / 100
        topRect.set(topLeft.toInt(), 0, topRight.toInt(), topBottom.toInt())

        // Border
        val borderLeft = borderStroke / 2
        val borderTop = topBottom + borderStroke / 2
        val borderRight = measureWidth - borderStroke / 2
        val borderBottom = measureHeight - borderStroke / 2
        borderRect.set(borderLeft, borderTop, borderRight, borderBottom)

        // Progress
        val progressLeft = borderStroke
        percentRectTopMin = topBottom + borderStroke
        val progressRight = measureWidth - borderStroke
        val progressBottom = measureHeight - borderStroke
        percentRect.set(progressLeft, percentRectTopMin, progressRight, progressBottom)

        // Draw the top
        drawRoundRect(
            color = Color.Gray,
            topLeft = Offset(topRect.left.toFloat(), topRect.top.toFloat()),
            size = Size(topRect.width().toFloat(), topRect.height().toFloat()),
            cornerRadius = CornerRadius(radius, radius)
        )

        // Draw the border
        drawRoundRect(
            color = Color.Gray,
            topLeft = Offset(borderRect.left, borderRect.top),
            size = Size(borderRect.width(), borderRect.height()),
            style = Stroke(width = borderStroke)
        )

        // Draw the progress
        val progressHeight =
            percentRect.bottom - (percentRect.top + (percentRect.bottom - percentRect.top) * batteryLevel / 100)
        val pctRect = RectF(
            percentRect.left,
            percentRect.top + progressHeight,
            percentRect.right,
            percentRect.bottom
        )

        stripeBitmap?.let {
            drawImage(
                it.asImageBitmap(),
                dstSize = IntSize(pctRect.width().toInt(), pctRect.height().toInt())
            )
        }
    }

    // Update battery level
    if (isConnected && isNormalButtonPressed) {
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                onBatteryLevelUpdated(api().getBatteryLevel())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBatteryView() {
    BatteryView(
        modifier = Modifier.size(50.dp),
        batteryLevel = 75,
        hasBatteryLevel = hasBatteryLevel,
        isConnected = true,
        isNormalButtonPressed = true,
        onBatteryLevelUpdated = { percent -> /* Handle updated battery level */ }
    )
}