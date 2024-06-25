package com.eat_healthy.tiffin.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.models.UsersFoodReminder
import com.eat_healthy.tiffin.models.WeekelyMenuResponse
import com.eat_healthy.tiffin.repository.LoginRepository
import com.eat_healthy.tiffin.ui.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@HiltWorker
class LunchDinnerReminderWorkManager
@AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted  workerParams: WorkerParameters,
    val sharedPrefManager: SharedPrefManager,
    val loginRepository: LoginRepository
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val notificationId = 1
    }
    override suspend fun doWork(): Result {
        loginRepository.updateWeekelyMenuFromWorker()
        checkDayAndBasedOnThatFilterCorrectReminderFood()
        return Result.success()
    }

    private  fun  createNotification(title:String?, body:String?, image: String? = null) {
        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(appContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        var largeBitmap: Bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.mealy_icon)
        CoroutineScope(Dispatchers.Main).launch {
            val builder : NotificationCompat.Builder
         //   val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
          //  val defaultSoundUri = Uri.parse("android.resource://" + appContext.getPackageName() + "/" + R.raw.custom);
          //  val customSoundUri = Uri.parse("android.resource://" + appContext.getPackageName() + "/raw/custom");
            if (image != null) {
                largeBitmap = withContext(Dispatchers.IO) {
                    Glide.with(appContext).asBitmap().load(image).submit()
                        .get()
                }

                val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.mealy_icon)
                builder = NotificationCompat.Builder(appContext, "1")
                    .setSmallIcon(R.drawable.ic_m)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(ContextCompat.getColor(appContext, R.color.colorPrimary))
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
                    .setLargeIcon(bitmap)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
//                    .setSound(customSoundUri)
            } else {
                builder = NotificationCompat.Builder(appContext, "1")
                    .setSmallIcon(R.drawable.ic_m)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(ContextCompat.getColor(appContext, R.color.colorPrimary))
                    .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
//                    .setSound(customSoundUri)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "channelName"
                val descriptionText = "Channel Description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("1", name, importance).apply {
                    description = descriptionText
//                    setSound(customSoundUri,null)
                }
                val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
            with(NotificationManagerCompat.from(appContext)) {
                if (ActivityCompat.checkSelfPermission(
                        appContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(notificationId,builder.build())
                }
            }
        }
    }

    private fun checkDayAndBasedOnThatFilterCorrectReminderFood() {
        inputData.getString("my_param")?.let {
            if (it.equals("lunch")) {
                val dayOfToday = DateAndTimeUtils.getTodaysDayOfTheWeek()
                when(dayOfToday){
                    "Monday" -> {
                        filterFoodDataForShowingInNotification("mon_lunch")
                    }
                    "Tuesday" ->{
                        filterFoodDataForShowingInNotification("tue_lunch")
                    }
                    "Wednesday" ->{
                        filterFoodDataForShowingInNotification("wed_lunch")
                    }
                    "Thursday" ->{
                        filterFoodDataForShowingInNotification("thurs_lunch")
                    }
                    "Friday" ->{
                        filterFoodDataForShowingInNotification("fri_lunch")
                    }
                    "Saturday" ->{
                        filterFoodDataForShowingInNotification("sat_lunch")
                    }
                }
            } else if(it.equals("dinner")) {
                val dayOfToday = DateAndTimeUtils.getTodaysDayOfTheWeek()
                when(dayOfToday){
                    "Monday" -> {
                        filterFoodDataForShowingInNotification("mon_dinner")
                    }
                    "Tuesday" ->{
                        filterFoodDataForShowingInNotification("tue_dinner")
                    }
                    "Wednesday" ->{
                        filterFoodDataForShowingInNotification("wed_dinner")
                    }
                    "Thursday" ->{
                        filterFoodDataForShowingInNotification("thurs_dinner")
                    }
                    "Friday" ->{
                        filterFoodDataForShowingInNotification("fri_dinner")
                    }
                    "Saturday" ->{
                        filterFoodDataForShowingInNotification("sat_dinner")
                    }
                }
            }
        }
    }

    private fun filterFoodDataForShowingInNotification(day: String) {
        val savedMenuListOnParticularDay =
            sharedPrefManager.getModelClass<UsersFoodReminder>(Constants.USERS_FOOD_REMINDER)?.savedMenuList?.filter {
                it.day.equals(day,true)
            }
        val latestMenuOnRespectiveDay =  sharedPrefManager.getModelClass<WeekelyMenuResponse>(Constants.MENU)?.weekelyMenuList?.filter { latestMenu -> (latestMenu.day?.contains(day,true) ?: false)}
        val finalMatchedMenu = latestMenuOnRespectiveDay?.filter { latestMenu ->
            (savedMenuListOnParticularDay?.map { it.item }?.contains(latestMenu.item) ?: false)
        }
        val reminderText = if(day.contains("lunch",true)) "Lunch Order Reminder : " else "Dinner Order Reminder : "
        if (!finalMatchedMenu.isNullOrEmpty()) {
            val food = finalMatchedMenu.getOrNull(0)
            createNotification(reminderText.plus(food?.item),food?.desc,food?.image)
        }else if(finalMatchedMenu.isNullOrEmpty() && !savedMenuListOnParticularDay.isNullOrEmpty() && !latestMenuOnRespectiveDay.isNullOrEmpty()) {
            createNotification(reminderText,latestMenuOnRespectiveDay.map { it.item }.joinToString(", "), "https://res.cloudinary.com/drbeg6afu/image/upload/v1709762642/special_thali_2_xi4fjh.jpg")
        }
        else {
        }
    }

}
