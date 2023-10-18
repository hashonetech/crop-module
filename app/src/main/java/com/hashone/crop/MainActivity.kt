package com.hashone.crop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.hashone.commons.base.BetterActivityResult
import com.hashone.commons.extensions.getColorCode
import com.hashone.commons.extensions.serializable
import com.hashone.commons.extensions.setStatusBarColor
import com.hashone.crop.databinding.ActivityMainBinding
import com.hashone.cropper.CropActivity
import com.hashone.cropper.builder.Crop
import com.hashone.cropper.model.AspectRatio
import com.hashone.cropper.model.CropAspectRatio
import com.hashone.cropper.model.CropDataSaved
import com.hashone.cropper.model.ImageMaskOutline2
import com.hashone.cropper.model.OutlineType
import java.io.File


class MainActivity : AppCompatActivity() {
    private var orgBitmap: Bitmap? = null
    private var myCropDataSaved: CropDataSaved? = null
    private var originalImageFilePath: String = ""
    private lateinit var mBinding: ActivityMainBinding

    private val REQUEST_CODE_IMAGE = 101
    private val REQUEST_CODE_VIDEO = 102
    private val REQUEST_CODE_IMAGE_VIDEO = 103
    private val REQUEST_CODE_CAMERA = 104

    val mActivityLauncher: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowUI()
        setContentView(R.layout.activity_main)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        checkPermissions(REQUEST_CODE_IMAGE_VIDEO)

        setDummyData()

