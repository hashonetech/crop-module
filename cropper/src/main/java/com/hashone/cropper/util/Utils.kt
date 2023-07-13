package com.hashone.cropper.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager


class Utils {
    companion object {
        var mLastClickTime = 0L

        fun isAppInstalled(context: Context, packageName: String): Boolean {
            try {
                context.packageManager.getPackageInfo(packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                return false
            }
            return true
        }

        fun pxToDp(px: Int): Int {
            return (px / Resources.getSystem().displayMetrics.density).toInt()
        }

        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun dpToPx(dp: Float): Float {
            return (dp * Resources.getSystem().displayMetrics.density)
        }

        fun openKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        }

        fun closeKeyboard(activity: Activity, view: View) {
            val inputManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }


        fun checkClickTime(): Boolean {
            return if (((SystemClock.elapsedRealtime() - mLastClickTime) >= 600)) {
                mLastClickTime = SystemClock.elapsedRealtime()
                true
            } else {
                false
            }
        }

        fun checkClickTime300(): Boolean {
            return if (((SystemClock.elapsedRealtime() - mLastClickTime) >= 300)) {
                mLastClickTime = SystemClock.elapsedRealtime()
                true
            } else {
                false
            }
        }

        fun checkClickTime1500(): Boolean {
            return if (((SystemClock.elapsedRealtime() - mLastClickTime) >= 1500)) {
                mLastClickTime = SystemClock.elapsedRealtime()
                true
            } else {
                false
            }
        }

        fun checkClickTime1(): Boolean {
            return if (((SystemClock.elapsedRealtime() - mLastClickTime) >= 350)) {
                mLastClickTime = SystemClock.elapsedRealtime()
                true
            } else {
                false
            }
        }

        fun getScreenWidth(context: Context): Int {
            return Resources.getSystem().displayMetrics.widthPixels
        }


    }

}