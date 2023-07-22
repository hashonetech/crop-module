package com.hashone.cropper.builder

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import com.hashone.cropper.CropActivity
import com.hashone.cropper.R
import com.hashone.cropper.model.CropDataSaved
import com.hashone.cropper.state.CropState
import java.io.Serializable

open class Crop(val builder: Builder) : Serializable {

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
        var bottomPanelBuilder = BottomPanelBuilder()

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
        var cropOuterBorderColor: Int = R.color.un_select_color,
        var borderWidth: Float = 1F,
        var borderSpacing: Float = 2F
    ) : Serializable

    class ToolBarBuilder(
        var toolBarColor: Int = R.color.white,
        var backIcon: Int = R.drawable.ic_back,
        var title: String = "Crop",
        var titleColor: Int = R.color.black,
        var titleFont: Int = R.font.outfit_regular,
        var titleSize: Float = 16F,
    ) : Serializable

    class AspectRatioBuilder(
        var backgroundColor: Int = R.color.white,
        var selectedColor: Int = R.color.black,
        var unSelectedColor: Int = R.color.un_select_color,
        var titleFont: Int = R.font.roboto_medium,
    ) : Serializable

    class BottomPanelBuilder(
        var cropBottomBackgroundColor: Int = R.color.white,
        var dividerColor: Int = R.color.extra_extra_light_gray_color,
        var doneButtonBuilder: ButtonBuilder = ButtonBuilder(buttonText = "Crop"),
        var cancelButtonBuilder: ButtonBuilder = ButtonBuilder(buttonText = "Skip"),
    ) : Serializable

    class ButtonBuilder(
        var textColor: Int = R.color.black,
        var icon: Int = R.drawable.ic_check_croppy_selected,
        var buttonText: String,
        var textFont: Int = R.font.roboto_medium,
        var textSize: Float = 16F,
    ) : Serializable


}