package com.hashone.cropper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hashone.commons.extensions.getColorCode
import com.hashone.commons.extensions.navigationUI
import com.hashone.commons.extensions.serializable
import com.hashone.commons.extensions.setStatusBarColor
import com.hashone.cropper.builder.Crop
import com.hashone.cropper.model.OriginalRatioData
import com.hashone.cropper.model.OutlineType
import com.hashone.cropper.model.RatioData
import com.hashone.cropper.model.RectCropShape
import com.hashone.cropper.model.ShapeData
import com.hashone.cropper.model.aspectRatios
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
                    val systemUiController = rememberSystemUiController()
                    SideEffect {
                        systemUiController.setStatusBarColor(color = Color(getColorCode(builder.screenBuilder.statusBarColor)))
                        systemUiController.setNavigationBarColor(color = Color(getColorCode(builder.screenBuilder.navigationBarColor)))
                    }

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
                OriginalRatioData(
                    id = 1,
                    title = getString(R.string.crop_label_original),
                    img = R.drawable.ic_orginal_crop,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 2,
                    title = getString(R.string.crop_label_square),
                    img = R.drawable.ic_square_crop,
                    ratioValue = 1F / 1F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 4,
                    title = "2:3",
                    img = R.drawable.ic_2_3_crop,
                    ratioValue = 2F / 3F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 5,
                    title = "3:4",
                    img = R.drawable.ic_3_4_crop,
                    ratioValue = 3F / 4F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 6,
                    title = "4:5",
                    img = R.drawable.ic_4_5_crop,
                    ratioValue = 4F / 5F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 7,
                    title = "9:16",
                    img = R.drawable.ic_9_16_crop,
                    ratioValue = 9F / 16F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 8,
                    title = "3:2",
                    img = R.drawable.ic_3_2_crop,
                    ratioValue = 3F / 2F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 9,
                    title = "4:3",
                    img = R.drawable.ic_4_3_crop,
                    ratioValue = 4F / 3F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 10,
                    title = "5:4",
                    img = R.drawable.ic_5_4_crop,
                    ratioValue = 5F / 4F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 11,
                    title = "16:9",
                    img = R.drawable.ic_16_9_crop,
                    ratioValue = 16F / 9F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                ShapeData(
                    id = 12,
                    title = getString(R.string.crop_label_radius),
                    img = R.drawable.ic_square_crop,
                    shapeImg = R.drawable.ic_radius_shape
                ),
                ShapeData(
                    id = 13,
                    title = getString(R.string.crop_label_circle),
                    img = R.drawable.ic_circle_crop,
                    shapeImg = R.drawable.ic_circle_shape
                ),
                ShapeData(
                    id = 14,
                    title = getString(R.string.crop_label_triangle),
                    img = R.drawable.ic_triangle_crop,
                    shapeImg = R.drawable.ic_triangle_shape
                ),
                ShapeData(
                    id = 15,
                    title = getString(R.string.crop_label_star),
                    img = R.drawable.ic_star_crop,
                    shapeImg = R.drawable.ic_star_shape
                ),
                ShapeData(
                    id = 16,
                    title = getString(R.string.crop_label_heart),
                    img = R.drawable.ic_heart_crop,
                    shapeImg = R.drawable.ic_heart_shape
                ),
                ShapeData(
                    id = 17,
                    title = getString(R.string.crop_label_insquare),
                    img = R.drawable.ic_insquare_crop,
                    shapeImg = R.drawable.ic_insquare_shape
                ),
                ShapeData(
                    id = 18,
                    title = getString(R.string.crop_label_inarc),
                    img = R.drawable.ic_inarc_crop,
                    shapeImg = R.drawable.ic_inarc_shape
                ),
                ShapeData(
                    id = 19,
                    title = getString(R.string.crop_label_heptagon),
                    img = R.drawable.ic_heptagon_crop,
                    shapeImg = R.drawable.ic_heptagon_shape
                ),
                ShapeData(
                    id = 20,
                    title = getString(R.string.crop_label_pentagon),
                    img = R.drawable.ic_pentagon_crop,
                    shapeImg = R.drawable.ic_pentagon_shape
                ),
                ShapeData(
                    id = 21,
                    title = getString(R.string.crop_label_hexagon),
                    img = R.drawable.ic_hexagon_crop,
                    shapeImg = R.drawable.ic_hexagon_shape
                ),
            )
        } else {
            aspectRatios = ArrayList()
            builder.mAspectRatio.forEachIndexed { index, cropAspectRatioData ->


                aspectRatios.add(
                    when(cropAspectRatioData.outlineType) {
                        OutlineType.ImageMask -> {
                            val aspectRatioData = cropAspectRatioData as ShapeData
                            ShapeData(
                                id = aspectRatioData.id,
                                title = cropAspectRatioData.title,
                                img = cropAspectRatioData.img,
                                shapeImg = cropAspectRatioData.shapeImg,
                                ratioValue = cropAspectRatioData.ratioValue
                            )
                        }
                        else -> {
                            RatioData(
                                id = cropAspectRatioData.id,
                                title = cropAspectRatioData.title,
                                img = cropAspectRatioData.img,
                                ratioValue = cropAspectRatioData.ratioValue,
                                cropOutline = RectCropShape(id = 0, title = "Rect")
                            )
                        }
                    }
                )
            }
        }

        if (builder.cropDataSaved != null) {
            val circleAspectRatioData = aspectRatios.first { it.title == getString(R.string.crop_label_circle) }
            if (builder.cropDataSaved!!.cropAspectRatioId == 3 && builder.cropDataSaved!!.cropOutlineTitle == "Oval" && circleAspectRatioData!=null) {
                builder.cropDataSaved!!.cropAspectRatioId = circleAspectRatioData.id
                builder.cropDataSaved!!.aspectRatio = circleAspectRatioData.aspectRatio.value
                builder.cropDataSaved!!.cropAspectRatioImg = circleAspectRatioData.img
                builder.cropDataSaved!!.cropOutlineId = circleAspectRatioData.cropOutline.id
                builder.cropDataSaved!!.cropOutlineTitle = circleAspectRatioData.cropOutline.title
                builder.cropDataSaved!!.cropOutlineType = OutlineType.ImageMask.name
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

