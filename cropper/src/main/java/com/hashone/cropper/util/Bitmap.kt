package com.hashone.cropper.util

import android.graphics.*

fun Bitmap.getBitmap(width: Int, height: Int, mColorCode: Int): Bitmap {
    val colorBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val surfaceCanvas = Canvas(colorBitmap)
    val surfacePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    surfacePaint.color = mColorCode
    surfaceCanvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), surfacePaint)
    return colorBitmap
}

fun Bitmap.rotate(degrees: Float): Bitmap =
    Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply { postRotate(degrees) }, true)

fun Bitmap.copy(): Bitmap? = copy(config, isMutable)

fun Bitmap.flip(xFlip: Boolean = false, yFlip: Boolean = false): Bitmap? {
    return let {
        Bitmap.createBitmap(
            it, 0, 0, width, height, Matrix().apply {
                postScale(
                    if (xFlip) -1F else 1F,
                    if (yFlip) -1F else 1F,
                    width / 2f,
                    height / 2f
                )
            }, true
        )
    }
}


fun drawDiagonalFromTopLeftRoundRect(
    canvas: Canvas, paint: Paint, right: Float,
    bottom: Float, mMargin: Float = 0f, mRadius: Float = 0f, mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(mMargin, mMargin, mMargin + mDiameter, mMargin + mDiameter),
        mRadius, mRadius, paint
    )
    canvas.drawRect(RectF(mMargin, mMargin + mRadius, mMargin + mRadius, bottom), paint)
    canvas.drawRect(RectF(mMargin + mRadius, mMargin, right, bottom), paint)
}


private fun drawTopLeftRoundRect(
    canvas: Canvas,
    paint: Paint,
    right: Float,
    bottom: Float,
    mMargin: Float = 0f,
    mRadius: Float = 0f,
    mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(mMargin, mMargin, mMargin + mDiameter, mMargin + mDiameter),
        mRadius.toFloat(), mRadius.toFloat(), paint
    )
    canvas.drawRect(RectF(mMargin, mMargin + mRadius, mMargin + mRadius, bottom), paint)
    canvas.drawRect(RectF(mMargin + mRadius, mMargin, right, bottom), paint)
}

private fun drawTopRightRoundRect(
    canvas: Canvas,
    paint: Paint,
    right: Float,
    bottom: Float,
    mMargin: Float = 0f,
    mRadius: Float = 0f,
    mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(right - mDiameter, mMargin, right, mMargin + mDiameter), mRadius.toFloat(),
        mRadius.toFloat(), paint
    )
    canvas.drawRect(RectF(mMargin, mMargin, right - mRadius, bottom), paint)
    canvas.drawRect(RectF(right - mRadius, mMargin + mRadius, right, bottom), paint)
}

private fun drawBottomLeftRoundRect(
    canvas: Canvas,
    paint: Paint,
    right: Float,
    bottom: Float,
    mMargin: Float = 0f,
    mRadius: Float = 0f,
    mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(mMargin, bottom - mDiameter, mMargin + mDiameter, bottom),
        mRadius.toFloat(), mRadius.toFloat(), paint
    )
    canvas.drawRect(RectF(mMargin, mMargin, mMargin + mDiameter, bottom - mRadius), paint)
    canvas.drawRect(RectF(mMargin + mRadius, mMargin, right, bottom), paint)
}

private fun drawBottomRightRoundRect(
    canvas: Canvas,
    paint: Paint,
    right: Float,
    bottom: Float,
    mMargin: Float = 0f,
    mRadius: Float = 0f,
    mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius.toFloat(),
        mRadius.toFloat(), paint
    )
    canvas.drawRect(RectF(mMargin, mMargin, right - mRadius, bottom), paint)
    canvas.drawRect(RectF(right - mRadius, mMargin, right, bottom - mRadius), paint)
}

private fun drawTopRoundRect(
    canvas: Canvas,
    paint: Paint,
    right: Float,
    bottom: Float,
    mMargin: Float = 0f,
    mRadius: Float = 0f,
    mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius.toFloat(), mRadius.toFloat(),
        paint
    )
    canvas.drawRect(RectF(mMargin, mMargin + mRadius, right, bottom), paint)
}

