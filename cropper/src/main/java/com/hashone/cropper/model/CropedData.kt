package com.hashone.cropper.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.hashone.cropper.R
import kotlinx.parcelize.Parcelize
import java.io.Serializable

class CropDataSaved : Serializable {
    var originalImg: String = ""
    var cropImg: String = ""
//    var originalImgBitmap: Bitmap? = null
//    var cropImgBitmap: Bitmap? = null
    var cropImgBitmap: ByteArray? = null

    var zoom: Float = 1F
    var basePx: Float = 1F
    var basePy: Float = 1F
    var panX: Float = 0F
    var panY: Float = 0F
    var rotation: Float = 0F
    var aspectRatio: Float = AspectRatio.Original.value
    var cropOutlineType: String = OutlineType.Rect.name
    var cropOutlineId: Int = 0
    var cropOutlineTitle: String = "Rect"

    var cropAspectRatioId: Int = 0
    var cropAspectRatioTitle: String = "Original"
    var cropAspectRatioImg: Int = R.drawable.ic_aspect_orginal

    var cropRectTop: Float = 0F
    var cropRectBottom: Float = 0F
    var cropRectLeft: Float = 0F
    var cropRectRight: Float = 0F

    var drawRectTop: Float = 0F
    var drawRectBottom: Float = 0F
    var drawRectLeft: Float = 0F
    var drawRectRight: Float = 0F

    var overlayRectTop: Float = 0F
    var overlayRectBottom: Float = 0F
    var overlayRectLeft: Float = 0F
    var overlayRectRight: Float = 0F
    var containerSizeWidth: Int = 0
    var containerSizeHeight: Int = 0
    var drawAreaSizeWidth: Int = 0
    var drawAreaSizeHeight: Int = 0
    var imageSizeWidth: Int = 0
    var imageSizeHeight: Int = 0

}