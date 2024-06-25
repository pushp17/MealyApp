package com.eat_healthy.tiffin.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateAndTimeUtils {
    private fun getTimeInMillisFromTimeString(timeString: String): Long {
        try {
            val sdf_1 = SimpleDateFormat("dd/MM/yyyy",Locale.US)
            val currentDate=sdf_1.format(Calendar.getInstance().time)
            val sdf2 = SimpleDateFormat("dd/MM/yyyy h:mm a",Locale.US)
            val date: Date = sdf2.parse("$currentDate $timeString")
            return date.time
        } catch (e: Exception) {
        }
        return 0
    }

    fun checkLunchOrDinnerTime(
        lunchStartTime: String,
        lunchEndTime: String,
        dinnerStartTime: String,
        dinnerEndTime: String
    ): String {
        val currentTime = System.currentTimeMillis()
        if (currentTime > getTimeInMillisFromTimeString(lunchStartTime) &&
            currentTime <= getTimeInMillisFromTimeString(lunchEndTime!!)
        ) return "Lunch"
        else if (currentTime > getTimeInMillisFromTimeString(dinnerStartTime!!)
            && currentTime <= getTimeInMillisFromTimeString(dinnerEndTime!!)
        ) return "Dinner"
        else if (currentTime > getTimeInMillisFromTimeString(lunchEndTime!!)
            && currentTime <= getTimeInMillisFromTimeString(dinnerEndTime!!)
        ) return "lunch_timeout"
        else return "time_out"
    }

    fun getDayOfTheWeek(strDate:String):String{
        val inFormat = SimpleDateFormat("dd/MM/yyyy",Locale.US)
        val date = inFormat.parse(strDate)
        val outFormat = SimpleDateFormat("EEEE",Locale.US)
        return outFormat.format(date)
    }

    fun getDayOfTheWeek(date:String,noOfDays:Int):String{
        val inFormat = SimpleDateFormat("dd/MM/yyyy",Locale.US)
      //  val date = inFormat.parse(strDate)

        val calender = Calendar.getInstance()
        calender.time=inFormat.parse(date)
        calender.add(Calendar.DATE, noOfDays)

        val outFormat = SimpleDateFormat("EEEE",Locale.US)
        return outFormat.format(calender.time)
    }

    fun getNoOfDaysBetweenTwoDates(startDate:String ?, endDate:String?,incrementNoOfDays:Int):Int {
        val inFormat = SimpleDateFormat("dd/MM/yyyy",Locale.US)
        val diff = if(startDate !=null && endDate !=null) {
            val startDateValue = inFormat.parse(startDate)
            val endDateValue = inFormat.parse(endDate)
            endDateValue.time - (startDateValue?.time ?: 0)
        }else if(startDate != null){
            val startDateValue = inFormat.parse(startDate)
            val calendar = Calendar.getInstance()

            // Below  line of code is just for testing End date, will remove after testing
//            calendar.add(Calendar.DAY_OF_MONTH, 30)

            val endDateValue= inFormat.parse(inFormat.format(calendar.time))
            endDateValue.time - (startDateValue?.time ?: 0)
        }else {
            val calendar = Calendar.getInstance()

            //Below line is for testing , just uncomment the above line after testing
//            calendar.add(Calendar.DAY_OF_MONTH, 30)

            val startDate= inFormat.parse(inFormat.format(calendar.time))

            val endDateValue = inFormat.parse(endDate)
            endDateValue.time - (startDate?.time ?: 0)
        }
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
    }


    // Here current date means we will start taking the order after one day because if we subtract
    // two dates 1/03/2021 to 30/03/2021 , it will
    // give 29 rather than 30 , so take the subscription start date as Calender.getInstance()
    fun getCurrentDate():String {
        val inFormat = SimpleDateFormat("dd/MM/yyyy",Locale.US)
        return inFormat.format(Calendar.getInstance().time)
    }

    fun getSubscriptionEndDate(strDate:String?):String {
        if(strDate==null) return ""
        val inFormat = SimpleDateFormat("dd/MM/yyyy",Locale.US)
        val calender = Calendar.getInstance()
        calender.time=inFormat.parse(strDate)
        calender.add(Calendar.DATE, 28)
        return inFormat.format(calender.time)
    }

    fun getTodaysDayOfTheWeek(): String {
        val calender = Calendar.getInstance()
        val outFormat = SimpleDateFormat("EEEE", Locale.US)
        return outFormat.format(calender.time)
    }

    fun getCalander(strDate:String,numberOfDays:Int):Calendar?{
        val inFormat = SimpleDateFormat("dd/MM/yyyy",Locale.US)
        val calender = Calendar.getInstance()
        calender.time=inFormat.parse(strDate)
        calender.add(Calendar.DATE, numberOfDays)
        return calender
    }

    fun returnTheLatestPassedTime(_lastTimeInMillis: Long? = null): Long {
        var lastTimeInMillis = _lastTimeInMillis
        if (lastTimeInMillis == null) {
            val comingTime = Calendar.getInstance()
            comingTime.add(Calendar.MINUTE, 30)
            lastTimeInMillis = comingTime.timeInMillis
        }
        if (Calendar.getInstance().timeInMillis > lastTimeInMillis) {
            val newTime = Calendar.getInstance()
            newTime.add(Calendar.MINUTE, 30)
            return newTime.timeInMillis
        }
        return lastTimeInMillis
    }
}