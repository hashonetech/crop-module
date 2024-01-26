package com.hashone.cropper.draw

import android.app.Activity
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.hashone.commons.utils.dpToPx
import com.hashone.cropper.builder.Crop
import com.hashone.cropper.model.CropImageMask
import com.hashone.cropper.model.CropImageMask2
import com.hashone.cropper.model.CropOutline
import com.hashone.cropper.model.CropPath
import com.hashone.cropper.model.CropShape
import com.hashone.cropper.util.drawGrid
import com.hashone.cropper.util.drawWithLayer
import com.hashone.cropper.util.scaleAndTranslatePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

/**
 * Draw overlay composed of 9 rectangles. When [drawHandles]
 * is set draw handles for changing drawing rectangle
 */
@Composable
internal fun DrawingOverlay(
    modifier: Modifier,
    drawOverlay: Boolean,
    rect: Rect,
    cropOutline: CropOutline,
    drawGrid: Boolean,
    transparentColor: Color,
    overlayColor: Color,
    handleColor: Color,
    strokeWidth: Dp,
    drawHandles: Boolean,
    handleSize: Float,
    cropBuilder: Crop.Builder,
    onDrawGrid: (DrawScope.(rect: Rect, strokeWidth: Float, color: Color) -> Unit)?
) {
    val activity = (LocalContext.current as? Activity)

    val density = LocalDensity.current
    val layoutDirection: LayoutDirection = LocalLayoutDirection.current

    val strokeWidthPx = LocalDensity.current.run { strokeWidth.toPx() }

    val pathHandles = remember {
        Path()
    }
    var isMaskBitmapReady by remember { mutableStateOf(false) }
    var maskImage by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(cropOutline) {
        withContext(Dispatchers.IO) {
            if (cropOutline is CropImageMask2) {
                maskImage = Glide.with(activity!!)
                    .asBitmap()
                    .load(cropOutline.imageInt)
                    .apply(RequestOptions().override(Target.SIZE_ORIGINAL))
                    .submit().get().asImageBitmap()
                isMaskBitmapReady = true
            }
        }
    }

    val newRect = Rect(
        rect.left,
        rect.top,
        rect.right,
        rect.bottom
    )
    when (cropOutline) {
        is CropShape -> {

            val outline = remember(rect, cropOutline) {
                val localRect = Rect(
                    rect.left + dpToPx(cropBuilder.screenBuilder.borderSpacing),
                    rect.top + dpToPx(cropBuilder.screenBuilder.borderSpacing),
                    rect.right - dpToPx(cropBuilder.screenBuilder.borderSpacing),
                    rect.bottom - dpToPx(cropBuilder.screenBuilder.borderSpacing)
                )
                cropOutline.shape.createOutline(localRect.size, layoutDirection, density)
            }

            DrawingOverlayImpl(
                modifier = modifier,
                drawOverlay = drawOverlay,
                rect = newRect,
                drawGrid = drawGrid,
                transparentColor = transparentColor,
                overlayColor = overlayColor,
                handleColor = handleColor,
                strokeWidth = strokeWidthPx,
                drawHandles = drawHandles,
                handleSize = handleSize,
                pathHandles = pathHandles,
                outline = outline,
                cropBuilder = cropBuilder,
                onDrawGrid = onDrawGrid,
            )
        }

        is CropPath -> {
            val path = remember(rect, cropOutline) {
                Path().apply {
                    addPath(cropOutline.path)
                    scaleAndTranslatePath(rect.width, rect.height)
                }
            }
                DrawingOverlayImpl(
                    modifier = modifier,
                    drawOverlay = drawOverlay,
                    rect = newRect,
                    drawGrid = drawGrid,
                    transparentColor = transparentColor,
                    overlayColor = overlayColor,
                    handleColor = handleColor,
                    strokeWidth = strokeWidthPx,
                    drawHandles = drawHandles,
                    handleSize = handleSize,
                    pathHandles = pathHandles,
                    path = path,
                    cropBuilder = cropBuilder,
                    onDrawGrid = onDrawGrid,
                )

        }

        is CropImageMask -> {
            val imageBitmap = cropOutline.image

            DrawingOverlayImpl(
                modifier = modifier,
                drawOverlay = drawOverlay,
                rect = rect,
                drawGrid = drawGrid,
                transparentColor = transparentColor,
                overlayColor = overlayColor,
                handleColor = handleColor,
                strokeWidth = strokeWidthPx,
                drawHandles = drawHandles,
                handleSize = handleSize,
                pathHandles = pathHandles,
                image = imageBitmap,
                cropBuilder = cropBuilder,
                onDrawGrid = onDrawGrid,
            )
        }


        is CropImageMask2 -> {

            if (isMaskBitmapReady && maskImage != null) {
                DrawingOverlayImpl(
                    modifier = modifier,
                    drawOverlay = drawOverlay,
                    rect = newRect,
                    drawGrid = drawGrid,
                    transparentColor = transparentColor,
                    overlayColor = overlayColor,
                    handleColor = handleColor,
                    strokeWidth = strokeWidthPx,
                    drawHandles = drawHandles,
                    handleSize = handleSize,
                    pathHandles = pathHandles,
                    image = maskImage!!,
                    cropBuilder = cropBuilder,
                    onDrawGrid = onDrawGrid,
                )
            }
        }


    }
}

