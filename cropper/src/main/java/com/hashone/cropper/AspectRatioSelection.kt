package com.hashone.cropper

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.hashone.cropper.builder.Crop
import com.hashone.cropper.settings.CropFrameFactory
import com.hashone.cropper.model.CropAspectRatio
import com.hashone.cropper.model.CropOutline
import com.hashone.cropper.model.CropOutlineContainer
import com.hashone.cropper.model.CustomOutlineContainer
import com.hashone.cropper.model.CustomPathOutline
import com.hashone.cropper.model.CutCornerCropShape
import com.hashone.cropper.model.CutCornerRectOutlineContainer
import com.hashone.cropper.model.OutlineType
import com.hashone.cropper.model.OvalCropShape
import com.hashone.cropper.model.OvalOutlineContainer
import com.hashone.cropper.model.PolygonCropShape
import com.hashone.cropper.model.PolygonOutlineContainer
import com.hashone.cropper.model.PolygonProperties
import com.hashone.cropper.model.RectCropShape
import com.hashone.cropper.model.RectOutlineContainer
import com.hashone.cropper.model.RoundedCornerCropShape
import com.hashone.cropper.model.RoundedRectOutlineContainer
import com.hashone.cropper.model.aspectRatios
import com.hashone.cropper.model.createCropOutlineContainer
import com.hashone.cropper.model.getOutlineContainer
import com.hashone.cropper.settings.CropOutlineProperty
import com.hashone.cropper.settings.Paths
import com.hashone.cropper.util.Utils
import com.hashone.cropper.util.createPolygonShape
import com.hashone.cropper.widget.AspectRatioSelectionCard
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
internal fun AnimatedAspectRatioSelection(
    cropBuilder: Crop.Builder,
    cropFrameFactory: CropFrameFactory,
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


    if (resetCrop) {
        currentIndex = 0
        conCropOutlinePropertyChange(
            CropOutlineProperty(
                OutlineType.Rect,
                RectCropShape(0, "Rect")
            ), aspectRatios[0]
        )
//        listState.animateScrollToItem(initialSelectedIndex)
        onCropReset()
    }

    BoxWithConstraints {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            state = listState,
        ) {
            itemsIndexed(aspectRatios) { index: Int, item: CropAspectRatio ->
                Layout(
                    content = {
                        val currentHeight =
                            0.1555555555555556 * Utils.getScreenWidth(activity).toDouble()
                        val viewRatio = if (aspectRatios.size <= 6) {
                            (Utils.getScreenWidth(activity) / aspectRatios.size.toFloat()) / currentHeight
                        } else {
                            (Utils.getScreenWidth(activity) / 5.1555F) / currentHeight
                        }
                        // Here's the content of each list item.
                        AspectRatioSelectionCard(
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = 1f
                                    scaleY = 1f
                                }
                                .width(Utils.pxToDp((currentHeight * viewRatio).roundToInt()).dp)
                                .clickable {
                                    if(Utils.checkClickTime300()) {
                                        isAspectRatioChangeManually = true
                                        if (currentIndex != index) {
                                            currentIndex = index
                                             conCropOutlinePropertyChange(
                                                 CropOutlineProperty(
                                                     aspectRatios[index].outlineType,
                                                     createCropOutlineContainer(aspectRatios[index].outlineType)
                                                 ), aspectRatios[index]
                                            )

                                        }
                                    }
                                },
                            bgColor = colorResource(id = cropBuilder.aspectRatioBackgroundColor),
                            itemColor = colorResource(id = if (currentIndex == index) cropBuilder.aspectRatioSelectedColor else cropBuilder.aspectRatioUnSelectedColor),
                            cropAspectRatio = item,
                            font = FontFamily(Font(cropBuilder.aspectRatioTitleFont)),
                        )

                    },
                    measurePolicy = { measures, constraints ->
                        // so it's measuring just the Box
                        val placeable = measures.first().measure(constraints)
                        layout(placeable.width, placeable.height) {
                            // Placing the Box in the right X position
                            placeable.place(0, 0)
                        }
                    }
                )

            }

//            if (currentIndex == 0)
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