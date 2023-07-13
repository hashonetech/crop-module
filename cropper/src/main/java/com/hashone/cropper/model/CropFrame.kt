package com.hashone.cropper.model

import androidx.compose.runtime.Immutable
import com.hashone.cropper.settings.Paths

/**
 * Model for drawing title with shape for crop selection menu.
 */
@Immutable
data class CropFrame(
    val outlineType: OutlineType,
    val editable: Boolean = false,
    val cropOutlineContainer: CropOutlineContainer<out CropOutline>
) {
    var selectedIndex: Int
        get() = cropOutlineContainer.selectedIndex
        set(value) {
            cropOutlineContainer.selectedIndex = value
        }

    val outlines: List<CropOutline>
        get() = cropOutlineContainer.outlines

    val outlineCount: Int
        get() = cropOutlineContainer.size

    fun addOutline(outline: CropOutline): CropFrame {
        outlines.toMutableList().add(outline)
       return this
    }
}



fun createCropOutlineContainer(
    outlineType: OutlineType
): CropOutline {
    return when (outlineType) {
        OutlineType.Rect -> (RectCropShape(id = 0, title = "Rect"))

        OutlineType.RoundedRect -> (RoundedCornerCropShape(id = 1, title = "Rounded"))

        OutlineType.CutCorner -> (CutCornerCropShape(id = 2, title = "CutCorner"))

        OutlineType.Oval -> (OvalCropShape(id = 3, title = "Oval"))

        OutlineType.Polygon -> PolygonCropShape(
            id = 0,
            title = "Polygon"
        )
//        {
//            PolygonOutlineContainer(
//                outlines = listOf(
//                    PolygonCropShape(
//                        id = 0,
//                        title = "Polygon"
//                    ),
//                    PolygonCropShape(
//                        id = 1,
//                        title = "Pentagon",
//                        polygonProperties = PolygonProperties(sides = 5, 0f),
//                        shape = createPolygonShape(5, 0f)
//                    ),
//                    PolygonCropShape(
//                        id = 2,
//                        title = "Heptagon",
//                        polygonProperties = PolygonProperties(sides = 7, 0f),
//                        shape = createPolygonShape(7, 0f)
//                    ),
//                    PolygonCropShape(
//                        id = 3,
//                        title = "Octagon",
//                        polygonProperties = PolygonProperties(sides = 8, 0f),
//                        shape = createPolygonShape(8, 0f)
//                    )
//                )
//            )
//        }

        OutlineType.Custom -> CustomPathOutline(id = 0, title = "Custom", path = Paths.Favorite)


//            OutlineType.ImageMask -> {

//                val outlines = defaultImages.mapIndexed { index, image ->
//                    ImageMaskOutline(id = index, title = "ImageMask", image = image)
//                }
//                ImageMaskOutlineContainer(
//                    outlines = outlines
//                )
//            }
//        else -> RoundedCornerCropShape(id = 1, title = "Rounded")
        else -> (RectCropShape(id = 0, title = "Rect"))

    }
}

@Suppress("UNCHECKED_CAST")
fun getOutlineContainer(
    outlineType: OutlineType,
    index: Int,
    outlines: List<CropOutline>
): CropOutlineContainer<out CropOutline> {
    return when (outlineType) {
        OutlineType.RoundedRect -> {
            RoundedRectOutlineContainer(
                selectedIndex = index,
                outlines = outlines as List<RoundedCornerCropShape>
            )
        }
        OutlineType.CutCorner -> {
            CutCornerRectOutlineContainer(
                selectedIndex = index,
                outlines = outlines as List<CutCornerCropShape>
            )
        }

        OutlineType.Oval -> {
            OvalOutlineContainer(
                selectedIndex = index,
                outlines = outlines as List<OvalCropShape>
            )
        }

        OutlineType.Polygon -> {
            PolygonOutlineContainer(
                selectedIndex = index,
                outlines = outlines as List<PolygonCropShape>
            )
        }

        OutlineType.Custom -> {
            CustomOutlineContainer(
                selectedIndex = index,
                outlines = outlines as List<CustomPathOutline>
            )
        }

        OutlineType.ImageMask -> {
            ImageMaskOutlineContainer(
                selectedIndex = index,
                outlines = outlines as List<ImageMaskOutline>
            )
        }
        else -> {
//            RectOutlineContainer(
//                selectedIndex = index,
//                outlines = outlines as List<RectCropShape>
//            )
            OvalOutlineContainer(
                selectedIndex = index,
                outlines = outlines as List<OvalCropShape>
            )
        }
    }
}


enum class OutlineType {
    Rect, RoundedRect, CutCorner, Oval, Polygon, Custom, ImageMask
}
