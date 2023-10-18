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
    val cropOutline: CropOutline = ImageMaskOutline2(id = 10, title = "Custom", imageInt = R.drawable.ic_circle_shape),
//    val cropOutline: CropOutline = RectCropShape(id = 0, title = "Rect"),
    var title: String,
    val aspectRatio: AspectRatio = AspectRatio.Original,
    val icons: List<Int> = listOf(),
    val img: Int
):Serializable

/**
 * Value class for containing aspect ratio
 * and [AspectRatio.Original] for comparing
 */
@Immutable
data class AspectRatio(val value: Float):Serializable {
    companion object {
        val Original = AspectRatio(-1f)
    }
}