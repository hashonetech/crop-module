package com.hashone.cropper.builder

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.res.stringResource
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.net.MediaType
import com.hashone.cropper.CropActivity
import com.hashone.cropper.R
import com.hashone.cropper.model.CropDataSaved
import com.hashone.cropper.state.CropState
import java.io.Serializable

open class Crop constructor(val builder: Builder) : Serializable {

    companion object {
        inline fun build(
            originalImageFilePath: String,
            cropDataSaved: CropDataSaved? = null,
            block: Builder.() -> Unit
        ) = Builder(
            originalImageFilePath,
            cropDataSaved,
        ).apply(block).build()

        fun open(activity: Activity, crop: Crop): Intent =
            CropActivity.newIntent(context = activity, crop = crop)
    }

    class Builder(
        val originalImageFilePath: String,
        var cropDataSaved: CropDataSaved? = null,
//        var mOriginalBitmap: Bitmap? = null,
    ) : Serializable {

        var LOCAL_FILE_SIZE = 1080
        var MAX_FILE_SIZE = 4096

        var cropState: CropState? = null
        var croppedImageBitmap: Bitmap? = null
        //TODO: Screen
        var windowBackgroundColor: Int = R.color.extra_extra_light_gray_color
        var statusBarColor: Int = R.color.extra_extra_light_gray_color
        var navigationBarColor: Int = R.color.white

        //TODO: Toolbar
        var toolBarColor: Int = R.color.white
        var backPressIcon: Int = R.drawable.ic_back
        var backPressIconDescription: String = ""
        var toolBarTitle: String = "Crop"
        var toolBarTitleColor: Int = R.color.black
        var toolBarTitleFont: Int = R.font.outfit_regular
        var toolBarTitleSize: Float = 16F

        //TODO: AspectRatio
        var aspectRatioBackgroundColor: Int = R.color.white
        var aspectRatioSelectedColor: Int = R.color.black
        var aspectRatioUnSelectedColor: Int = R.color.dark_gray_color_2
        var aspectRatioTitleFont: Int = R.font.roboto_medium

        //TODO: Bottom Icon & Text
        var cropDoneTextColor: Int = R.color.black
        var cropDoneIcon: Int = R.drawable.ic_check_croppy_selected
        var cropDoneText: String = "Crop"
        var cropDoneTextFont: Int = R.font.roboto_medium
        var cropDoneTextSize: Float = 16F

        var cropCancelTextColor: Int = R.color.black
        var cropCancelIcon: Int = R.drawable.ic_cancel
        var cropCancelText: String = "Skip"
        var cropCancelTextFont: Int = R.font.roboto_medium
        var cropCancelTextSize: Float = 16F

        var cropBottomBackgroundColor: Int = R.color.white
        var dividerColor: Int = R.color.extra_extra_light_gray_color

        fun build() = Crop(this)
    }
}