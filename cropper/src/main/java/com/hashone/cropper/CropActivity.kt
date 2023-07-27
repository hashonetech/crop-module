package com.hashone.cropper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hashone.commons.extensions.getColorCode
import com.hashone.commons.extensions.hideSystemUI
import com.hashone.commons.extensions.navigationUI
import com.hashone.commons.extensions.serializable
import com.hashone.commons.extensions.setStatusBarColor
import com.hashone.cropper.builder.Crop
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
                    Column {
                        ImageCropDemo(builder)
                    }
                }
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

