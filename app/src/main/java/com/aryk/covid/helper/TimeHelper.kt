package com.aryk.covid.helper

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import java.util.Calendar

/**
 * class which helps to convert api times to system times
 *
 * @author Abdul Khan, ar.yawarkhan@gmail.com
 * @since 03.04.2020
 */
class TimeHelper(
    private val systemTimeFormatter: DateFormat
) {
    /**
     * returns a string from the given timestamp
     *
     * @param timeStamp in seconds
     * @param locale of the user
     * @return formatted date time with the format MEDIUM, Long
     */
    fun unixTimeStampInSecondsToLongDateTime(timeStamp: Long, locale: Locale): String {
        val dateFormat = DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.SHORT,
            locale
        )

        return dateFormat.format(Date(timeStamp))
    }

    /**
     * @return current day of the device
     */
    fun getCurrentDay(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }

    /**
     * @return current time as date object
     */
    fun getCurrentTimeAsDate(): Date {
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date = format.format(Date())
        return format.parse(date) ?: Date()
    }

    /**
     * @return given time as date object
     */
    fun getTimeAsDate(time: String): Date {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.parse(time) ?: Date()
    }
}
