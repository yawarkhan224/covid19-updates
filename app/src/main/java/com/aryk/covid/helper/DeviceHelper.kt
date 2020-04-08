package com.aryk.covid.helper

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.os.IBinder
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.aryk.covid.BuildConfig
import kotlin.math.roundToInt

/**
 * Class which contains functionality to identify the device and get the status of it
 *
 * @author Abdul Khan, ar.yawarkhan@gmail.com
 * @since 08.04.2020
 */
@SuppressWarnings("ForbiddenComment", "MagicNumber")
class DeviceHelper {
    /**
     * check if we are on the phone version
     * @param context
     * @return true if we have displays smaller than large
     */
    fun isPhone(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration
            .SCREENLAYOUT_SIZE_MASK != Configuration.SCREENLAYOUT_SIZE_XLARGE && context
            .resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK !=
                Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /**
     * check if the device is in landscape
     *
     * @param context
     * @return true if the device is in landscape mode
     */
    fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * check if the device is in portrait mode
     *
     * @param context
     * @return true if the device is in portrait mode
     */
    fun isPortrait(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * Converts Dp in px
     *
     * @param dp
     * @return pixel value of the dp
     */
    fun convertDpToPixel(dp: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.roundToInt()
    }

    fun convertSpToPixel(sp: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = sp * (metrics.scaledDensity / 160f)
        return px.roundToInt()
    }

    /**
     * hides the keyboard
     * @param context
     */
    fun hideKeyBoard(context: Context?, windowToken: IBinder?) {
        context?.let {
            val inputManager = it.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager

            inputManager.hideSoftInputFromWindow(windowToken, 0)
        }
    }

    /**
     * get the size of the default display
     *
     * @param windowManager
     */
    fun getScreenSize(windowManager: WindowManager): Point {
        val point = Point()

        windowManager.defaultDisplay.getSize(point)

        return point
    }

    /**
     * set the screen orientation for the specific device
     *
     * @param activity to set the orientation
     */
    fun setScreenOrientationRule(activity: Activity) {
        if (!BuildConfig.DEBUG) {
            if (isPhone(activity)) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
        }
    }
}
