package com.hashone.cropper

import android.app.Activity
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import com.hashone.commons.utils.dpToPx
import com.hashone.cropper.builder.Crop
import com.hashone.cropper.crop.CropAgent
import com.hashone.cropper.draw.DrawingOverlay
import com.hashone.cropper.draw.ImageDrawCanvas
import com.hashone.cropper.image.ImageWithConstraints
import com.hashone.cropper.image.getScaledImageBitmap
import com.hashone.cropper.model.CropAspectRatio
import com.hashone.cropper.model.CropOutline
import com.hashone.cropper.settings.CropDefaults
import com.hashone.cropper.settings.CropProperties
import com.hashone.cropper.settings.CropStyle
import com.hashone.cropper.settings.CropType
import com.hashone.cropper.state.CropState
import com.hashone.cropper.state.DynamicCropState
import com.hashone.cropper.state.rememberCropState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ImageCropper(
    modifier: Modifier = Modifier,
    imageBitmap: ImageBitmap,
    contentDescription: String?,
    cropStyle: CropStyle = CropDefaults.style(),
    cropProperties: CropProperties,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    crop: Boolean = false,
    aspectRationChange: Boolean = false,
    shapeChange: Boolean = false,
    cropBuilder: Crop.Builder,
    mCropData: CropData? = null,
    resetCrop: Boolean = false,
    onCropStart: () -> Unit,
    onCropReset: () -> Unit,
    onHashUpdate: (Boolean) -> Unit,
    onDrawGrid: (DrawScope.(rect: Rect, strokeWidth: Float, color: Color) -> Unit)? = null,
    onCropSuccess: (ImageBitmap?, CropState?) -> Unit,
) {

    var isUpdate by remember { mutableStateOf(false) }

    ImageWithConstraints(
        modifier = modifier.clipToBounds(),
        contentScale = cropProperties.contentScale,
        contentDescription = contentDescription,
        filterQuality = filterQuality,
        imageBitmap = imageBitmap,
        drawImage = false
    ) {

        // No crop operation is applied by ScalableImage so rect points to bounds of original
        // bitmap
        val scaledImageBitmap = getScaledImageBitmap(
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            rect = rect,
            bitmap = imageBitmap,
            contentScale = cropProperties.contentScale,
        )

        // Container Dimensions
        val containerWidthPx = constraints.maxWidth
        val containerHeightPx = constraints.maxHeight

        val containerWidth: Dp
        val containerHeight: Dp

        // Bitmap Dimensions
        val bitmapWidth = scaledImageBitmap.width
        val bitmapHeight = scaledImageBitmap.height

        // Dimensions of Composable that displays Bitmap
        val imageWidthPx: Int
        val imageHeightPx: Int

        with(LocalDensity.current) {
            imageWidthPx = imageWidth.roundToPx()
            imageHeightPx = imageHeight.roundToPx()
            containerWidth = containerWidthPx.toDp()
            containerHeight = containerHeightPx.toDp()
        }

        val zoom = cropProperties.zoom
        val cropType = cropProperties.cropType
        val contentScale = cropProperties.contentScale
        val fixedAspectRatio = cropProperties.fixedAspectRatio
        val cropAspectRatio = cropProperties.cropAspectRatio
        val cropOutline = cropProperties.cropOutlineProperty.cropOutline

        val resetKeys = if (aspectRationChange) getResetKeys(
            scaledImageBitmap,
            imageWidthPx,
            imageHeightPx,
            contentScale,
            cropType,
            fixedAspectRatio,
            zoom
        ) else
            getResetKeys(
                scaledImageBitmap,
                imageWidthPx,
                imageHeightPx,
                contentScale,
                cropType,
                fixedAspectRatio,
                cropAspectRatio,
                zoom
            )

        val cropState = rememberCropState(
            imageSize = IntSize(bitmapWidth, bitmapHeight),
            containerSize = IntSize(containerWidthPx, containerHeightPx),
            drawAreaSize = IntSize(imageWidthPx, imageHeightPx),
            cropProperties = cropProperties,
            keys = resetKeys
        )

        val isHandleTouched by remember(cropState) {
            derivedStateOf {
                cropState is DynamicCropState && handlesTouched(cropState.touchRegion)
            }
        }

        val isInsideTouched by remember(cropState) {
            derivedStateOf {
                insideTouched(cropState.touchRegion)
            }
        }

        val pressedStateColor = remember(cropStyle.backgroundColor) {
            cropStyle.backgroundColor
                .copy(cropStyle.backgroundColor.alpha * .7f)
        }

        val transparentColor by animateColorAsState(
            animationSpec = tween(300, easing = LinearEasing),
            targetValue = if (isHandleTouched) pressedStateColor else cropStyle.backgroundColor,
            label = ""
        )

        if (!isUpdate) {
            isUpdate = isInsideTouched || aspectRationChange || shapeChange
            onHashUpdate(isInsideTouched || aspectRationChange || shapeChange || cropProperties.zoom!=cropState.zoom)
        }

        // Crops image when user invokes crop operation

        Crop(
            cropBuilder,
            crop,
            cropState,
            scaledImageBitmap,
            cropState.cropRect,
            cropOutline,
            onCropStart,
            onCropSuccess,
            cropProperties.requiredSize
        )

        val imageModifier = Modifier
            .size(containerWidth, containerHeight)
            .crop(
                keys = resetKeys,
                cropState = cropState
            )

        LaunchedEffect(key1 = cropProperties) {
            cropState.updateProperties(cropProperties)
        }
        if (!aspectRationChange)
            LaunchedEffect(key1 = cropProperties) {
                delay(200)
                resetWithAnimation(
                    cropState = cropState,
                    pan = Offset(cropProperties.basePx, cropProperties.basePy),
                    zoom = cropProperties.zoom,
                )
            }

        /// Create a MutableTransitionState<Boolean> for the AnimatedVisibility.
        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(100)
            visible = true
        }

        ImageCropper(
            modifier = imageModifier,
            visible = visible,
            imageBitmap = imageBitmap,
            containerWidth = containerWidth,
            containerHeight = containerHeight,
            imageWidthPx = imageWidthPx,
            imageHeightPx = imageHeightPx,
            handleSize = cropProperties.handleSize,
            overlayRect = cropState.overlayRect,
            cropType = cropType,
            cropOutline = cropOutline,
            cropStyle = cropStyle,
            transparentColor = colorResource(id = cropBuilder.screenBuilder.windowBackgroundColor),
            cropBuilder = cropBuilder,
            onDrawGrid = onDrawGrid,
        )
    }
}

