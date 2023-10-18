package com.hashone.cropper

import android.app.Activity
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.hashone.commons.utils.dpToPx
import com.hashone.cropper.builder.Crop
import com.hashone.cropper.settings.CropFrameFactory
import com.hashone.cropper.model.CropAspectRatio
import com.hashone.cropper.model.OutlineType
import com.hashone.cropper.model.RectCropShape
import com.hashone.cropper.model.aspectRatios
import com.hashone.cropper.model.createCropOutlineContainer
import com.hashone.cropper.settings.CropOutlineProperty
import com.hashone.cropper.util.Utils
import com.hashone.cropper.widget.AspectRatioSelectionCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class CustomRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(
        draggedAlpha = 0f,
        focusedAlpha = 0f,
        hoveredAlpha = 0f,
        pressedAlpha = 0f,
    )
}

@Composable
internal fun AnimatedAspectRatioSelection(
    cropBuilder: Crop.Builder,
    cropOutlineProperty: CropOutlineProperty,
    initialSelectedIndex: Int = 0,
    activity: Activity,
    resetCrop: Boolean = false,
    onAspectRatioChangeStart: () -> Unit,
    onCropReset: () -> Unit,
    conCropOutlinePropertyChange: (CropOutlineProperty, CropAspectRatio) -> Unit,
) {

    var isAspectRatioChangeManually by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(initialSelectedIndex) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        snapshotFlow {
            if (resetCrop) {
                currentIndex = 0
                conCropOutlinePropertyChange(
                    CropOutlineProperty(
                        OutlineType.Rect,
                        RectCropShape(0, "Rect")
                    ), aspectRatios[0]
                )
                onCropReset()
            }
        }
    }

    BoxWithConstraints {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = cropBuilder.aspectRatioBuilder.backgroundColor))
            ,
            state = listState,
        ) {


            itemsIndexed(aspectRatios) { index: Int, item: CropAspectRatio ->
                CompositionLocalProvider(LocalRippleTheme provides CustomRippleTheme()) {
                    Layout(
                        content = {
                            val currentHeight =
                                0.1555555555555556 * Utils.getScreenWidth(activity).toDouble()
                            val viewRatio = if (aspectRatios.size <= 6) {
                                (Utils.getScreenWidth(activity) / aspectRatios.size.toFloat()) / currentHeight
                            } else {
                                (Utils.getScreenWidth(activity) / 5.4F) / currentHeight
                            }
                            // Here's the content of each list item.
                            AspectRatioSelectionCard(
                                modifier = Modifier
                                    .width(Utils.pxToDp((currentHeight * viewRatio).roundToInt()).dp)
//                                    .defaultMinSize(minWidth = Utils.pxToDp((currentHeight * viewRatio).roundToInt()).dp)
//                                    .height(56.dp)
                                    .clip(CircleShape)
                                    .clickable(
                                        indication = rememberRipple(),
                                        interactionSource = remember {
                                            MutableInteractionSource()
                                        }
                                    ) {
                                        if (Utils.checkClickTime300()) {
                                            isAspectRatioChangeManually = true
                                            if (currentIndex != index) {
                                                currentIndex = index
                                                conCropOutlinePropertyChange(
//                                                    aspectRatios[index].cropOutlineProperty,
                                                    CropOutlineProperty(
                                                        aspectRatios[index].outlineType,
//                                                        if (aspectRatios[index].outlineType != OutlineType.ImageMask) createCropOutlineContainer(aspectRatios[index].outlineType) else aspectRatios[index].cropOutline
                                                        aspectRatios[index].cropOutline
//                                                        createCropOutlineContainer(aspectRatios[index].outlineType)
                                                    ),
                                                    aspectRatios[index]
                                                )
                                                coroutineScope.launch {
                                                    val itemInfo =
                                                        listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentIndex }
                                                    val center =
                                                        listState.layoutInfo.viewportEndOffset / 2
                                                    val childCenter =
                                                        itemInfo!!.offset + itemInfo.size / 2
                                                    listState.animateScrollBy(
                                                        (childCenter - center).toFloat(),
                                                        tween(300)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    .padding(8.dp),
                                bgColor = colorResource(id = cropBuilder.aspectRatioBuilder.backgroundColor),
                                itemColor = colorResource(id = if (currentIndex == index) cropBuilder.aspectRatioBuilder.selectedColor else cropBuilder.aspectRatioBuilder.unSelectedColor),
                                cropAspectRatio = item,
                                font = FontFamily(Font(cropBuilder.aspectRatioBuilder.titleFont)),
                                titleSize = cropBuilder.aspectRatioBuilder.titleSize,
                            )
                        },
                        measurePolicy = { measures, constraints ->
                            // so it's measuring just the Box
                            val placeable = measures.first().measure(constraints)
                            layout(placeable.width, placeable.height) {
                                // Placing the Box in the right X position
                                placeable.place(dpToPx(0f).toInt(), 0)
                            }
                        }
                    )
                }
            }

            if (!isAspectRatioChangeManually)
                coroutineScope.launch {
                    val itemInfo = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentIndex }
                    if (itemInfo != null) {
                        val center = listState.layoutInfo.viewportEndOffset / 2
                        val childCenter = itemInfo.offset + itemInfo.size / 2
                        listState.animateScrollBy((childCenter - center).toFloat())
                    } else {
                        listState.animateScrollToItem(currentIndex)
                    }
                }
        }
    }
}