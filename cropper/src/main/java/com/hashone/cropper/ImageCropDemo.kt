package com.hashone.cropper

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.scale
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hashone.commons.utils.EXTENSION_PNG
import com.hashone.cropper.builder.Crop
import com.hashone.cropper.model.AspectRatio
import com.hashone.cropper.model.BaseAspectRatioData
import com.hashone.cropper.model.CropDataSaved
import com.hashone.cropper.model.OutlineType
import com.hashone.cropper.model.RectCropShape
import com.hashone.cropper.model.aspectRatios
import com.hashone.cropper.model.createCropOutlineContainer
import com.hashone.cropper.model.getRatioData
import com.hashone.cropper.settings.CropDefaults
import com.hashone.cropper.settings.CropOutlineProperty
import com.hashone.cropper.settings.CropProperties
import com.hashone.cropper.settings.CropStyle
import com.hashone.cropper.state.CropState
import com.hashone.cropper.theme.CircularIndeterminateProgressBar
import com.hashone.cropper.util.FileUtils
import com.hashone.cropper.util.Utils
import com.hashone.cropper.util.copy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
//@Preview
@Composable
fun ImageCropDemo(cropBuilder: Crop.Builder) {
    val activity = (LocalContext.current as? ComponentActivity)
    val cropStyle by remember { mutableStateOf(CropDefaults.style(strokeWidth = cropBuilder.screenBuilder.borderWidth.dp)) }
    val handleSize: Float = LocalDensity.current.run { 15.dp.toPx() }
    var resetCrop by remember { mutableStateOf(false) }
    var aspectRatioChange by remember { mutableStateOf(false) }
    var onShapeChange by remember { mutableStateOf(false) }
    var loadImage by remember { mutableStateOf(true) }
    var crop by remember { mutableStateOf(false) }

    val defaultCropOuterProp = CropOutlineProperty(
        OutlineType.Rect,
        RectCropShape(0, "Rect")
    )

    val cropOutlineProperty = if (cropBuilder.cropDataSaved != null) {
        val aspectRatio =
            aspectRatios.firstOrNull { it.id == cropBuilder.cropDataSaved?.cropAspectRatioId }
        if (aspectRatio != null) {
            CropOutlineProperty(
                OutlineType.valueOf(cropBuilder.cropDataSaved?.cropOutlineType!!),
                aspectRatio.cropOutline
            )
        } else {
            CropOutlineProperty(
                OutlineType.valueOf(cropBuilder.cropDataSaved?.cropOutlineType!!),
                createCropOutlineContainer(OutlineType.valueOf(cropBuilder.cropDataSaved?.cropOutlineType!!))
            )
        }
    } else defaultCropOuterProp
    var cropProperties by remember {
        mutableStateOf(
            CropDefaults.properties(
                cropOutlineProperty = if (cropBuilder.cropDataSaved != null) cropOutlineProperty else defaultCropOuterProp,
                handleSize = handleSize,
                fling = false,
                aspectRatio = if (cropBuilder.cropDataSaved != null) (if (cropBuilder.cropDataSaved?.aspectRatio != null) AspectRatio(
                    cropBuilder.cropDataSaved?.aspectRatio!!
                ) else aspectRatios[0].aspectRatio) else aspectRatios[0].aspectRatio,
                cropAspectRatio = if (cropBuilder.cropDataSaved != null) {
                    getRatioData(
                        OutlineType.valueOf(cropBuilder.cropDataSaved?.cropOutlineType!!),
                        cropBuilder.cropDataSaved!!
                    )
                } else {
                    aspectRatios[0]
                },
                zoom = if (cropBuilder.cropDataSaved != null) cropBuilder.cropDataSaved!!.zoom else 1F,
                basePx = if (cropBuilder.cropDataSaved != null) cropBuilder.cropDataSaved!!.basePx else 0F,
                basePy = if (cropBuilder.cropDataSaved != null) cropBuilder.cropDataSaved!!.basePy else 0F,
            )
        )
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                modifier = Modifier,
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = cropBuilder.toolBarBuilder.title.ifEmpty {
                            stringResource(id = R.string.crop_label_crop)
                        },
                        fontSize = cropBuilder.toolBarBuilder.titleSize.sp,
                        color = colorResource(cropBuilder.toolBarBuilder.titleColor),
                        fontFamily = FontFamily(Font(cropBuilder.toolBarBuilder.titleFont))
                    )
                },

                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(
                            ImageVector.vectorResource(id = cropBuilder.toolBarBuilder.backIcon),
                            contentDescription = stringResource(id = R.string.crop_label_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(
                        cropBuilder.toolBarBuilder.toolBarColor
                    )
                )
            )
        },
    ) {
        MainContent(
            cropBuilder,
            Modifier.padding(it),
            cropProperties,
            cropStyle,
            resetCrop,
            aspectRatioChange,
            onShapeChange,
            onAspectRatioChangeStart = {
                aspectRatioChange = false
            },
            onAspectRatioChange = {
                aspectRatioChange = true
            },
            onShapeChange = {
                onShapeChange = true
            },
            onCropReset = {
                resetCrop = false
            },
            onLoadImage = {
                loadImage = false
            },
            onCropTapped = {
                crop = it
            }
        ) {
            cropProperties = it
        }

        CircularIndeterminateProgressBar(resetCrop || loadImage)
    }
}