internal suspend fun resetWithAnimation(
    cropState: CropState? = null,
    pan: Offset = Offset.Zero,
    zoom: Float = 1f,
    rotation: Float = 0f,
    animationSpec: AnimationSpec<Float> = tween(400)
) = coroutineScope {
    launch { cropState!!.animateZoomTo(zoom, animationSpec) }
    launch { cropState!!.snapPanXtoUpdate(pan.x, animationSpec) }
    launch { cropState!!.snapPanYtoUpdate(pan.y, animationSpec) }
    launch { cropState!!.animateRotationTo(rotation, animationSpec) }
}

@Composable
private fun Crop(
    crop: Boolean,
    cropState: CropState,
    onCropSuccess: (ImageBitmap?, CropState) -> Unit
) {
    LaunchedEffect(crop) {
        if (crop) {
            onCropSuccess(null, cropState)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ImageCropper(
    modifier: Modifier,
    visible: Boolean,
    imageBitmap: ImageBitmap,
    containerWidth: Dp,
    containerHeight: Dp,
    imageWidthPx: Int,
    imageHeightPx: Int,
    handleSize: Float,
    cropType: CropType,
    cropOutline: CropOutline,
    cropStyle: CropStyle,
    overlayRect: Rect,
    transparentColor: Color,
    cropBuilder: Crop.Builder,
    onDrawGrid: (DrawScope.(rect: Rect, strokeWidth: Float, color: Color) -> Unit)?,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(transparentColor)
    ) {

        AnimatedVisibility(
            visible = visible,
            enter = scaleIn(tween(500))
        ) {

            ImageCropperImpl(
                modifier = modifier,
                imageBitmap = imageBitmap,
                containerWidth = containerWidth,
                containerHeight = containerHeight,
                imageWidthPx = imageWidthPx,
                imageHeightPx = imageHeightPx,
                cropType = cropType,
                cropOutline = cropOutline,
                handleSize = handleSize,
                cropStyle = cropStyle,
                rectOverlay = overlayRect,
                transparentColor = transparentColor,
                cropBuilder = cropBuilder,
                onDrawGrid = onDrawGrid,
            )
        }
    }
}

@Composable
private fun ImageCropperImpl(
    modifier: Modifier,
    imageBitmap: ImageBitmap,
    containerWidth: Dp,
    containerHeight: Dp,
    imageWidthPx: Int,
    imageHeightPx: Int,
    cropType: CropType,
    cropOutline: CropOutline,
    handleSize: Float,
    cropStyle: CropStyle,
    transparentColor: Color,
    rectOverlay: Rect,
    cropBuilder: Crop.Builder,
    onDrawGrid: (DrawScope.(rect: Rect, strokeWidth: Float, color: Color) -> Unit)?,
) {

    Box(contentAlignment = Alignment.Center) {

        // Draw Image
        ImageDrawCanvas(
            modifier = modifier,
            imageBitmap = imageBitmap,
            imageWidth = imageWidthPx,
            imageHeight = imageHeightPx
        )

        val drawOverlay = cropStyle.drawOverlay

        val drawGrid = cropStyle.drawGrid
        val overlayColor = cropStyle.overlayColor
        val handleColor = cropStyle.handleColor
        val drawHandles = cropType == CropType.Static
        val strokeWidth = cropStyle.strokeWidth

        DrawingOverlay(
            modifier = Modifier.size(containerWidth, containerHeight),
            drawOverlay = drawOverlay,
            rect = rectOverlay,
            cropOutline = cropOutline,
            drawGrid = drawGrid,
            overlayColor = colorResource(id = cropBuilder.screenBuilder.cropOuterBorderColor),
            handleColor = handleColor,
            strokeWidth = strokeWidth,
            drawHandles = drawHandles,
            handleSize = handleSize,
            transparentColor = transparentColor,
            cropBuilder = cropBuilder,
            onDrawGrid = onDrawGrid,
        )

    }
}

@Composable
private fun Crop(
    cropBuilder: Crop.Builder,
    crop: Boolean,
    cropState: CropState,
    scaledImageBitmap: ImageBitmap,
    cropRect: Rect,
    cropOutline: CropOutline,
    onCropStart: () -> Unit,
    onCropSuccess: (ImageBitmap?, CropState?) -> Unit,
    requiredSize: IntSize?,
) {

    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val activity = (LocalContext.current as? Activity)

    // Crop Agent is responsible for cropping image
    val cropAgent = remember { CropAgent() }

    LaunchedEffect(crop) {
        if (crop) {
            withContext(Dispatchers.IO) {
                //This dispatcher is optimized to perform disk or network I/O outside of the main thread. Examples include using the Room component, reading from or writing to files, and running any network operations.
                flow {
                    val localRect = Rect(
                        cropRect.left + dpToPx(cropBuilder.screenBuilder.borderSpacing / 2f),
                        cropRect.top + dpToPx(cropBuilder.screenBuilder.borderSpacing / 2f),
                        cropRect.right,
                        cropRect.bottom
                    )
                    val croppedImageBitmap = cropAgent.crop(
                        scaledImageBitmap,
                        localRect,
                        cropOutline,
                        layoutDirection,
                        density,
                        activity
                    )
                    if (requiredSize != null) {
                        emit(
                            cropAgent.resize(
                                croppedImageBitmap,
                                (croppedImageBitmap.width * cropState.zoom).toInt(),
                                (croppedImageBitmap.height * cropState.zoom).toInt(),
                            )
                        )
                    } else {
                        emit(croppedImageBitmap)
                    }
                }
                    .flowOn(Dispatchers.Default)
                    .onStart {
                        onCropStart()
                    }
                    .onEach {
                        onCropSuccess(it, cropState)
                    }
                    .launchIn(this)
            }

        }
    }
}

@Composable
private fun getResetKeys(
    scaledImageBitmap: ImageBitmap,
    imageWidthPx: Int,
    imageHeightPx: Int,
    contentScale: ContentScale,
    cropType: CropType,
    fixedAspectRatio: Boolean,
    cropAspectRatio: CropAspectRatio,
    zoom: Float
) = remember(
    scaledImageBitmap,
    imageWidthPx,
    imageHeightPx,
    contentScale,
    cropType,
    fixedAspectRatio,
    cropAspectRatio,
    zoom
) {
    arrayOf(
        scaledImageBitmap,
        imageWidthPx,
        imageHeightPx,
        contentScale,
        cropType,
        fixedAspectRatio,
        cropAspectRatio,
        zoom
    )
}

@Composable
private fun getResetKeys(
    scaledImageBitmap: ImageBitmap,
    imageWidthPx: Int,
    imageHeightPx: Int,
    contentScale: ContentScale,
    cropType: CropType,
    fixedAspectRatio: Boolean,
    zoom: Float
) = remember(
    scaledImageBitmap,
    imageWidthPx,
    imageHeightPx,
    contentScale,
    cropType,
    fixedAspectRatio,
    zoom
) {
    arrayOf(
        scaledImageBitmap,
        imageWidthPx,
        imageHeightPx,
        contentScale,
        cropType,
        fixedAspectRatio,
        zoom
    )
}