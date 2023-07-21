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
            cropState: CropState? = null,
            croppedImageBitmap: Bitmap? = null,
            block: Builder.() -> Unit
        ) = Builder(
            originalImageFilePath,
            cropDataSaved,
            cropState,
            croppedImageBitmap
        ).apply(block).build()

        fun open(activity: Activity, crop: Crop): Intent =
            CropActivity.newIntent(context = activity, crop = crop)
    }

    class Builder(
        val originalImageFilePath: String,
        var cropDataSaved: CropDataSaved? = null,
        var cropState: CropState? = null,
        var croppedImageBitmap: Bitmap? = null,
    ) : Serializable {

        //TODO: Image Bitmap Size
        var sizeBuilder = SizeBuilder()

        //TODO: Screen
        var screenBuilder = ScreenBuilder()

        //TODO: Toolbar
        var toolBarBuilder = ToolBarBuilder()

        //TODO: AspectRatio
        var aspectRatioBuilder = AspectRatioBuilder()


        //TODO: Bottom Icon & Text
        var bottomBuilder = BottomBuilder()

        fun build() = Crop(this)
    }

    class SizeBuilder(
        var localFileSize: Int = 1080,
        var maxFileSize: Int = 4096,
    ) : Serializable

    class ScreenBuilder(
        var windowBackgroundColor: Int = R.color.window_bg_color,
        var statusBarColor: Int = R.color.extra_extra_light_gray_color,
        var navigationBarColor: Int = R.color.white,
        var cropOuterBorderColor: Int = R.color.un_select_color
    ) : Serializable

    class ToolBarBuilder(
        var toolBarColor: Int = R.color.white,
        var backPressIcon: Int = R.drawable.ic_back,
        var backPressIconDescription: String = "",
        var toolBarTitle: String = "Crop",
        var toolBarTitleColor: Int = R.color.black,
        var toolBarTitleFont: Int = R.font.outfit_regular,
        var toolBarTitleSize: Float = 16F,
    ) : Serializable

    class AspectRatioBuilder(
        var aspectRatioBackgroundColor: Int = R.color.white,
        var aspectRatioSelectedColor: Int = R.color.black,
        var aspectRatioUnSelectedColor: Int = R.color.un_select_color,
        var aspectRatioTitleFont: Int = R.font.roboto_medium,
    ) : Serializable

    class BottomBuilder(
        var cropBottomBackgroundColor: Int = R.color.white,
        var dividerColor: Int = R.color.extra_extra_light_gray_color,
        var cropDoneButtonBuilder: ButtonBuilder = ButtonBuilder(buttonText = "Crop"),
        var cropCancelButtonBuilder: ButtonBuilder = ButtonBuilder(buttonText = "Skip"),
    ) : Serializable

    class ButtonBuilder(
        var textColor: Int = R.color.black,
        var icon: Int = R.drawable.ic_check_croppy_selected,
        var buttonText: String = "Crop",
        var textFont: Int = R.font.roboto_medium,
        var textSize: Float = 16F,
    ) : Serializable


}