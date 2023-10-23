package com.hashone.cropper.model

import androidx.compose.runtime.Immutable
import com.hashone.cropper.R
import java.io.Serializable

/**
 * Model for drawing title with shape for crop selection menu. Aspect ratio is used
 * for setting overlay in state and UI
 */
@Immutable
data class CropAspectRatio(
    val id: Int,
    val isShape: Boolean = false,
    val outlineType: OutlineType = OutlineType.Rect,
    val cropOutline: CropOutline = ImageMaskOutline2(
        id = 10,
        title = "Custom",
        imageInt = R.drawable.ic_circle_shape
    ),
    var title: String,
    val aspectRatio: AspectRatio = AspectRatio.Original,
    val icons: List<Int> = listOf(),
    val img: Int
) : Serializable


@Immutable
data class RatioData(
    override val id: Int,
    override val title: String,
    override val img: Int,
    override val ratioValue: Float,
) : BaseAspectRatioData, Serializable {
    override val aspectRatio: AspectRatio = AspectRatio(ratioValue)
    override val cropOutline: CropOutline = RectCropShape(id = 0, title = "Rect")
    override val outlineType: OutlineType = OutlineType.Rect
    override val isShape: Boolean = false
}

@Immutable
data class OriginalRatioData(
    override val id: Int,
    override val title: String,
    override val img: Int,
    override val ratioValue: Float = -1F,
) : BaseAspectRatioData, Serializable {
    override val aspectRatio: AspectRatio = AspectRatio.Original
    override val cropOutline: CropOutline = RectCropShape(id = 0, title = "Rect")
    override val outlineType: OutlineType = OutlineType.Rect
    override val isShape: Boolean = false
}

@Immutable
data class ShapeData(
    override val id: Int,
    override val title: String,
    override val img: Int,
    override val ratioValue: Float = 1F,
    var shapeImg: Int = 0
) : BaseAspectRatioData, Serializable {
    override val aspectRatio: AspectRatio = AspectRatio(ratioValue)
    override val cropOutline: CropOutline = ImageMaskOutline2(
        id = id,
        title = title,
        imageInt = shapeImg
    )
    override val outlineType: OutlineType = OutlineType.ImageMask
    override val isShape: Boolean = true
}

/**
 * Value class for containing aspect ratio
 * and [AspectRatio.Original] for comparing
 */
@Immutable
data class AspectRatio(val value: Float) : Serializable {
    companion object {
        val Original = AspectRatio(-1f)
    }
}

interface BaseAspectRatioData {
    val id: Int
    val title: String
    val img: Int
    val ratioValue: Float
    val isShape: Boolean
    val aspectRatio: AspectRatio
    val outlineType: OutlineType
    val cropOutline: CropOutline
}