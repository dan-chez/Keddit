@file: JvmName("TimeUtils")
package com.danchez.keddit.commons.extensions

import java.util.*
import kotlin.time.hours

fun Long.getFriendlyTime(): String {
    val dateTime = Date(this * 1000)
    val sb = StringBuffer()
    val current = Calendar.getInstance().time
    var diffInSeconds = ((current.time - dateTime.time) / 1000).toInt()

    val sec = if (diffInSeconds >= 60) (diffInSeconds % 60) else diffInSeconds
    diffInSeconds /= 60
    val min = if (diffInSeconds >= 60) (diffInSeconds % 60) else diffInSeconds
    diffInSeconds /= 60
    val hrs = if (diffInSeconds >= 24) (diffInSeconds % 24) else diffInSeconds
    diffInSeconds /= 24
    val days = if (diffInSeconds >= 30) (diffInSeconds % 30) else diffInSeconds
    diffInSeconds /= 30
    val months = if (diffInSeconds >= 12) (diffInSeconds % 12) else diffInSeconds
    diffInSeconds /= 12
    val years = diffInSeconds

    if (years > 0) {
        if (years == 1) {
            sb.append("hace un año")
        } else {
            sb.append("hace $years años")
        }
        if (years <= 6 && months > 0) {
            if (months == 1) {
                sb.append(" y un mes")
            } else {
                sb.append(" y $months meses")
            }
        }
    } else if (months > 0) {
        if (months == 1) {
            sb.append("hace un mes")
        } else {
            sb.append("hace $months meses")
        }
        if (months <= 6 && days > 0) {
            if (days == 1) {
                sb.append(" y un día")
            } else {
                sb.append(" y $days días")
            }
        }
    } else if (days > 0) {
        if (days == 1) {
            sb.append("hace un día")
        } else {
            sb.append("hace $days días")
        }
        if (days <= 3 && hrs > 0) {
            if (hrs == 1) {
                sb.append(" y una hora")
            } else {
                sb.append(" y $hrs horas")
            }
        }
    } else if (hrs > 0) {
        if (hrs == 1) {
            sb.append("hace una hora")
        } else {
            sb.append("hace $hrs horas")
        }
        if (min > 1) {
            sb.append(" y $min minutos")
        }
    } else if (min > 0) {
        if (min == 1) {
            sb.append("hace un minuto")
        } else {
            sb.append("hace $min minutos")
        }
        if (sec > 1) {
            sb.append(" y $sec segundos")
        }
    } else {
        if (sec <= 1) {
            sb.append("alrededor de un segundo")
        } else {
            sb.append("alrededor de $sec segundos")
        }
    }
    return sb.toString()
}