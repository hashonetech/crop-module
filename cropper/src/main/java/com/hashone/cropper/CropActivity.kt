package com.hashone.cropper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat
import com.hashone.commons.extensions.getColorCode
import com.hashone.commons.extensions.navigationUI
import com.hashone.commons.extensions.serializable
import com.hashone.commons.extensions.setStatusBarColor
import com.hashone.cropper.builder.Crop
import com.hashone.cropper.model.AspectRatio
import com.hashone.cropper.model.CropAspectRatio
import com.hashone.cropper.model.ImageMaskOutline2
import com.hashone.cropper.model.OutlineType
import com.hashone.cropper.model.RectCropShape
import com.hashone.cropper.model.aspectRatios
import com.hashone.cropper.model.createCropOutlineContainer
import com.hashone.cropper.theme.ComposeCropperTheme

class CropActivity : ComponentActivity() {


    private lateinit var builder: Crop.Builder

    companion object {

        const val KEY_CROP_DATA = "KEY_CROP_DATA"
        const val KEY_RETURN_CROP_DATA = "KEY_RETURN_CROP_DATA"

        fun newIntent(context: Context, crop: Crop): Intent {
            return Intent(context, CropActivity::class.java).apply {
                Bundle().apply { putSerializable(KEY_CROP_DATA, crop) }
                    .also { this.putExtras(it) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        builder = (intent!!.extras!!.serializable<Crop>(KEY_CROP_DATA)!!).builder

        setWindowUI()

        setContent {
            ComposeCropperTheme(builder) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = builder.screenBuilder.windowBackgroundColor))
                ) {
//                    if (builder.mAspectRatio.isNotEmpty() && !builder.useDefaults) {
                    updateAspectRatio()

                    Column {
                        ImageCropDemo(builder)
                    }
                }
            }
        }
    }

    private fun updateAspectRatio() {

        if (builder.useDefaults) {
            aspectRatios = ArrayList()
            aspectRatios = arrayListOf(
                CropAspectRatio(
                    id = 1,
                    title = getString(R.string.label_original),
                    aspectRatio = AspectRatio.Original,
                    img = R.drawable.ic_orginal_crop,
                    outlineType = OutlineType.Rect,
                    cropOutline = RectCropShape(id = 0, title = "Rect"),
                ),

                CropAspectRatio(
                    id = 2,
                    title = getString(R.string.label_square),
                    aspectRatio = AspectRatio(1 / 1f),
                    img = R.drawable.ic_square_crop,
                    outlineType = OutlineType.Rect,
                    cropOutline = RectCropShape(id = 0, title = "Rect"),
                ),
                /*CropAspectRatio(
                    id = 3,
                    isShape = true,
                    outlineType = OutlineType.Oval,
                    cropOutline = OvalCropShape(id = 3, title = "Oval"),
                    title = "Circle",
                    aspectRatio = AspectRatio(1 / 1.000001f),
                    img = R.drawable.ic_circle_crop
                ),*/
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
                ),

                )
        } else {
            aspectRatios = ArrayList()
            aspectRatios.add(
                CropAspectRatio(
                    id = 1,
                    title = getString(R.string.label_original),
                    aspectRatio = AspectRatio.Original,
                    img = R.drawable.ic_orginal_crop,
                    outlineType = OutlineType.Rect,
                    cropOutline = RectCropShape(id = 0, title = "Rect"),
                ))

            builder.mAspectRatio.forEachIndexed { index, cropAspectRatioData ->
                aspectRatios.add(
                    CropAspectRatio(
                        id = cropAspectRatioData.id,
                        title = cropAspectRatioData.title,
                        aspectRatio = cropAspectRatioData.aspectRatio,
                        img = cropAspectRatioData.img,
                        isShape = cropAspectRatioData.isShape,
                        outlineType = cropAspectRatioData.outlineType,
                        cropOutline = if (cropAspectRatioData.outlineType != OutlineType.ImageMask) createCropOutlineContainer(cropAspectRatioData.outlineType) else cropAspectRatioData.cropOutline
                    )
                )
            }
        }

    }

    //TODO: Screen UI - Start
    private fun setWindowUI() {
            if (builder.screenBuilder.statusBarColor != -1) {
                setStatusBarColor(getColorCode(builder.screenBuilder.statusBarColor))
                navigationUI(true, getColorCode(builder.screenBuilder.navigationBarColor))
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                }
            }
    }

}

