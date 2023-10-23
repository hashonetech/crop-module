package com.hashone.cropper.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.unit.IntSize
import com.hashone.cropper.TouchRegion
import com.hashone.cropper.model.AspectRatio
import com.hashone.cropper.model.BaseAspectRatioData
import com.hashone.cropper.settings.CropOutlineProperty
import com.hashone.cropper.settings.CropProperties
import kotlinx.coroutines.coroutineScope

/**
 *  * State for cropper with dynamic overlay. When this state is selected instead of overlay
 *  image is moved while overlay is stationary.
 *
 * @param imageSize size of the **Bitmap**
 * @param containerSize size of the Composable that draws **Bitmap**
 * @param maxZoom maximum zoom value
 * @param fling when set to true dragging pointer builds up velocity. When last
 * pointer leaves Composable a movement invoked against friction till velocity drops below
 * to threshold
 * @param zoomable when set to true zoom is enabled
 * @param pannable when set to true pan is enabled
 * @param rotatable when set to true rotation is enabled
 * @param limitPan limits pan to bounds of parent Composable. Using this flag prevents creating
 * empty space on sides or edges of parent
 */
class StaticCropState internal constructor(
    private var handleSize: Float,
    imageSize: IntSize,
    containerSize: IntSize,
    drawAreaSize: IntSize,
    aspectRatio: AspectRatio,
    cropAspectRatio: BaseAspectRatioData,
    cropOutlineProperty: CropOutlineProperty,
    overlayRatio: Float,
    maxZoom: Float = 5f,
    fling: Boolean = false,
    zoomable: Boolean = true,
    pannable: Boolean = true,
    rotatable: Boolean = false,
    limitPan: Boolean = false
) : CropState(
    imageSize = imageSize,
    containerSize = containerSize,
    drawAreaSize = drawAreaSize,
    aspectRatio = aspectRatio,
    overlayRatio = overlayRatio,
    maxZoom = maxZoom,
    fling = fling,
    zoomable = zoomable,
    pannable = pannable,
    rotatable = rotatable,
    limitPan = limitPan,
    cropOutlineProperty = cropOutlineProperty,
    cropAspectRatio = cropAspectRatio,

    ) {

    override suspend fun onDown(change: PointerInputChange) = Unit
    override suspend fun onMove(changes: List<PointerInputChange>) = Unit
    override suspend fun onUp(change: PointerInputChange) = Unit

    private var doubleTapped = false

    override suspend fun updateProperties(cropProperties: CropProperties, forceUpdate: Boolean) {
        handleSize = cropProperties.handleSize
        super.updateProperties(cropProperties, forceUpdate)
    }

    /*
        Transform gestures
    */
    override suspend fun onGesture(
        centroid: Offset,
        panChange: Offset,
        zoomChange: Float,
        rotationChange: Float,
        mainPointer: PointerInputChange,
        changes: List<PointerInputChange>
    ) = coroutineScope {
        doubleTapped = false


        val position = mainPointer.position
        val touchPositionScreenX = position.x
        val touchPositionScreenY = position.y

        val touchPositionOnScreen = Offset(touchPositionScreenX, touchPositionScreenY)

        // Get whether user touched outside, handles of rectangle or inner region or overlay
        // rectangle. Depending on where is touched we can move or scale overlay
        touchRegion = getTouchRegion(
            position = touchPositionOnScreen,
            rect = overlayRect,
            threshold = handleSize
        )

        updateTransformState(
            centroid = centroid,
            zoomChange = zoomChange,
            panChange = panChange,
            rotationChange = rotationChange
        )

        // Update image draw rectangle based on pan, zoom or rotation change
        drawAreaRect = updateImageDrawRectFromTransformation()

        // Fling Gesture
        if (pannable && fling) {
            if (changes.size == 1) {
                addPosition(mainPointer.uptimeMillis, mainPointer.position)
            }
        }
    }

    override suspend fun onGestureStart() = coroutineScope {}

    override suspend fun onGestureEnd(onBoundsCalculated: () -> Unit) {

        // Gesture end might be called after second tap and we don't want to fling
        // or animate back to valid bounds when doubled tapped
        if (!doubleTapped) {

            if (pannable && fling && zoom > 1) {
                fling {
                    // We get target value on start instead of updating bounds after
                    // gesture has finished
                    drawAreaRect = updateImageDrawRectFromTransformation()
                    onBoundsCalculated()
                }
            } else {
                onBoundsCalculated()
            }

            animateTransformationToOverlayBounds(overlayRect, animate = true)
        }
    }

    // Double Tap
    override suspend fun onDoubleTap(
        offset: Offset,
        zoom: Float,
        onAnimationEnd: () -> Unit
    ) {
        doubleTapped = true

        if (fling) {
            resetTracking()
        }
        resetWithAnimation(pan = pan, zoom = zoom, rotation = rotation)
        drawAreaRect = updateImageDrawRectFromTransformation()
        animateTransformationToOverlayBounds(overlayRect, true)
        onAnimationEnd()
    }

    private fun getTouchRegion(
        position: Offset,
        rect: Rect,
        threshold: Float
    ): TouchRegion {

        val closedTouchRange = -threshold / 2..threshold

        return when {
            position.x - rect.left in closedTouchRange &&
                    position.y - rect.top in closedTouchRange -> TouchRegion.TopLeft

            rect.right - position.x in closedTouchRange &&
                    position.y - rect.top in closedTouchRange -> TouchRegion.TopRight

            rect.right - position.x in closedTouchRange &&
                    rect.bottom - position.y in closedTouchRange -> TouchRegion.BottomRight

            position.x - rect.left in closedTouchRange &&
                    rect.bottom - position.y in closedTouchRange -> TouchRegion.BottomLeft


            rect.contains(offset = position) -> TouchRegion.Inside
            else -> TouchRegion.None
        }
    }

}
