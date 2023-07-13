package com.hashone.crop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.drawToBitmap
import com.bumptech.glide.Glide
import com.hashone.commons.base.BetterActivityResult
import com.hashone.commons.extensions.getColorCode
import com.hashone.commons.extensions.navigationUI
import com.hashone.commons.extensions.serializable
import com.hashone.commons.extensions.setStatusBarColor
import com.hashone.crop.databinding.ActivityMainBinding
import com.hashone.cropper.CropActivity
import com.hashone.cropper.builder.Crop
import com.hashone.cropper.model.CropDataSaved
import com.hashone.cropper.state.CropState
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


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

        val out = Environment.getExternalStorageDirectory().absolutePath
        val outFile = File(out, "hashone_phone_wallpaper.png")
        if (!outFile.exists())
            copyAssets()
        originalImageFilePath = outFile.absolutePath
        Glide.with(this).load(outFile.absolutePath).into(mBinding.originalImage)

    }

    private fun clickEvent() {
        mBinding.txtCropImage.setOnClickListener {
            mActivityLauncher.launch(
                Crop.open(activity = this, Crop.build(
                    originalImageFilePath = originalImageFilePath,
                    cropDataSaved = myCropDataSaved,
                ) {
                    cropState = null
                    croppedImageBitmap = null
                    //TODO: Screen
                    windowBackgroundColor = com.hashone.cropper.R.color.extra_extra_light_gray_color
                    statusBarColor = com.hashone.cropper.R.color.extra_extra_light_gray_color
                    navigationBarColor = com.hashone.cropper.R.color.white

                    //TODO: Toolbar
                    toolBarColor = com.hashone.cropper.R.color.white
                    backPressIcon = com.hashone.cropper.R.drawable.ic_back
                    backPressIconDescription = ""
                    toolBarTitle = "Crop"
                    toolBarTitleColor = com.hashone.cropper.R.color.black
                    toolBarTitleFont = com.hashone.cropper.R.font.outfit_regular
                    toolBarTitleSize = 16F

                    //TODO: AspectRatio
                    aspectRatioBackgroundColor = com.hashone.cropper.R.color.white
                    aspectRatioSelectedColor = com.hashone.cropper.R.color.black
                    aspectRatioUnSelectedColor = com.hashone.cropper.R.color.dark_gray_color_2
                    aspectRatioTitleFont = com.hashone.cropper.R.font.roboto_medium

                    //TODO: Bottom Icon & Text
                    cropDoneTextColor = com.hashone.cropper.R.color.black
                    cropDoneIcon = com.hashone.cropper.R.drawable.ic_check_croppy_selected
                    cropDoneText = "Crop"
                    cropDoneTextFont = com.hashone.cropper.R.font.roboto_medium
                    cropDoneTextSize = 16F

                    cropCancelTextColor = com.hashone.cropper.R.color.black
                    cropCancelIcon = com.hashone.cropper.R.drawable.ic_cancel
                    cropCancelText = "Skip"
                    cropCancelTextFont = com.hashone.cropper.R.font.roboto_medium
                    cropCancelTextSize = 16F

                    cropBottomBackgroundColor = com.hashone.cropper.R.color.white
                    dividerColor = com.hashone.cropper.R.color.extra_extra_light_gray_color

                }),
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

    private fun copyAssets() {
        val assetManager = assets
        var files: Array<String>? = null
        try {
            files = assetManager.list("")
        } catch (e: IOException) {
            Log.e("tag", "Failed to get asset file list.", e)
        }
        for (filename in files!!) {
            var `in`: InputStream? = null
            var out: OutputStream? = null
            try {
                `in` = assetManager.open(filename)
                val outDir = Environment.getExternalStorageDirectory().absolutePath
                val outFile = File(outDir, filename)
                out = FileOutputStream(outFile)
                copyFile(`in`, out)
                `in`.close()
                `in` = null
                out.flush()
                out.close()
                out = null
            } catch (e: IOException) {
                Log.e("tag", "Failed to copy asset file: $filename", e)
            }
        }
    }

    @Throws(IOException::class)
    private fun copyFile(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (`in`.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
    }

    //TODO: Screen UI - Start
    private fun setWindowUI() {
            setStatusBarColor(getColorCode(R.color.black))

    }
}