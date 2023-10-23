package com.hashone.cropper

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import com.hashone.cropper.model.AspectRatio
import com.hashone.cropper.model.BaseAspectRatioData
import com.hashone.cropper.model.OriginalRatioData
import com.hashone.cropper.model.OutlineType
import com.hashone.cropper.model.RectCropShape
import com.hashone.cropper.model.aspectRatios
import com.hashone.cropper.settings.CropOutlineProperty

data class CropData(
    var drawRect: Rect = Rect.Zero,
    var overlayRect: Rect = Rect.Zero,
    var cropRect: Rect = Rect.Zero,
    var containerSize: IntSize,
    var imageSize: IntSize,
    var drawSize: IntSize,
    var overlaySize: IntSize,
    var aspectRatio: AspectRatio = aspectRatios[0].aspectRatio,
    var cropAspectRatio: BaseAspectRatioData = OriginalRatioData(
        id = 1,
        title = "Original",
        img = R.drawable.ic_orginal_crop
    ),
    var cropOutlineProperty: CropOutlineProperty = CropOutlineProperty(
        OutlineType.Rect,
        RectCropShape(0, "Rect")
    ),
)