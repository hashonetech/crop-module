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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat
import com.hashone.commons.extensions.getColorCode
import com.hashone.commons.extensions.navigationUI
import com.hashone.commons.extensions.serializable
import com.hashone.commons.extensions.setStatusBarColor
import com.hashone.cropper.builder.Crop
import com.hashone.cropper.model.CropAspectRatio
import com.hashone.cropper.model.OriginalRatioData
import com.hashone.cropper.model.OutlineType
import com.hashone.cropper.model.RatioData
import com.hashone.cropper.model.RectCropShape
import com.hashone.cropper.model.ShapeData
import com.hashone.cropper.model.aspectRatios
import com.hashone.cropper.model.createCropOutlineContainer
import com.hashone.cropper.model.getRatioData
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
                OriginalRatioData(
                    id = 1,
                    title = getString(R.string.label_original),
                    img = R.drawable.ic_orginal_crop,
                ),
                RatioData(
                    id = 2,
                    title = getString(R.string.label_square),
                    img = R.drawable.ic_square_crop,
                    ratioValue = 1F / 1F
                ),
                RatioData(
                    id = 4,
                    title = "2:3",
                    img = R.drawable.ic_2_3_crop,
                    ratioValue = 2F / 3F
                ),
                RatioData(
                    id = 5,
                    title = "3:4",
                    img = R.drawable.ic_3_4_crop,
                    ratioValue = 3F / 4F
                ),
                RatioData(
                    id = 6,
                    title = "4:5",
                    img = R.drawable.ic_4_5_crop,
                    ratioValue = 4F / 5F
                ),
                RatioData(
                    id = 7,
                    title = "9:16",
                    img = R.drawable.ic_9_16_crop,
                    ratioValue = 9F / 16F
                ),
                RatioData(
                    id = 8,
                    title = "3:2",
                    img = R.drawable.ic_3_2_crop,
                    ratioValue = 3F / 2F
                ),
                RatioData(
                    id = 9,
                    title = "4:3",
                    img = R.drawable.ic_4_3_crop,
                    ratioValue = 4F / 3F
                ),
                RatioData(
                    id = 10,
                    title = "5:4",
                    img = R.drawable.ic_5_4_crop,
                    ratioValue = 5F / 4F
                ),
                RatioData(
                    id = 11,
                    title = "16:9",
                    img = R.drawable.ic_16_9_crop,
                    ratioValue = 16F / 9F
                ),
                ShapeData(
                    id = 12,
                    title = getString(R.string.label_radius),
                    img = R.drawable.ic_square_crop,
                    shapeImg = R.drawable.ic_radius_shape
                ),
                ShapeData(
                    id = 13,
                    title = getString(R.string.label_circle),
                    img = R.drawable.ic_circle_crop,
                    shapeImg = R.drawable.ic_circle_shape
                ),
                ShapeData(
                    id = 14,
                    title = getString(R.string.label_triangle),
                    img = R.drawable.ic_triangle_crop,
                    shapeImg = R.drawable.ic_triangle_shape
                ),
                ShapeData(
                    id = 15,
                    title = getString(R.string.label_star),
                    img = R.drawable.ic_star_crop,
                    shapeImg = R.drawable.ic_star_shape
                ),
                ShapeData(
                    id = 16,
                    title = getString(R.string.label_heart),
                    img = R.drawable.ic_heart_crop,
                    shapeImg = R.drawable.ic_heart_shape
                ),
                ShapeData(
                    id = 17,
                    title = getString(R.string.label_insquare),
                    img = R.drawable.ic_insquare_crop,
                    shapeImg = R.drawable.ic_insquare_shape
                ),
                ShapeData(
                    id = 18,
                    title = getString(R.string.label_inarc),
                    img = R.drawable.ic_inarc_crop,
                    shapeImg = R.drawable.ic_inarc_shape
                ),
                ShapeData(
                    id = 19,
                    title = getString(R.string.label_heptagon),
                    img = R.drawable.ic_heptagon_crop,
                    shapeImg = R.drawable.ic_heptagon_shape
                ),
                ShapeData(
                    id = 20,
                    title = getString(R.string.label_pentagon),
                    img = R.drawable.ic_pentagon_crop,
                    shapeImg = R.drawable.ic_pentagon_shape
                ),
                ShapeData(
                    id = 21,
                    title = getString(R.string.label_hexagon),
                    img = R.drawable.ic_hexagon_crop,
                    shapeImg = R.drawable.ic_hexagon_shape
                ),
            )
        } else {
            aspectRatios = ArrayList()
/*            aspectRatios.add(
                OriginalRatioData(
                    id = 1,
                    title = getString(R.string.label_original),
                    img = R.drawable.ic_orginal_crop,
                )
            )
            aspectRatios.addAll(builder.mAspectRatio)*/

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

