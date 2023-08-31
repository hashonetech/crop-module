package com.hashone.crop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hashone.commons.base.BetterActivityResult
import com.hashone.commons.extensions.getColorCode
import com.hashone.commons.extensions.serializable
import com.hashone.commons.extensions.setStatusBarColor
import com.hashone.crop.databinding.ActivityMainBinding
import com.hashone.cropper.CropActivity
import com.hashone.cropper.builder.Crop
import com.hashone.cropper.model.CropDataSaved
import java.io.File


class MainActivity : AppCompatActivity() {
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
        mBinding.txtCropImage.setOnClickListener {
            val cropIntent = Crop.build(
                originalImageFilePath = originalImageFilePath,
                cropDataSaved = myCropDataSaved,
                cropState = null,
                croppedImageBitmap = null
            ) {

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


                //TODO: AspectRatio
                aspectRatioBuilder = Crop.AspectRatioBuilder(
                    backgroundColor = com.hashone.cropper.R.color.white,
                    selectedColor = com.hashone.cropper.R.color.black,
                    unSelectedColor = com.hashone.cropper.R.color.un_select_color,
                    titleFont = com.hashone.cropper.R.font.roboto_medium,
                    titleSize = 12F
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


    private fun getFileFromAssets(): File = File(cacheDir, "hashone_phone_wallpaper.png")
        .also {
            if (!it.exists()) {
                it.outputStream().use { cache ->
                    assets.open("hashone_phone_wallpaper.png").use { inputStream ->
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