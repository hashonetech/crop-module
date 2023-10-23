package com.hashone.cropper.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntSize
import com.hashone.cropper.model.BaseAspectRatioData
import com.hashone.cropper.settings.CropProperties
import com.hashone.cropper.settings.CropType


/**
 * Create and [remember] the [CropState] based on the currently appropriate transform
 * configuration to allow changing pan, zoom, and rotation.
 * @param imageSize size of the **Bitmap**
 * @param containerSize size of the Composable that draws **Bitmap**
 * @param cropProperties wrapper class that contains crop state properties such as
 * crop type,
 * @param keys are used to reset remember block to initial calculations. This can be used
 * when image, contentScale or any property changes which requires values to be reset to initial
 * values
 */
@Composable
fun rememberCropState(
    imageSize: IntSize,
    containerSize: IntSize,
    drawAreaSize: IntSize,
    cropProperties: CropProperties,
    vararg keys: Any?
): CropState {

    // Properties of crop state
    val handleSize = cropProperties.handleSize
    val cropType = cropProperties.cropType
    val aspectRatio = cropProperties.aspectRatio
    val overlayRatio = cropProperties.overlayRatio
    val maxZoom = cropProperties.maxZoom
    val fling = cropProperties.fling
    val zoomable = cropProperties.zoomable
    val pannable = cropProperties.pannable
    val rotatable = cropProperties.rotatable
    val fixedAspectRatio = cropProperties.fixedAspectRatio
    val minDimension = cropProperties.minDimension
    val cropOutlineProperty = cropProperties.cropOutlineProperty
    val cropAspectRatio: BaseAspectRatioData = cropProperties.cropAspectRatio

    return remember(*keys) {
        when (cropType) {
            CropType.Static -> {
                StaticCropState(
                    imageSize = imageSize,
                    containerSize = containerSize,
                    drawAreaSize = drawAreaSize,
                    aspectRatio = aspectRatio,
                    overlayRatio = overlayRatio,
                    maxZoom = maxZoom,
                    fling = fling,
                    zoomable = zoomable,
                    pannable = pannable,
                    rotatable = rotatable,
                    limitPan = false,
                    cropOutlineProperty = cropOutlineProperty,
                    cropAspectRatio = cropAspectRatio,
                    handleSize = handleSize,
                )
            }
            else -> {

                DynamicCropState(
                    imageSize = imageSize,
                    containerSize = containerSize,
                    drawAreaSize = drawAreaSize,
                    aspectRatio = aspectRatio,
                    overlayRatio = overlayRatio,
                    maxZoom = maxZoom,
                    handleSize = handleSize,
                    fling = fling,
                    zoomable = zoomable,
                    pannable = pannable,
                    rotatable = rotatable,
                    limitPan = true,
                    fixedAspectRatio = fixedAspectRatio,
                    minDimension = minDimension,
                    cropOutlineProperty = cropOutlineProperty,
                    cropAspectRatio = cropAspectRatio,
                )
            }
        }
    }
}
