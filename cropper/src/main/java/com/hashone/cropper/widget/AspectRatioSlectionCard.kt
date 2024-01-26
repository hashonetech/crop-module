package com.hashone.cropper.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.hashone.cropper.model.BaseAspectRatioData

@Composable
fun AspectRatioSelectionCard(
    modifier: Modifier = Modifier,
    bgColor: Color,
    itemColor: Color,
    cropAspectRatio: BaseAspectRatioData,
    font: FontFamily,
    titleSize: Float,
) {
    Box(
        modifier = modifier
            .background(bgColor)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = cropAspectRatio.img),
                    contentDescription = "Icon",
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(itemColor)
                )
            }
            if (cropAspectRatio.title.isNotEmpty()) {
                Text(
                    modifier = Modifier,
                    text = cropAspectRatio.title,
                    color = itemColor,
                    fontSize = titleSize.sp,
                    fontFamily = font,
                    maxLines = 1
                )
            }
        }
    }
}
