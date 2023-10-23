package com.hashone.cropper.settings

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.hashone.cropper.ImageCropper
import com.hashone.cropper.crop
import com.hashone.cropper.model.AspectRatio
import com.hashone.cropper.model.BaseAspectRatioData
import com.hashone.cropper.model.CropOutline
import com.hashone.cropper.model.OutlineType
import com.hashone.cropper.model.aspectRatios
import com.hashone.cropper.state.CropState
import com.hashone.cropper.theme.DefaultBackgroundColor
import com.hashone.cropper.theme.DefaultHandleColor
import com.hashone.cropper.theme.DefaultOverlayColor
import java.io.Serializable

/**
 * Contains the default values used by [ImageCropper]
 */
object CropDefaults {

    /**
     * Properties effect crop behavior that should be passed to [CropState]
     */
    fun properties(
        cropType: CropType = CropType.Static,
        handleSize: Float,
        maxZoom: Float = 20f,
        contentScale: ContentScale = ContentScale.Fit,
        cropOutlineProperty: CropOutlineProperty,
        aspectRatio: AspectRatio = aspectRatios[0].aspectRatio,
        cropAspectRatio: BaseAspectRatioData = aspectRatios[0],
        overlayRatio: Float = 1f,
        pannable: Boolean = true,
        fling: Boolean = false,
        zoomable: Boolean = true,
        rotatable: Boolean = false,
        fixedAspectRatio: Boolean = false,
        requiredSize: IntSize? = null,
        minDimension: IntSize? = null,
        zoom: Float = 1f,
        basePx: Float = 1f,
        basePy: Float = 1f,
    ): CropProperties {
        return CropProperties(
            cropType = cropType,
            handleSize = handleSize,
            contentScale = contentScale,
            cropOutlineProperty = cropOutlineProperty,
            maxZoom = maxZoom,
            aspectRatio = aspectRatio,
            cropAspectRatio = cropAspectRatio,
            overlayRatio = overlayRatio,
            pannable = pannable,
            fling = fling,
            zoomable = zoomable,
            rotatable = rotatable,
            fixedAspectRatio = fixedAspectRatio,
            requiredSize = requiredSize,
            minDimension = minDimension,
            zoom = zoom,
            basePx = basePx,
            basePy = basePy,
        )
    }

    /**
     * Style is cosmetic changes that don't effect how [CropState] behaves because of that
     * none of these properties are passed to [CropState]
     */
    fun style(
        drawOverlay: Boolean = true,
        drawGrid: Boolean = false,
        strokeWidth: Dp = 1.dp,
        overlayColor: Color = DefaultOverlayColor,
        handleColor: Color = DefaultHandleColor,
        backgroundColor: Color = DefaultBackgroundColor
    ): CropStyle {
        return CropStyle(
            drawOverlay = drawOverlay,
            drawGrid = drawGrid,
            strokeWidth = strokeWidth,
            overlayColor = overlayColor,
            handleColor = handleColor,
            backgroundColor = backgroundColor
        )
    }
}

/**
 * Data class for selecting cropper properties. Fields of this class control inner work
 * of [CropState] while some such as [cropType], [aspectRatio], [handleSize]
 * is shared between ui and state.
 */
@Immutable
data class CropProperties internal constructor(
    val cropType: CropType,
    val handleSize: Float,
    var contentScale: ContentScale,
    val cropOutlineProperty: CropOutlineProperty,
    val aspectRatio: AspectRatio,
    val cropAspectRatio: BaseAspectRatioData,
    val overlayRatio: Float,
    val pannable: Boolean,
    val fling: Boolean,
    val rotatable: Boolean,
    val zoomable: Boolean,
    val maxZoom: Float,
    val zoom: Float,
    val fixedAspectRatio: Boolean = false,
    val requiredSize: IntSize? = null,
    val minDimension: IntSize? = null,
    val basePx: Float,
    val basePy: Float,
)

/**
 * Data class for cropper styling only. None of the properties of this class is used
 * by [CropState] or [Modifier.crop]
 */
@Immutable
data class CropStyle internal constructor(
    val drawOverlay: Boolean,
    val drawGrid: Boolean,
    val strokeWidth: Dp,
    val overlayColor: Color,
    val handleColor: Color,
    val backgroundColor: Color,
    val cropTheme: CropTheme = CropTheme.Dark
)

/**
 * Property for passing [CropOutline] between settings UI to [ImageCropper]
 */
@Immutable
data class CropOutlineProperty(
    val outlineType: OutlineType,
    val cropOutline: CropOutline
) : Serializable

/**
 * Light, Dark or system controlled theme
 */
enum class CropTheme {
    Light,
    Dark,
    System
}
