package com.hashone.cropper.model

import com.hashone.cropper.model.AspectRatio
import com.hashone.cropper.model.CropAspectRatio
import com.hashone.cropper.model.OutlineType
import com.hashone.cropper.model.OvalCropShape
import com.hashone.cropper.R

/**
 * Aspect ratio list with pre-defined aspect ratios
 */
val aspectRatios = listOf(
    CropAspectRatio(
        id = 1,
        title = "Original",
        aspectRatio = AspectRatio.Original,
        img = R.drawable.ic_aspect_orginal
    ),
    /*CropAspectRatio(
        title = "Free",
        aspectRatio = AspectRatio.Original,
        img = R.drawable.ic_aspect_free
    ),*/
    CropAspectRatio(
        id = 2,
        title = "Square",
        aspectRatio = AspectRatio(1 / 1f),
        img = R.drawable.ic_aspect_square
    ),
    CropAspectRatio(
        id = 3,
        isShape = true,
        outlineType = OutlineType.Oval,
        cropOutline = OvalCropShape(id = 3, title = "Oval"),
        title = "Circle",
        aspectRatio = AspectRatio(1 / 1.000001f),
        img = R.drawable.ic_aspect_circle
    ),
    CropAspectRatio(
        id = 4,
        title = "2:3",
        aspectRatio = AspectRatio(2 / 3f),
        img = R.drawable.ic_aspect_2_3
    ),
    CropAspectRatio(
        id = 5,
        title = "3:4",
        aspectRatio = AspectRatio(3 / 4f),
        img = R.drawable.ic_aspect_3_4
    ),
    CropAspectRatio(
        id = 6,
        title = "4:5",
        aspectRatio = AspectRatio(4 / 5f),
        img = R.drawable.ic_aspect_4_5
    ),
    CropAspectRatio(
        id = 7,
        title = "9:16",
        aspectRatio = AspectRatio(9 / 16f),
        img = R.drawable.ic_aspect_9_16
    ),
    CropAspectRatio(
        id = 8,
        title = "3:2",
        aspectRatio = AspectRatio(3 / 2f),
        img = R.drawable.ic_aspect_3_2
    ),
    CropAspectRatio(
        id = 9,
        title = "4:3",
        aspectRatio = AspectRatio(4 / 3f),
        img = R.drawable.ic_aspect_4_3
    ),
    CropAspectRatio(
        id = 10,
        title = "5:4",
        aspectRatio = AspectRatio(5f / 4f),
        img = R.drawable.ic_aspect_5_4
    ),
    CropAspectRatio(
        id = 11,
        title = "16:9",
        aspectRatio = AspectRatio(16 / 9f),
        img = R.drawable.ic_aspect_16_9
    ),

    )