        clickEvent()
    }

    private fun setDummyData() {
        originalImageFilePath = getFileFromAssets().absolutePath
        Glide.with(this).load(originalImageFilePath).into(mBinding.originalImage)
    }

    private fun clickEvent() {
        /*
        val aspectRatios = arrayListOf(
            CropAspectRatio(
                id = 1,
                title = "Original",
                aspectRatio = AspectRatio.Original,
                img = com.hashone.cropper.R.drawable.ic_orginal_crop,

                ),
            CropAspectRatio(
                id = 2,
                title = "Square",
                aspectRatio = AspectRatio(1 / 1f),
                img = com.hashone.cropper.R.drawable.ic_square_crop,

                ),
            CropAspectRatio(
                id = 4,
                title = "2:3",
                aspectRatio = AspectRatio(2 / 3f),
                img = com.hashone.cropper.R.drawable.ic_2_3_crop,

                ),
            CropAspectRatio(
                id = 5,
                title = "3:4",
                aspectRatio = AspectRatio(3 / 4f),
                img = com.hashone.cropper.R.drawable.ic_3_4_crop,

                ),
            CropAspectRatio(
                id = 6,
                title = "4:5",
                aspectRatio = AspectRatio(4 / 5f),
                img = com.hashone.cropper.R.drawable.ic_4_5_crop,

                ),
            CropAspectRatio(
                id = 7,
                title = "9:16",
                aspectRatio = AspectRatio(9 / 16f),
                img = com.hashone.cropper.R.drawable.ic_9_16_crop,

                ),
            CropAspectRatio(
                id = 8,
                title = "3:2",
                aspectRatio = AspectRatio(3 / 2f),
                img = com.hashone.cropper.R.drawable.ic_3_2_crop,

                ),
            CropAspectRatio(
                id = 9,
                title = "4:3",
                aspectRatio = AspectRatio(4 / 3f),
                img = com.hashone.cropper.R.drawable.ic_4_3_crop,

                ),
            CropAspectRatio(
                id = 10,
                title = "5:4",
                aspectRatio = AspectRatio(5f / 4f),
                img = com.hashone.cropper.R.drawable.ic_5_4_crop,

                ),
            CropAspectRatio(
                id = 11,
                title = "16:9",
                aspectRatio = AspectRatio(16 / 9f),
                img = com.hashone.cropper.R.drawable.ic_16_9_crop,

                ),
            CropAspectRatio(
                id = 19,
                title = "Rounded",
                isShape = true,
                outlineType = OutlineType.RoundedRect,
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_square_crop,
            ),
            CropAspectRatio(
                id = 20,
                title = "CutCorner",
                isShape = true,
                outlineType = OutlineType.CutCorner,
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_square_crop,
            ),
            CropAspectRatio(
                id = 21,
                title = "Oval",
                isShape = true,
                outlineType = OutlineType.Oval,
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_square_crop,
            ),
            CropAspectRatio(
                id = 22,
                title = "Polygon",
                isShape = true,
                outlineType = OutlineType.Polygon,
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_square_crop,
            ),
            CropAspectRatio(
                id = 23,
                title = "Pentagon",
                isShape = true,
                outlineType = OutlineType.Pentagon,
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_square_crop,
            ),
            CropAspectRatio(
                id = 24,
                title = "Heptagon",
                isShape = true,
                outlineType = OutlineType.Heptagon,
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_square_crop,
            ),
            CropAspectRatio(
                id = 25,
                title = "Octagon",
                isShape = true,
                outlineType = OutlineType.Octagon,
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_square_crop,
            ),
            CropAspectRatio(
                id = 26,
                title = "Heart",
                isShape = true,
                outlineType = OutlineType.Custom,
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_square_crop,
            ),
            CropAspectRatio(
                id = 27,
                title = "Star",
                isShape = true,
                outlineType = OutlineType.Star,
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_square_crop,
            ),

            CropAspectRatio(
                id = 28,
                title = "sun",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 1,
                    title = "ImageMask",
                    imageInt = com.hashone.cropper.R.drawable.sun
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.sun,
            ),

            CropAspectRatio(
                id = 29,
                title = "cloud",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 2,
                    title = "ImageMask",
                    imageInt = com.hashone.cropper.R.drawable.cloud
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.cloud,
            ),

            CropAspectRatio(
                id = 30,
                title = "stick",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 3,
                    title = "ImageMask",
                    imageInt = com.hashone.cropper.R.drawable.shape_stick
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.shape_stick,
            ),

            )
        */

        val aspectRatios = arrayListOf(
            CropAspectRatio(
                id = 1,
                title = "Original",
                aspectRatio = AspectRatio.Original,
                img = com.hashone.cropper.R.drawable.ic_orginal_crop,
            ),

            CropAspectRatio(
                id = 2,
                title = "Square",
                aspectRatio = AspectRatio(1 / 1f),
                img = com.hashone.cropper.R.drawable.ic_square_crop
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
                img = com.hashone.cropper.R.drawable.ic_2_3_crop
            ),
            CropAspectRatio(
                id = 5,
                title = "3:4",
                aspectRatio = AspectRatio(3 / 4f),
                img = com.hashone.cropper.R.drawable.ic_3_4_crop
            ),
            CropAspectRatio(
                id = 6,
                title = "4:5",
                aspectRatio = AspectRatio(4 / 5f),
                img = com.hashone.cropper.R.drawable.ic_4_5_crop
            ),
            CropAspectRatio(
                id = 7,
                title = "9:16",
                aspectRatio = AspectRatio(9 / 16f),
                img = com.hashone.cropper.R.drawable.ic_9_16_crop
            ),
            CropAspectRatio(
                id = 8,
                title = "3:2",
                aspectRatio = AspectRatio(3 / 2f),
                img = com.hashone.cropper.R.drawable.ic_3_2_crop
            ),
            CropAspectRatio(
                id = 9,
                title = "4:3",
                aspectRatio = AspectRatio(4 / 3f),
                img = com.hashone.cropper.R.drawable.ic_4_3_crop
            ),
            CropAspectRatio(
                id = 10,
                title = "5:4",
                aspectRatio = AspectRatio(5f / 4f),
                img = com.hashone.cropper.R.drawable.ic_5_4_crop
            ),
            CropAspectRatio(
                id = 11,
                title = "16:9",
                aspectRatio = AspectRatio(16 / 9f),
                img = com.hashone.cropper.R.drawable.ic_16_9_crop
            ),
            CropAspectRatio(
                id = 12,
                title = "Radius",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 1,
                    title = "Radius",
                    imageInt = com.hashone.cropper.R.drawable.ic_radius_shape
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_square_crop,
            ),
            CropAspectRatio(
                id = 13,
                title = "Circle",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 2,
                    title = "Circle",
                    imageInt = com.hashone.cropper.R.drawable.ic_circle_shape
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_circle_crop,
            ),
            CropAspectRatio(
                id = 14,
                title = "Triangle",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 3,
                    title = "Triangle",
                    imageInt = com.hashone.cropper.R.drawable.ic_triangle_shape
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_triangle_crop,
            ),
            CropAspectRatio(
                id = 15,
                title = "Star",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 4,
                    title = "Star",
                    imageInt = com.hashone.cropper.R.drawable.ic_star_shape
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_star_crop,
            ),
            CropAspectRatio(
                id = 16,
                title = "Heart",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 5,
                    title = "Heart",
                    imageInt = com.hashone.cropper.R.drawable.ic_heart_shape
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_heart_crop,
            ),
            CropAspectRatio(
                id = 17,
                title = "Insquare",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 6,
                    title = "Insquare",
                    imageInt = com.hashone.cropper.R.drawable.ic_insquare_shape
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_insquare_crop,
            ),
            CropAspectRatio(
                id = 18,
                title = "Inarc",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 7,
                    title = "Inarc",
                    imageInt = com.hashone.cropper.R.drawable.ic_inarc_shape
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_inarc_crop,
            ),
            CropAspectRatio(
                id = 19,
                title = "Heptagon",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 8,
                    title = "Heptagon",
                    imageInt = com.hashone.cropper.R.drawable.ic_heptagon_shape
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_heptagon_crop,
            ),
            CropAspectRatio(
                id = 20,
                title = "Pentagon",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 9,
                    title = "Pentagon",
                    imageInt = com.hashone.cropper.R.drawable.ic_pentagon_shape
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_pentagon_crop,
            ),
            CropAspectRatio(
                id = 21,
                title = "Hexagon",
                isShape = true,
                outlineType = OutlineType.ImageMask,
                cropOutline = ImageMaskOutline2(
                    id = 10,
                    title = "Hexagon",
                    imageInt = com.hashone.cropper.R.drawable.ic_hexagon_shape
                ),
                aspectRatio = AspectRatio(1f / 1f),
                img = com.hashone.cropper.R.drawable.ic_hexagon_crop,
            ),

            )
        mBinding.txtCropImage.setOnClickListener {
            val cropIntent = Crop.build(
                originalImageFilePath = originalImageFilePath,
                cropDataSaved = myCropDataSaved,
                cropState = null,
                croppedImageBitmap = null,
                useDefaults = true,
                forceCrop = true
            ) {


                //TODO: Crop Ratios
                mAspectRatio = arrayListOf()


                //TODO: Screen
                screenBuilder = Crop.ScreenBuilder(
                    windowBackgroundColor = com.hashone.cropper.R.color.window_bg_color,
                    statusBarColor = com.hashone.cropper.R.color.white,
                    navigationBarColor = com.hashone.cropper.R.color.white,
                    cropOuterBorderColor = com.hashone.cropper.R.color.un_select_color,
                    borderWidth = 1f,
                    borderSpacing = 2f,
                )

                //TODO: Toolbar
                toolBarBuilder = Crop.ToolBarBuilder(
                    toolBarColor = com.hashone.cropper.R.color.white,
                    backIcon = com.hashone.cropper.R.drawable.ic_back,
                    title = "Crop",
                    titleColor = com.hashone.cropper.R.color.black,
                    titleFont = com.hashone.cropper.R.font.roboto_medium,
                    titleSize = 16F,
                )

                //TODO: Image Size
                sizeBuilder = Crop.SizeBuilder(
                    localFileSize = 1080,
                    maxFileSize = 4096,
                )

                //TODO: AspectRatio
                aspectRatioBuilder = Crop.AspectRatioBuilder(
                    backgroundColor = com.hashone.cropper.R.color.white,
                    selectedColor = com.hashone.cropper.R.color.black,
                    unSelectedColor = com.hashone.cropper.R.color.un_select_color,
                    titleFont = com.hashone.cropper.R.font.roboto_medium,
                    titleSize = 12F,
                    originalTitle = "Original",
                    squareTitle = "Square",
                    circleTitle = "Circle",
                )


                //TODO: Bottom Icon & Text
                bottomPanelBuilder = Crop.BottomPanelBuilder(
                    cropBottomBackgroundColor = com.hashone.cropper.R.color.white,
                    dividerColor = com.hashone.cropper.R.color.white,
                    doneButtonBuilder = Crop.ButtonBuilder(
                        textColor = com.hashone.cropper.R.color.black,
                        icon = com.hashone.cropper.R.drawable.ic_check_croppy_selected,
                        buttonText = "Crop",
                        textFont = com.hashone.cropper.R.font.roboto_medium,
                        textSize = 16F,
                    ),
                    cancelButtonBuilder = Crop.ButtonBuilder(
                        textColor = com.hashone.cropper.R.color.black,
                        icon = com.hashone.cropper.R.drawable.ic_cancel,
                        buttonText = "Skip",
                        textFont = com.hashone.cropper.R.font.roboto_medium,
                        textSize = 16F,
                    ),
                )
            }

            mActivityLauncher.launch(
                Crop.open(activity = this, cropIntent),
                onActivityResult = object : BetterActivityResult.OnActivityResult<ActivityResult> {
                    override fun onActivityResult(activityResult: ActivityResult) {
                        if (activityResult.resultCode == Activity.RESULT_OK) {
                            activityResult.data?.let { intentData ->
                                if (intentData.hasExtra(CropActivity.KEY_RETURN_CROP_DATA)) {
                                    myCropDataSaved =
                                        intentData.extras?.serializable<CropDataSaved>(CropActivity.KEY_RETURN_CROP_DATA)
                                    Glide.with(this@MainActivity).load(myCropDataSaved!!.cropImg)
                                        .into(mBinding.cropedImage)
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    private var mCurrentRequestCode = -1
    private fun checkPermissions(requestCode: Int): Boolean {
        mCurrentRequestCode = requestCode
        val permissions = ArrayList<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (mCurrentRequestCode == REQUEST_CODE_IMAGE) {
                if (!isPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES)) permissions.add(
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else if (mCurrentRequestCode == REQUEST_CODE_VIDEO) {
                if (!isPermissionGranted(Manifest.permission.READ_MEDIA_VIDEO)) permissions.add(
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            } else if (mCurrentRequestCode == REQUEST_CODE_CAMERA) {
                if (!isPermissionGranted(Manifest.permission.CAMERA)) permissions.add(Manifest.permission.CAMERA)
            } else {
                if (!isPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES)) permissions.add(
                    Manifest.permission.READ_MEDIA_IMAGES
                )
                if (!isPermissionGranted(Manifest.permission.READ_MEDIA_VIDEO)) permissions.add(
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            }
        } else {
            if (mCurrentRequestCode == REQUEST_CODE_CAMERA) {
                if (!isPermissionGranted(Manifest.permission.CAMERA)) permissions.add(Manifest.permission.CAMERA)
            } else if (!isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) permissions.add(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        if (permissions.isNotEmpty()) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        } else {
            return true
        }
        return false
    }

    private var requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (!it.isNullOrEmpty()) {
                var mIsGranted: Boolean = false
                it.forEach { (permission, isGranted) ->
                    when (mCurrentRequestCode) {
                        REQUEST_CODE_IMAGE -> {
                            mIsGranted = isGranted
                        }

                        REQUEST_CODE_VIDEO -> {
                            mIsGranted = isGranted
                        }

                        else -> {
                            mIsGranted = isGranted
                        }
                    }
                }
            }
        }


    //    private fun getFileFromAssets(): File = File(cacheDir, "hashone_phone_wallpaper.png")
//    private fun getFileFromAssets(): File = File(cacheDir, "large_img.png")
    private fun getFileFromAssets(): File = File(cacheDir, "demo_1.jpg")
    //    private fun getFileFromAssets(): File = File(cacheDir, "hashone_phone_wallpaper.png")
//    private fun getFileFromAssets(): File = File(cacheDir, "large_img.png")
        .also {
            if (!it.exists()) {
                it.outputStream().use { cache ->
//                    assets.open("hashone_phone_wallpaper.png").use { inputStream ->
//                    assets.open("large_img.png").use { inputStream ->
                    assets.open("demo_1.jpg").use { inputStream ->
                        inputStream.copyTo(cache)
                    }
                }
            }
        }

    //TODO: Screen UI - Start
    private fun setWindowUI() {
        setStatusBarColor(getColorCode(R.color.black))
    }
}