package com.hashone.cropper.model

import com.hashone.cropper.R

/**
 * Aspect ratio list with pre-defined aspect ratios
 */
val aspectRatios = listOf(
    CropAspectRatio(
        id = 1,
        title = "Original",
        aspectRatio = AspectRatio.Original,
        img = R.drawable.ic_orginal_crop
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
        img = R.drawable.ic_square_crop
    ),
    CropAspectRatio(
        id = 3,
        isShape = true,
        outlineType = OutlineType.Oval,
        cropOutline = OvalCropShape(id = 3, title = "Oval"),
        title = "Circle",
        aspectRatio = AspectRatio(1 / 1.000001f),
        img = R.drawable.ic_circle_crop
    ),
    CropAspectRatio(
        id = 4,
        title = "2:3",
        aspectRatio = AspectRatio(2 / 3f),
        img = R.drawable.ic_2_3_crop
    ),
    CropAspectRatio(
        id = 5,
        title = "3:4",
        aspectRatio = AspectRatio(3 / 4f),
        img = R.drawable.ic_3_4_crop
    ),
    CropAspectRatio(
        id = 6,
        title = "4:5",
        aspectRatio = AspectRatio(4 / 5f),
        img = R.drawable.ic_4_5_crop
    ),
    CropAspectRatio(
        id = 7,
        title = "9:16",
        aspectRatio = AspectRatio(9 / 16f),
        img = R.drawable.ic_9_16_crop
    ),
    CropAspectRatio(
        id = 8,
        title = "3:2",
        aspectRatio = AspectRatio(3 / 2f),
        img = R.drawable.ic_3_2_crop
    ),
    CropAspectRatio(
        id = 9,
        title = "4:3",
        aspectRatio = AspectRatio(4 / 3f),
        img = R.drawable.ic_4_3_crop
    ),
    CropAspectRatio(
        id = 10,
        title = "5:4",
        aspectRatio = AspectRatio(5f / 4f),
        img = R.drawable.ic_5_4_crop
    ),
    CropAspectRatio(
        id = 11,
        title = "16:9",
        aspectRatio = AspectRatio(16 / 9f),
        img = R.drawable.ic_16_9_crop
    ),

    )