package com.hashone.cropper.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hashone.cropper.R

/**
 * Aspect ratio list with pre-defined aspect ratios
 */
var aspectRatios = arrayListOf<CropAspectRatio>()
    /*CropAspectRatio(
        id = 1,
        title = "Original",
        aspectRatio = AspectRatio.Original,
        img = R.drawable.ic_orginal_crop,
        outlineType = OutlineType.Rect,
        cropOutline = RectCropShape(id = 0, title = "Rect"),
    ),

    CropAspectRatio(
        id = 2,
        title = "Square",
        aspectRatio = AspectRatio(1 / 1f),
        img = R.drawable.ic_square_crop,
        outlineType = OutlineType.Rect,
        cropOutline = RectCropShape(id = 0, title = "Rect"),
    ),
    *//*CropAspectRatio(
        id = 3,
        isShape = true,
        outlineType = OutlineType.Oval,
        cropOutline = OvalCropShape(id = 3, title = "Oval"),
        title = "Circle",
        aspectRatio = AspectRatio(1 / 1.000001f),
        img = R.drawable.ic_circle_crop
    ),*//*
    CropAspectRatio(
        id = 4,
        title = "2:3",
        aspectRatio = AspectRatio(2 / 3f),
        img = R.drawable.ic_2_3_crop,
        outlineType = OutlineType.Rect,
        cropOutline = RectCropShape(id = 0, title = "Rect"),
    ),
    CropAspectRatio(
        id = 5,
        title = "3:4",
        aspectRatio = AspectRatio(3 / 4f),
        img = R.drawable.ic_3_4_crop,
        outlineType = OutlineType.Rect,
        cropOutline = RectCropShape(id = 0, title = "Rect"),
    ),
    CropAspectRatio(
        id = 6,
        title = "4:5",
        aspectRatio = AspectRatio(4 / 5f),
        img = R.drawable.ic_4_5_crop,
        outlineType = OutlineType.Rect,
        cropOutline = RectCropShape(id = 0, title = "Rect"),
    ),
    CropAspectRatio(
        id = 7,
        title = "9:16",
        aspectRatio = AspectRatio(9 / 16f),
        img = R.drawable.ic_9_16_crop,
        outlineType = OutlineType.Rect,
        cropOutline = RectCropShape(id = 0, title = "Rect"),
    ),
    CropAspectRatio(
        id = 8,
        title = "3:2",
        aspectRatio = AspectRatio(3 / 2f),
        img = R.drawable.ic_3_2_crop,
        outlineType = OutlineType.Rect,
        cropOutline = RectCropShape(id = 0, title = "Rect"),
    ),
    CropAspectRatio(
        id = 9,
        title = "4:3",
        aspectRatio = AspectRatio(4 / 3f),
        img = R.drawable.ic_4_3_crop,
        outlineType = OutlineType.Rect,
        cropOutline = RectCropShape(id = 0, title = "Rect"),
    ),
    CropAspectRatio(
        id = 10,
        title = "5:4",
        aspectRatio = AspectRatio(5f / 4f),
        img = R.drawable.ic_5_4_crop,
        outlineType = OutlineType.Rect,
        cropOutline = RectCropShape(id = 0, title = "Rect"),
    ),
    CropAspectRatio(
        id = 11,
        title = "16:9",
        aspectRatio = AspectRatio(16 / 9f),
        img = R.drawable.ic_16_9_crop,
        outlineType = OutlineType.Rect,
        cropOutline = RectCropShape(id = 0, title = "Rect"),
    ),
    CropAspectRatio(
        id = 12,
        title = "Radius",
        isShape = true,
        outlineType = OutlineType.ImageMask,
        cropOutline = ImageMaskOutline2(
            id = 1,
            title = "Radius",
            imageInt = R.drawable.ic_radius_shape
        ),
        aspectRatio = AspectRatio(1f / 1f),
        img = R.drawable.ic_square_crop,
    ),
    CropAspectRatio(
        id = 13,
        title = "Circle",
        isShape = true,
        outlineType = OutlineType.ImageMask,
        cropOutline = ImageMaskOutline2(
            id = 2,
            title = "Circle",
            imageInt = R.drawable.ic_circle_shape
        ),
        aspectRatio = AspectRatio(1f / 1f),
        img = R.drawable.ic_circle_crop,
    ),
    CropAspectRatio(
        id = 14,
        title = "Triangle",
        isShape = true,
        outlineType = OutlineType.ImageMask,
        cropOutline = ImageMaskOutline2(
            id = 3,
            title = "Triangle",
            imageInt = R.drawable.ic_triangle_shape
        ),
        aspectRatio = AspectRatio(1f / 1f),
        img = R.drawable.ic_triangle_crop,
    ),
    CropAspectRatio(
        id = 15,
        title = "Star",
        isShape = true,
        outlineType = OutlineType.ImageMask,
        cropOutline = ImageMaskOutline2(
            id = 4,
            title = "Star",
            imageInt = R.drawable.ic_star_shape
        ),
        aspectRatio = AspectRatio(1f / 1f),
        img = R.drawable.ic_star_crop,
    ),
    CropAspectRatio(
        id = 16,
        title = "Heart",
        isShape = true,
        outlineType = OutlineType.ImageMask,
        cropOutline = ImageMaskOutline2(
            id = 5,
            title = "Heart",
            imageInt = R.drawable.ic_heart_shape
        ),
        aspectRatio = AspectRatio(1f / 1f),
        img = R.drawable.ic_heart_crop,
    ),
    CropAspectRatio(
        id = 17,
        title = "Insquare",
        isShape = true,
        outlineType = OutlineType.ImageMask,
        cropOutline = ImageMaskOutline2(
            id = 6,
            title = "Insquare",
            imageInt = R.drawable.ic_insquare_shape
        ),
        aspectRatio = AspectRatio(1f / 1f),
        img = R.drawable.ic_insquare_crop,
    ),
    CropAspectRatio(
        id = 18,
        title = "Inarc",
        isShape = true,
        outlineType = OutlineType.ImageMask,
        cropOutline = ImageMaskOutline2(
            id = 7,
            title = "Inarc",
            imageInt = R.drawable.ic_inarc_shape
        ),
        aspectRatio = AspectRatio(1f / 1f),
        img = R.drawable.ic_inarc_crop,
    ),
    CropAspectRatio(
        id = 19,
        title = "Heptagon",
        isShape = true,
        outlineType = OutlineType.ImageMask,
        cropOutline = ImageMaskOutline2(
            id = 8,
            title = "Heptagon",
            imageInt = R.drawable.ic_heptagon_shape
        ),
        aspectRatio = AspectRatio(1f / 1f),
        img = R.drawable.ic_heptagon_crop,
    ),
    CropAspectRatio(
        id = 20,
        title = "Pentagon",
        isShape = true,
        outlineType = OutlineType.ImageMask,
        cropOutline = ImageMaskOutline2(
            id = 9,
            title = "Pentagon",
            imageInt = R.drawable.ic_pentagon_shape
        ),
        aspectRatio = AspectRatio(1f / 1f),
        img = R.drawable.ic_pentagon_crop,
    ),
    CropAspectRatio(
        id = 21,
        title = "Hexagon",
        isShape = true,
        outlineType = OutlineType.ImageMask,
        cropOutline = ImageMaskOutline2(
            id = 10,
            title = "Hexagon",
            imageInt = R.drawable.ic_hexagon_shape
        ),
        aspectRatio = AspectRatio(1f / 1f),
        img = R.drawable.ic_hexagon_crop,
    ),*/

//    )