private fun drawBottomRoundRect(
    canvas: Canvas,
    paint: Paint,
    right: Float,
    bottom: Float,
    mMargin: Float = 0f,
    mRadius: Float = 0f,
    mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(mMargin, bottom - mDiameter, right, bottom), mRadius.toFloat(), mRadius.toFloat(),
        paint
    )
    canvas.drawRect(RectF(mMargin, mMargin, right, bottom - mRadius), paint)
}

private fun drawLeftRoundRect(
    canvas: Canvas,
    paint: Paint,
    right: Float,
    bottom: Float,
    mMargin: Float = 0f,
    mRadius: Float = 0f,
    mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius.toFloat(), mRadius.toFloat(),
        paint
    )
    canvas.drawRect(RectF(mMargin + mRadius, mMargin, right, bottom), paint)
}

private fun drawRightRoundRect(
    canvas: Canvas,
    paint: Paint,
    right: Float,
    bottom: Float,
    mMargin: Float = 0f,
    mRadius: Float = 0f,
    mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(right - mDiameter, mMargin, right, bottom), mRadius.toFloat(), mRadius.toFloat(),
        paint
    )
    canvas.drawRect(RectF(mMargin, mMargin, right - mRadius, bottom), paint)
}

private fun drawOtherTopLeftRoundRect(
    canvas: Canvas,
    paint: Paint,
    right: Float,
    bottom: Float,
    mMargin: Float = 0f,
    mRadius: Float = 0f,
    mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(mMargin, bottom - mDiameter, right, bottom), mRadius.toFloat(), mRadius.toFloat(),
        paint
    )
    canvas.drawRoundRect(
        RectF(right - mDiameter, mMargin, right, bottom), mRadius.toFloat(), mRadius.toFloat(),
        paint
    )
    canvas.drawRect(RectF(mMargin, mMargin, right - mRadius, bottom - mRadius), paint)
}

private fun drawOtherTopRightRoundRect(
    canvas: Canvas,
    paint: Paint,
    right: Float,
    bottom: Float,
    mMargin: Float = 0f,
    mRadius: Float = 0f,
    mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius.toFloat(), mRadius.toFloat(),
        paint
    )
    canvas.drawRoundRect(
        RectF(mMargin, bottom - mDiameter, right, bottom), mRadius.toFloat(), mRadius.toFloat(),
        paint
    )
    canvas.drawRect(RectF(mMargin + mRadius, mMargin, right, bottom - mRadius), paint)
}

private fun drawOtherBottomLeftRoundRect(
    canvas: Canvas,
    paint: Paint,
    right: Float,
    bottom: Float, mMargin: Float = 0f, mRadius: Float = 0f, mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius.toFloat(), mRadius.toFloat(),
        paint
    )
    canvas.drawRoundRect(
        RectF(right - mDiameter, mMargin, right, bottom), mRadius.toFloat(), mRadius.toFloat(),
        paint
    )
    canvas.drawRect(RectF(mMargin, mMargin + mRadius, right - mRadius, bottom), paint)
}

private fun drawOtherBottomRightRoundRect(
    canvas: Canvas, paint: Paint, right: Float,
    bottom: Float, mMargin: Float = 0f, mRadius: Float = 0f, mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius.toFloat(), mRadius.toFloat(),
        paint
    )
    canvas.drawRoundRect(
        RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius.toFloat(), mRadius.toFloat(),
        paint
    )
    canvas.drawRect(RectF(mMargin + mRadius, mMargin + mRadius, right, bottom), paint)
}

private fun drawDiagonalFromTopRightRoundRect(
    canvas: Canvas, paint: Paint, right: Float,
    bottom: Float, mMargin: Float = 0f, mRadius: Float = 0f, mDiameter: Float = 0f
) {
    canvas.drawRoundRect(
        RectF(right - mDiameter, mMargin, right, mMargin + mDiameter), mRadius.toFloat(),
        mRadius.toFloat(), paint
    )
    canvas.drawRoundRect(
        RectF(mMargin, bottom - mDiameter, mMargin + mDiameter, bottom),
        mRadius.toFloat(), mRadius.toFloat(), paint
    )
    canvas.drawRect(RectF(mMargin, mMargin, right - mRadius, bottom - mRadius), paint)
    canvas.drawRect(RectF(mMargin + mRadius, mMargin + mRadius, right, bottom), paint)
}