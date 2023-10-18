package com.hashone.cropper

//import CropFrameListDialog
import android.app.Activity
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hashone.commons.utils.dpToPx
import com.hashone.cropper.builder.Crop
import com.hashone.cropper.model.AspectRatio
import com.hashone.cropper.model.CropFrame
import com.hashone.cropper.model.OutlineType
import com.hashone.cropper.settings.CropFrameFactory
import com.hashone.cropper.settings.CropOutlineProperty
import com.hashone.cropper.util.Utils
import com.hashone.cropper.widget.CropFrameDisplayCard
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


/**
 * Crop frame selection
 */
@Composable
fun CropFrameSelection(
    cropBuilder: Crop.Builder,
    aspectRatio: AspectRatio,
    cropFrameFactory: CropFrameFactory,
    cropOutlineProperty: CropOutlineProperty,
    activity: Activity,
    conCropOutlinePropertyChange: (CropOutlineProperty) -> Unit
) {

    var showEditDialog by remember { mutableStateOf(false) }

    var cropFrame by remember {
        mutableStateOf(
            cropFrameFactory.getCropFrame(cropOutlineProperty.outlineType)
        )
    }

    val initialIndex = remember {
        OutlineType.values().indexOfFirst {
            it == cropOutlineProperty.outlineType
        }
    }

    CropFrameSelectionList(
        cropBuilder = cropBuilder,
        modifier = Modifier.fillMaxWidth(),
        cropFrames = cropFrameFactory.getCropFrames(),
        initialSelectedIndex = initialIndex,
        activity = activity,
        onClick = {
            cropFrame = it
            showEditDialog = true
        },
        onCropFrameChange = {
            conCropOutlinePropertyChange(
                CropOutlineProperty(
                    it.outlineType,
                    it.cropOutline
                )
            )
        }
    )
}

/**
 * Animated list for selecting [CropFrame]
 */
@Composable
private fun CropFrameSelectionList(
    cropBuilder: Crop.Builder,
    modifier: Modifier = Modifier,
    initialSelectedIndex: Int = 0,
    cropFrames: List<CropFrame>,
    activity: Activity,
    onClick: (CropFrame) -> Unit,
    onCropFrameChange: (CropFrame) -> Unit
) {

    var currentIndex by remember { mutableStateOf(initialSelectedIndex) }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var isShapeChangeManually by remember { mutableStateOf(false) }



    BoxWithConstraints {
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .background(colorResource(id = cropBuilder.aspectRatioBuilder.backgroundColor)),
            state = listState,
        ) {
            itemsIndexed(cropFrames) { index: Int, item: CropFrame ->

                CompositionLocalProvider(LocalRippleTheme provides CustomRippleTheme()) {
                    Layout(
                        content = {
                            val currentHeight =
                                0.1555555555555556 * Utils.getScreenWidth(activity).toDouble()
                            val viewRatio = if (cropFrames.size <= 6) {
                                (Utils.getScreenWidth(activity) / cropFrames.size.toFloat()) / currentHeight
                            } else {
                                (Utils.getScreenWidth(activity) / 8F) / currentHeight
                            }

//                            val cropOutline = item.cropOutlineContainer.selectedItem
                            val cropOutline = item.cropOutline

//                            val editable = item.editable
                            val editable = false
                            // Here's the content of each list item.
                            CropFrameDisplayCard(
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                                    .width(Utils.pxToDp((currentHeight * viewRatio * 1.1).roundToInt()).dp)
                                    .clickable(
                                        indication = rememberRipple(),
                                        interactionSource = remember {
                                            MutableInteractionSource()
                                        },
//                                        indication = null
                                    ) {
                                       /* coroutineScope.launch {
//                                            lazyListState.animateScrollBy(animationProgress.distanceToSelector)
                                        }*/

                                        if (Utils.checkClickTime300()) {
                                            isShapeChangeManually = true
                                            if (currentIndex != index) {
                                                currentIndex = index
//                                                conCropOutlinePropertyChange(
//                                                    CropOutlineProperty(
//                                                        aspectRatios[index].outlineType,
//                                                        createCropOutlineContainer(aspectRatios[index].outlineType)
//                                                    ), aspectRatios[index]
//                                                )
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
                                                    onCropFrameChange(cropFrames[index])

                                                }
                                            }
                                        }
                                    },
                                editable = editable,
                                scale = 1f,
                                outlineColor = Color.Black,
                                title = cropOutline.title,
                                cropOutline = cropOutline,
                                bgColor = colorResource(id = cropBuilder.aspectRatioBuilder.backgroundColor),
                                itemColor = colorResource(id = if (currentIndex == index) cropBuilder.aspectRatioBuilder.selectedColor else cropBuilder.aspectRatioBuilder.unSelectedColor),
                                font = FontFamily(Font(cropBuilder.aspectRatioBuilder.titleFont)),
                                fontSize = (cropBuilder.aspectRatioBuilder.titleSize).sp,
                            ) {
                                onClick(item)
                            }
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

//                if (currentIndex != selectedLocalIndex) {
//                    currentIndex = selectedLocalIndex
//                    onCropFrameChange(cropFrames[selectedLocalIndex])
//                }
            }

//              if (!isShapeChangeManually)
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