@Composable
private fun DrawingOverlayImpl(
    modifier: Modifier,
    drawOverlay: Boolean,
    rect: Rect,
    drawGrid: Boolean,
    transparentColor: Color,
    overlayColor: Color,
    handleColor: Color,
    strokeWidth: Float,
    drawHandles: Boolean,
    handleSize: Float,
    pathHandles: Path,
    outline: Outline,
    cropBuilder: Crop.Builder,
    onDrawGrid: (DrawScope.(rect: Rect, strokeWidth: Float, color: Color) -> Unit)?,
) {
    Canvas(modifier = modifier) {
        drawOverlay(
            drawOverlay,
            rect,
            drawGrid,
            transparentColor,
            overlayColor,
            handleColor,
            strokeWidth,
            drawHandles,
            handleSize,
            pathHandles,
            cropBuilder,
            onDrawGrid,
        ) {
            drawCropOutline(outline = outline)
        }
    }
}

@Composable
private fun DrawingOverlayImpl(
    modifier: Modifier,
    drawOverlay: Boolean,
    rect: Rect,
    drawGrid: Boolean,
    transparentColor: Color,
    overlayColor: Color,
    handleColor: Color,
    strokeWidth: Float,
    drawHandles: Boolean,
    handleSize: Float,
    pathHandles: Path,
    path: Path,
    cropBuilder: Crop.Builder,
    onDrawGrid: (DrawScope.(rect: Rect, strokeWidth: Float, color: Color) -> Unit)?,
) {
    Canvas(modifier = modifier) {
        drawOverlay(
            drawOverlay,
            rect,
            drawGrid,
            transparentColor,
            overlayColor,
            handleColor,
            strokeWidth,
            drawHandles,
            handleSize,
            pathHandles,
            cropBuilder,
            onDrawGrid,
        ) {
            drawCropPath(path)
        }
    }
}

@Composable
private fun DrawingOverlayImpl(
    modifier: Modifier,
    drawOverlay: Boolean,
    rect: Rect,
    drawGrid: Boolean,
    transparentColor: Color,
    overlayColor: Color,
    handleColor: Color,
    strokeWidth: Float,
    drawHandles: Boolean,
    handleSize: Float,
    pathHandles: Path,
    image: ImageBitmap,
    cropBuilder: Crop.Builder,
    onDrawGrid: (DrawScope.(rect: Rect, strokeWidth: Float, color: Color) -> Unit)?,
) {
    Canvas(modifier = modifier) {
        drawOverlay(
            drawOverlay,
            rect,
            drawGrid,
            transparentColor,
            overlayColor,
            handleColor,
            strokeWidth,
            drawHandles,
            handleSize,
            pathHandles,
            cropBuilder,
            onDrawGrid,
        ) {

            drawCropImage(rect, image, cropBuilder)
        }
    }
}

