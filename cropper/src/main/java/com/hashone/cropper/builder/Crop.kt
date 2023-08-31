package com.hashone.cropper.builder

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.FontRes
import androidx.annotation.IntRange
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
        @IntRange
        var localFileSize: Int = 1080,
        @IntRange
        var maxFileSize: Int = 4096,
    ) : Serializable

    class ScreenBuilder(
        @ColorRes
        var windowBackgroundColor: Int = R.color.window_bg_color,
        @ColorRes
        var statusBarColor: Int = R.color.extra_extra_light_gray_color,
        @ColorRes
        var navigationBarColor: Int = R.color.white,
        @ColorRes
        var cropOuterBorderColor: Int = R.color.un_select_color,
        @FloatRange
        var borderWidth: Float = 1F,
        @FloatRange
        var borderSpacing: Float = 2F
    ) : Serializable

    class ToolBarBuilder(
        @ColorRes
        var toolBarColor: Int = R.color.white,
        @DrawableRes
        var backIcon: Int = R.drawable.ic_back,
        var title: String = "Crop",
        @ColorRes
        var titleColor: Int = R.color.black,
        @FontRes
        var titleFont: Int = R.font.outfit_regular,
        @FloatRange
        var titleSize: Float = 16F,
    ) : Serializable

    class AspectRatioBuilder(
        @ColorRes
        var backgroundColor: Int = R.color.white,
        @ColorRes
        var selectedColor: Int = R.color.black,
        @ColorRes
        var unSelectedColor: Int = R.color.un_select_color,
        @FontRes
        var titleFont: Int = R.font.roboto_medium,
        @FloatRange
        var titleSize: Float = 12F,
    ) : Serializable

    class BottomPanelBuilder(
        @ColorRes
        var cropBottomBackgroundColor: Int = R.color.white,
        @ColorRes
        var dividerColor: Int = R.color.extra_extra_light_gray_color,
        var doneButtonBuilder: ButtonBuilder = ButtonBuilder(buttonText = "Crop"),
        var cancelButtonBuilder: ButtonBuilder = ButtonBuilder(buttonText = "Skip"),
    ) : Serializable

    class ButtonBuilder(
        @ColorRes
        var textColor: Int = R.color.black,
        @DrawableRes
        var icon: Int = R.drawable.ic_check_croppy_selected,
        var buttonText: String,
        @FontRes
        var textFont: Int = R.font.roboto_medium,
        @FloatRange
        var textSize: Float = 16F,
    ) : Serializable


}