@Composable
private fun MainContent(
    cropBuilder: Crop.Builder,
    modifier: Modifier,
    cropProperties: CropProperties,
    cropStyle: CropStyle,
    resetCrop: Boolean = false,
    aspectRationChange: Boolean = false,
    shapeChange: Boolean = false,
    onAspectRatioChangeStart: () -> Unit,
    onAspectRatioChange: () -> Unit,
    onShapeChange: () -> Unit,
    onLoadImage: () -> Unit,
    onCropReset: () -> Unit,
    onCropTapped: (Boolean) -> Unit,
    onCropPropertiesChange: (CropProperties) -> Unit
) {
    val activity = (LocalContext.current as? Activity)
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var croppedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var isBitmapReady by remember { mutableStateOf(false) }
    var hashChange by remember { mutableStateOf(false) }
    var mOriginalBitmap: Bitmap? = null

    var mCropState by remember { mutableStateOf<CropState?>(null) }
    var crop by remember { mutableStateOf(false) }
    var isCropping by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val localBitmap = if (mOriginalBitmap != null) {
                mOriginalBitmap!!.copy()
            } else {
                val bitmap = Glide.with(activity!!)
                    .asBitmap()
                    .load(cropBuilder.originalImageFilePath)
                    .apply(
                        RequestOptions()
                            .override(cropBuilder.sizeBuilder.localFileSize)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    )
                    .submit().get()

                val maxSize = max(bitmap.width, bitmap.height)
                if (maxSize > cropBuilder.sizeBuilder.maxFileSize) {
                    if (bitmap.height > bitmap.width) {
                        bitmap.scale(
                            (cropBuilder.sizeBuilder.localFileSize * bitmap.width) / bitmap.height,
                            cropBuilder.sizeBuilder.localFileSize,
                            true
                        )
                    } else {
                        bitmap.scale(
                            cropBuilder.sizeBuilder.localFileSize,
                            (cropBuilder.sizeBuilder.localFileSize * bitmap.height) / bitmap.width,
                            true
                        )
                    }
                } else {
                    bitmap
                }
                bitmap
            }

            imageBitmap = localBitmap!!.asImageBitmap()
            mOriginalBitmap = imageBitmap!!.asAndroidBitmap()
            delay(700)
            isBitmapReady = true
        }
        onLoadImage()
    }

    val cropData by remember {
        mutableStateOf(
            if (cropBuilder.cropDataSaved != null) {
                CropData(
                    cropRect = Rect(
                        cropBuilder.cropDataSaved?.cropRectLeft!!,
                        cropBuilder.cropDataSaved?.cropRectTop!!,
                        cropBuilder.cropDataSaved?.cropRectRight!!,
                        cropBuilder.cropDataSaved?.cropRectBottom!!
                    ),
                    overlayRect = Rect(
                        cropBuilder.cropDataSaved?.overlayRectLeft!!,
                        cropBuilder.cropDataSaved?.overlayRectTop!!,
                        cropBuilder.cropDataSaved?.overlayRectRight!!,
                        cropBuilder.cropDataSaved?.overlayRectBottom!!
                    ),
                    drawRect = Rect(
                        cropBuilder.cropDataSaved?.drawRectLeft!!,
                        cropBuilder.cropDataSaved?.drawRectTop!!,
                        cropBuilder.cropDataSaved?.drawRectRight!!,
                        cropBuilder.cropDataSaved?.drawRectBottom!!
                    ),
                    imageSize = IntSize(
                        cropBuilder.cropDataSaved?.imageSizeWidth!!,
                        cropBuilder.cropDataSaved?.imageSizeHeight!!
                    ),
                    drawSize = IntSize(
                        cropBuilder.cropDataSaved?.drawAreaSizeWidth!!,
                        cropBuilder.cropDataSaved?.drawAreaSizeHeight!!
                    ),
                    overlaySize = IntSize(
                        cropBuilder.cropDataSaved?.drawAreaSizeWidth!!,
                        cropBuilder.cropDataSaved?.drawAreaSizeHeight!!
                    ),
                    containerSize = IntSize(
                        cropBuilder.cropDataSaved?.containerSizeWidth!!,
                        cropBuilder.cropDataSaved?.containerSizeHeight!!
                    ),
                    aspectRatio = AspectRatio(cropBuilder.cropDataSaved?.aspectRatio!!),
                    cropAspectRatio =
                    getRatioData(
                        OutlineType.valueOf(cropBuilder.cropDataSaved?.cropOutlineType!!),
                        cropBuilder.cropDataSaved!!
                    ),
                    cropOutlineProperty = CropOutlineProperty(
                        OutlineType.valueOf(cropBuilder.cropDataSaved?.cropOutlineType!!),
                        createCropOutlineContainer(OutlineType.valueOf(cropBuilder.cropDataSaved?.cropOutlineType!!))
                    )
                )
            } else null
        )
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = cropBuilder.screenBuilder.windowBackgroundColor))
    ) {
        LaunchedEffect(Unit) {
            snapshotFlow {

            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F)
                    .padding(30.dp, 22.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                if (isBitmapReady) {
                    ImageCropper(
                        modifier = Modifier.clipToBounds(),
                        imageBitmap = imageBitmap!!,
                        contentDescription = stringResource(id = R.string.crop_label_crop),
                        cropStyle = cropStyle,
                        cropProperties = cropProperties,
                        crop = crop,
                        aspectRationChange = aspectRationChange,
                        shapeChange = shapeChange,
                        mCropData = cropData,
                        resetCrop = resetCrop,
                        cropBuilder = cropBuilder,
                        onCropStart = {
                            isCropping = true
                        },
                        onHashUpdate = {
                            hashChange = it
                        },
                        onCropReset = {
                            onCropReset()
                        },
                    ) { imageBitmap, cropState ->
                        croppedImage = imageBitmap
                        mCropState = cropState
                        crop = false
                        cropBuilder.cropState = cropState
                        croppedImage?.let {
                            cropBuilder.croppedImageBitmap = croppedImage!!.asAndroidBitmap()
                            cropState?.cropAspectRatio = cropProperties.cropAspectRatio
                            cropState?.aspectRatio = cropProperties.aspectRatio
                            cropState?.cropOutlineProperty = cropProperties.cropOutlineProperty
                            val newCropSavedData = getCropStateData(cropState)
                            activity?.let { myActivity ->

                                val finalBitmap = Bitmap.createScaledBitmap(
                                    croppedImage!!.asAndroidBitmap(),
                                    (mCropState!!.cropRect.width).roundToInt(),
                                    (mCropState!!.cropRect.height).roundToInt(),
                                    true
                                )
                                newCropSavedData!!.originalImg = cropBuilder.originalImageFilePath
                                if (finalBitmap != null) {
                                    finalBitmap.setHasAlpha(true)
                                    newCropSavedData.cropImg = FileUtils.saveBitmapToFile(
                                        finalBitmap,
                                        "Crop_" + System.currentTimeMillis() + "." + EXTENSION_PNG,
                                        FileUtils.getImageDir(activity)
                                    ).absolutePath
                                }

                                myActivity.setResult(Activity.RESULT_OK, Intent().apply {
                                    putExtra(CropActivity.KEY_RETURN_CROP_DATA, newCropSavedData)
                                })
                                myActivity.finish()
                            }
                        }

                        isCropping = false
                        onCropTapped(false)
                    }
                }
            }

            if (aspectRatios.size > 1) {

                AspectRatioSelection(
                    cropBuilder = cropBuilder,
                    cropProperties = cropProperties,
                    aspectRationChange = aspectRationChange,
                    aspectRatio = cropProperties.cropAspectRatio,
                    activity = activity!!,
                    resetCrop = resetCrop,
                    onAspectRatioChangeStart = {
                        onAspectRatioChangeStart()
                    },
                    onAspectRatioChange = {

                    },
                    onCropPropertiesChange = {
                        onCropPropertiesChange(
                            cropProperties.copy(
                                cropOutlineProperty = it.cropOutlineProperty,
                                cropAspectRatio = it.cropAspectRatio,
                                aspectRatio = it.cropAspectRatio.aspectRatio,
                                fixedAspectRatio = (it.aspectRatio != AspectRatio.Original)
                            )
                        )
                        onAspectRatioChange()
                    },
                    onCropReset = {
                        onCropReset()
                    }
                )
            }
            Spacer(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(colorResource(cropBuilder.bottomPanelBuilder.dividerColor))
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .background(colorResource(cropBuilder.bottomPanelBuilder.cropBottomBackgroundColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (it == 1) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        activity.let {
                                            it?.setResult(Activity.RESULT_OK, Intent())
                                            it?.finish()
                                        }
                                    },
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    ImageVector.vectorResource(id = cropBuilder.bottomPanelBuilder.cancelButtonBuilder.icon),
                                    contentDescription = stringResource(id = R.string.crop_label_skip),
                                )

                                Text(
                                    modifier = Modifier.padding(paddingValues = PaddingValues(8.dp)),
                                    text = cropBuilder.bottomPanelBuilder.cancelButtonBuilder.buttonText.ifEmpty {
                                        stringResource(
                                            id = R.string.crop_label_skip
                                        )
                                    },
                                    fontSize = cropBuilder.bottomPanelBuilder.cancelButtonBuilder.textSize.sp,
                                    color = colorResource(cropBuilder.bottomPanelBuilder.cancelButtonBuilder.textColor),
                                    fontFamily = FontFamily(Font(cropBuilder.bottomPanelBuilder.cancelButtonBuilder.textFont))
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        if (hashChange || cropBuilder.forceCrop) {
                                            if (Utils.checkClickTime1()) {
                                                isCropping = true
                                                crop = true
                                                onCropTapped(true)
                                            }
                                        } else {
                                            activity.let {
                                                it?.setResult(Activity.RESULT_OK, Intent())
                                                it?.finish()
                                            }
                                        }
                                    },
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    ImageVector.vectorResource(id = cropBuilder.bottomPanelBuilder.doneButtonBuilder.icon),
                                    contentDescription = stringResource(id = R.string.crop_label_done),
                                )
                                Text(
                                    modifier = Modifier.padding(paddingValues = PaddingValues(8.dp)),
                                    text = cropBuilder.bottomPanelBuilder.doneButtonBuilder.buttonText.ifEmpty {
                                        stringResource(
                                            id = R.string.crop_label_crop
                                        )
                                    },
                                    fontSize = cropBuilder.bottomPanelBuilder.doneButtonBuilder.textSize.sp,
                                    color = colorResource(cropBuilder.bottomPanelBuilder.doneButtonBuilder.textColor),
                                    fontFamily = FontFamily(Font(cropBuilder.bottomPanelBuilder.doneButtonBuilder.textFont))
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    CircularIndeterminateProgressBar(isDisplayed = isCropping)

}

fun getCropStateData(cropState: CropState?): CropDataSaved? {
    if (cropState != null) {
        val cropDataSaved = CropDataSaved()
        cropDataSaved.aspectRatio = cropState.aspectRatio.value

        aspectRatios.forEach {
            if (it.title == cropState.cropAspectRatio.title) {
                cropDataSaved.cropAspectRatioId = it.id
                cropDataSaved.cropAspectRatioTitle = it.title
                cropDataSaved.cropAspectRatioImg = it.img
            }

        }

        cropDataSaved.cropOutlineType = cropState.cropOutlineProperty.outlineType.name
        cropDataSaved.cropOutlineId = cropState.cropOutlineProperty.cropOutline.id
        cropDataSaved.cropOutlineTitle = cropState.cropOutlineProperty.cropOutline.title

        cropDataSaved.zoom = cropState.zoom
        cropDataSaved.basePx = cropState.animatablePanX.value
        cropDataSaved.basePy = cropState.animatablePanY.value
        cropDataSaved.panX = cropState.pan.x
        cropDataSaved.panY = cropState.pan.y
        cropDataSaved.rotation = cropState.rotation

        cropDataSaved.cropRectLeft = cropState.cropRect.left
        cropDataSaved.cropRectTop = cropState.cropRect.top
        cropDataSaved.cropRectRight = cropState.cropRect.right
        cropDataSaved.cropRectBottom = cropState.cropRect.bottom

        cropDataSaved.drawRectLeft = cropState.drawAreaRect.left
        cropDataSaved.drawRectTop = cropState.drawAreaRect.top
        cropDataSaved.drawRectRight = cropState.drawAreaRect.right
        cropDataSaved.drawRectBottom = cropState.drawAreaRect.bottom

        cropDataSaved.overlayRectLeft = cropState.overlayRect.left
        cropDataSaved.overlayRectTop = cropState.overlayRect.top
        cropDataSaved.overlayRectRight = cropState.overlayRect.right
        cropDataSaved.overlayRectBottom = cropState.overlayRect.bottom

        cropDataSaved.containerSizeWidth = cropState.containerSize.width
        cropDataSaved.containerSizeHeight = cropState.containerSize.height

        cropDataSaved.drawAreaSizeWidth = cropState.drawAreaSize.width
        cropDataSaved.drawAreaSizeHeight = cropState.drawAreaSize.height

        cropDataSaved.imageSizeWidth = cropState.imageSize.width
        cropDataSaved.imageSizeHeight = cropState.imageSize.height
        return cropDataSaved
    } else return null
}


@Composable
internal fun AspectRatioSelection(
    cropBuilder: Crop.Builder,
    cropProperties: CropProperties,
    aspectRationChange: Boolean = false,
    aspectRatio: BaseAspectRatioData,
    activity: Activity,
    resetCrop: Boolean = false,
    onCropPropertiesChange: (CropProperties) -> Unit,
    onAspectRatioChangeStart: () -> Unit,
    onAspectRatioChange: () -> Unit,
    onCropReset: () -> Unit,
) {

    val initialSelectedIndex = remember {
        aspectRatios.indexOf(aspectRatios.first { it.id == aspectRatio.id })
    }

    val cropOutlineProperty = cropProperties.cropOutlineProperty
    AnimatedAspectRatioSelection(
        cropBuilder = cropBuilder,
        cropOutlineProperty = cropOutlineProperty,
        initialSelectedIndex = initialSelectedIndex,
        activity = activity,
        onAspectRatioChangeStart = {
            onAspectRatioChangeStart()
        },
        onCropReset = {
            onCropReset()
        },
        conCropOutlinePropertyChange = { myCropOutlineProperty, myAspectRatio ->
            onAspectRatioChange()
            onCropPropertiesChange(
                cropProperties.copy(
                    cropOutlineProperty = myCropOutlineProperty,
                    cropAspectRatio = myAspectRatio,
                    aspectRatio = myAspectRatio.aspectRatio
                )
            )
        },
        resetCrop = resetCrop,
    )
}