private fun DrawScope.drawOverlay(
    drawOverlay: Boolean,
    rect: Rect,
    drawGrid: Boolean,
    transparentColor: Color,
    overlayColor: Color,
    handleColor: Color,
    strokeWidth: Float,
    drawHandles: Boolean,
    handleSize: Float,
    pathHandles: Path,
    cropBuilder: Crop.Builder,
    onDrawGrid: (DrawScope.(rect: Rect, strokeWidth: Float, color: Color) -> Unit)?,
    drawBlock: DrawScope.() -> Unit,
) {
    drawWithLayer {

        // Destination
        drawRect(transparentColor)
        // Source
        translate(
            left = rect.left + dpToPx(cropBuilder.screenBuilder.borderSpacing),
            top = rect.top + dpToPx(cropBuilder.screenBuilder.borderSpacing)
        ) {
            drawBlock()
        }

        if (drawGrid) {
            if (onDrawGrid != null) {
                onDrawGrid(rect, strokeWidth, overlayColor)
            } else {
                drawGrid(
                    rect = rect,
                    strokeWidth = strokeWidth,
                    color = overlayColor,
                )
            }
        }
    }

    if (drawOverlay) {
        drawRect(
            topLeft = Offset(rect.topLeft.x, rect.topLeft.y),
            size = Size(rect.size.width, rect.size.height),
            color = overlayColor,
            style = Stroke(width = strokeWidth)
        )
    }
}

private fun DrawScope.drawCropImage(
    rect: Rect,
    imageBitmap: ImageBitmap,
    cropBuilder: Crop.Builder,
    blendMode: BlendMode = BlendMode.DstOut
) {
    drawImage(
        image = imageBitmap,
        dstSize = IntSize(
            rect.size.width.toInt() - dpToPx(cropBuilder.screenBuilder.borderSpacing * 2f).roundToInt(),
            rect.size.height.toInt() - dpToPx(cropBuilder.screenBuilder.borderSpacing * 2f).roundToInt()
        ),
        blendMode = blendMode
    )
}

private fun DrawScope.drawCropOutline(
    outline: Outline,
    blendMode: BlendMode = BlendMode.SrcOut
) {
    drawOutline(
        outline = outline,
        color = Color.Transparent,
        blendMode = blendMode
    )
}

private fun DrawScope.drawCropPath(
    path: Path,
    blendMode: BlendMode = BlendMode.SrcOut
) {
    drawPath(
        path = path,
        color = Color.Transparent,
        blendMode = blendMode
    )
}

private fun Path.updateHandlePath(
    rect: Rect,
    handleSize: Float
) {
    if (rect != Rect.Zero) {
        // Top left lines
        moveTo(rect.topLeft.x, rect.topLeft.y + handleSize)
        lineTo(rect.topLeft.x, rect.topLeft.y)
        lineTo(rect.topLeft.x + handleSize, rect.topLeft.y)

        // Top right lines
        moveTo(rect.topRight.x - handleSize, rect.topRight.y)
        lineTo(rect.topRight.x, rect.topRight.y)
        lineTo(rect.topRight.x, rect.topRight.y + handleSize)

        // Bottom right lines
        moveTo(rect.bottomRight.x, rect.bottomRight.y - handleSize)
        lineTo(rect.bottomRight.x, rect.bottomRight.y)
        lineTo(rect.bottomRight.x - handleSize, rect.bottomRight.y)

        // Bottom left lines
        moveTo(rect.bottomLeft.x + handleSize, rect.bottomLeft.y)
        lineTo(rect.bottomLeft.x, rect.bottomLeft.y)
        lineTo(rect.bottomLeft.x, rect.bottomLeft.y - handleSize)
    }
}
