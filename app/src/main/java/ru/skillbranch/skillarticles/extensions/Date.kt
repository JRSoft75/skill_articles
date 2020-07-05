package ru.skillbranch.skillarticles.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR



fun Date.format(pattern: String="HH:mm:ss dd.MM.yy"):String{
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun  Date.add(value:Int, units: TimeUnits = TimeUnits.SECOND):Date{
    var time = this.time

    time += when(units){
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.shortFormat(): String {
    val pattern = if(this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.isSameDay(date: Date):Boolean{
    val day1 = this.time / DAY
    val day2 = date.time / DAY
    return day1 == day2
}

fun Date.humanizeDiff(date:Date = Date()): String {
    var milliseconds: Long = date.getTime() - this.getTime()
    val isFuture = milliseconds > 0
    milliseconds = abs(milliseconds)
    val seconds = (milliseconds / 1000).toInt()
    val minutes = (milliseconds / (60 * 1000)).toInt()
    val hours = (milliseconds / (60 * 60 * 1000)).toInt()
    val days = (milliseconds / (24 * 60 * 60 * 1000)).toInt()
    var result: String
    if(isFuture) {
        result = when (seconds) {
            in 0..1 -> "только что"
            in 2..45 -> "несколько секунд назад"
            in 46..75 -> "минуту назад"
            else -> {
                if (seconds > 75 && minutes <= 45) {
                    TimeUnits.MINUTE.plural(minutes) + " назад"
                } else if (minutes > 46 && minutes <= 75) {
                    "час назад"
                } else if (minutes > 75 && hours <= 22) {
                    TimeUnits.HOUR.plural(hours) + " назад"
                } else if (hours > 22 && hours <= 26) {
                    "день назад"
                } else if (hours > 26 && days <= 360) {
                    TimeUnits.DAY.plural(days) + " назад"
                } else if (days > 360) {
                    "более года назад"
                } else {
                    ""
                }
            }
        }
    }else {
        result = when (seconds) {
            in 0..1 -> "только что"
            in 2..45 -> "через несколько секунд"
            in 46..75 -> "через минуту"
            else -> {
                if (seconds > 75 && minutes <= 45) {
                    "через " + TimeUnits.MINUTE.plural(minutes)
                } else if (minutes > 46 && minutes <= 75) {
                    "через час"
                } else if (minutes > 75 && hours <= 22) {
                    "через " + TimeUnits.HOUR.plural(hours)
                } else if (hours > 22 && hours <= 26) {
                    "через день"
                } else if (hours > 26 && days <= 360) {
                    "через " + TimeUnits.DAY.plural(days)
                } else if (days > 360 && days <= 370) {
                    "через год"
                } else if (days > 370) {
                    "более чем через год"
                } else {
                    ""
                }
            }
        }
    }

    return result
    /*
0с - 1с "только что"
1с - 45с "несколько секунд назад"
45с - 75с "минуту назад"
75с - 45мин "N минут назад"
45мин - 75мин "час назад"
75мин 22ч "N часов назад"
22ч - 26ч "день назад"
26ч - 360д "N дней назад"
>360д "более года назад"
     */
}

enum class TimeUnits{
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(num: Int): String? {
        val preLastDigit = num % 100 / 10
        val second:Array<String> = arrayOf("секунд","секунду","секунды")
        val minute:Array<String> = arrayOf("минут","минуту","минуты")
        val hour:Array<String> = arrayOf("часов","час","часа")
        val day:Array<String> = arrayOf("дней","день","дня")
        return "$num " + if (preLastDigit == 1) {
            when(this){
                SECOND -> second[0]
                MINUTE -> minute[0]
                HOUR -> hour[0]
                DAY -> day[0]
            }
        } else when (num % 10) {
            1 -> {
                when(this){
                    SECOND -> second[1]
                    MINUTE -> minute[1]
                    HOUR -> hour[1]
                    DAY -> day[1]
                }
            }
            2, 3, 4 -> {
                when(this){
                    SECOND -> second[2]
                    MINUTE -> minute[2]
                    HOUR -> hour[2]
                    DAY -> day[2]
                }
            }
            else -> when(this){
                SECOND -> second[0]
                MINUTE -> minute[0]
                HOUR -> hour[0]
                DAY -> day[0]
            }
        }
    }


}