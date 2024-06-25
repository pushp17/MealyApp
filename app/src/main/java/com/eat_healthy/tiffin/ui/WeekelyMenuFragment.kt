package com.eat_healthy.tiffin.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.Operation
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.WeekelyMenuAdapter
import com.eat_healthy.tiffin.databinding.WeekelyMenuFragmentLayoutBinding
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.UsersFoodReminder
import com.eat_healthy.tiffin.models.WeekelyMenu
import com.eat_healthy.tiffin.models.WeekelyMenuResponse
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.LunchDinnerReminderWorkManager
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class WeekelyMenuFragment : ListViewFragment<WeekelyMenuAdapter, WeekelyMenuFragmentLayoutBinding>() {
    val sharedViewModel: SharedViewModel by activityViewModels()
    private val weekelyMenuMutableList = mutableListOf<WeekelyMenu>()
    override val fragmentLayoutResId: Int
        get() = R.layout.weekely_menu_fragment_layout
    private var dayOfTheWeek = "mon"
    var weekelyMenuRes: WeekelyMenuResponse? = null
    var type : String =""
    var weekelyMenu: WeekelyMenu? = null
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            initializeWorker(type)
        }
        else {
            Toast.makeText(requireActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show()

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setOnClickListener(this)
        sharedViewModel.weekelyMenuLiveData.observe(viewLifecycleOwner, observer)
        sharedViewModel.getWeekelyMenu()

        showCancelAllNotificationButton()

        binding.tvMonday.setOnClickListener {
            dayOfTheWeek = "mon"
            setMenuItemsBasedOnDay("mon")
        }

        binding.tvTuesday.setOnClickListener {
            dayOfTheWeek = "tue"
            setMenuItemsBasedOnDay("tue")
        }

        binding.tvWednesday.setOnClickListener {
            dayOfTheWeek = "wed"
            setMenuItemsBasedOnDay("wed")
        }

        binding.tvThursday.setOnClickListener {
            dayOfTheWeek = "thurs"
            setMenuItemsBasedOnDay("thurs")
        }

        binding.tvFriday.setOnClickListener {
            dayOfTheWeek = "fri"
            setMenuItemsBasedOnDay("fri")
        }

        binding.tvSaturday.setOnClickListener {
            dayOfTheWeek = "sat"
            setMenuItemsBasedOnDay("sat")
        }

        binding.tvCancelAllNotification.setOnClickListener {
            WorkManager.getInstance(requireContext()).cancelAllWorkByTag("tagLunchWorkManager")
            WorkManager.getInstance(requireContext()).cancelAllWorkByTag("tagDinnerWorkManager")
            sharedPrefManager.clearASpecificPreference(Constants.LUNCH_WORKER_ENQUED)
            sharedPrefManager.clearASpecificPreference(Constants.DINNER_WORKER_ENQUED)
            sharedPrefManager.clearASpecificPreference(Constants.USERS_FOOD_REMINDER)
            showToast("All Notification Is Canceled")
            binding.tvCancelAllNotification.visibility = View.GONE
        }

        firebaseAnalytics.logEvent(Constants.MENU_PAGE_OPENED,null)
    }

    private fun checkPermission(_type: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            initializeWorker(_type)
            return
        }
        type = _type
        when {
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                initializeWorker(_type)
            }
            else -> {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun initializeWorker(type : String) {
        if(type.equals("notifyLunch") && !sharedPrefManager.getBooleanFromPreference(Constants.LUNCH_WORKER_ENQUED)){
            saveMealReminder(weekelyMenu!!, dayOfTheWeek.plus("_").plus("lunch"))
            initializeWorkerForLunchTime()
            showToast("Notification set for lunch")
        } else if(type.equals("notifyDinner") && !sharedPrefManager.getBooleanFromPreference(Constants.DINNER_WORKER_ENQUED)) {
            saveMealReminder(weekelyMenu!!, dayOfTheWeek.plus("_").plus("dinner"))
            initializeWorkerForDinnerTime()
            showToast("Notification set for Dinner")
        }
    }


    override fun receivedResponse(item: Any?) {
        item?.let {
            if(it is WeekelyMenuResponse) {
                weekelyMenuRes = it
                binding.llWeekdays.visibility = View.VISIBLE
                it.weekelyMenuList?.let { it1 -> weekelyMenuMutableList.addAll(it1) }
                weekelyMenuMutableList.let { it1 -> adapter.setItems(it1.filter { it.day?.contains("mon") == true }) }
                firebaseAnalytics.logEvent(Constants.MENU_PAGE_SUCCESS,null)
            }
        }
    }

    override fun onClickItem(position: Int, item: ListItem?, id: String?) {
        when (item) {
            is WeekelyMenu -> {
                weekelyMenu = item
                when (id) {
                    "notifyLunch" -> {
                          if(sharedPrefManager.getBooleanFromPreference(Constants.LUNCH_WORKER_ENQUED)) {
                              saveMealReminder(item, dayOfTheWeek.plus("_").plus("lunch"))
                              showToast("Notification set for lunch")
                              firebaseAnalytics.logEvent(Constants.LUNCH_REMINDER_CLICKED,null)
                          }else {
                              checkPermission("notifyLunch")
                          }

                    }
                    "notifyDinner" -> {
                        if(sharedPrefManager.getBooleanFromPreference(Constants.DINNER_WORKER_ENQUED)) {
                            saveMealReminder(item, dayOfTheWeek.plus("_").plus("dinner"))
                            showToast("Notification set for Dinner")
                            firebaseAnalytics.logEvent(Constants.DINNER_REMINDER_CLICKED,null)
                        } else {
                            checkPermission("notifyDinner")
                        }
                    }
                }
            }
        }
    }


    private fun saveMealReminder(weekelyMenu: WeekelyMenu, day: String) {
        val savedFoodReminderMenu =
            sharedPrefManager.getModelClass<UsersFoodReminder>(Constants.USERS_FOOD_REMINDER)
        if (savedFoodReminderMenu == null) {
            val savedMenuList = mutableListOf<WeekelyMenu>()
            savedMenuList.add(WeekelyMenu(weekelyMenu.item, weekelyMenu.desc, weekelyMenu.image, day))
            sharedPrefManager.addModelClass(
                Constants.USERS_FOOD_REMINDER,
                UsersFoodReminder(savedMenuList)
            )
        } else {
            savedFoodReminderMenu.savedMenuList.add(WeekelyMenu(weekelyMenu.item, weekelyMenu.desc, weekelyMenu.image, day))
            sharedPrefManager.addModelClass(
                Constants.USERS_FOOD_REMINDER,
                savedFoodReminderMenu
            )
        }
    }

    private fun initializeWorkerForLunchTime() {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, weekelyMenuRes?.lunch_alarm_hour ?: 11)
        dueDate.set(Calendar.MINUTE, weekelyMenuRes?.lunch_alarm_min ?: 1)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff)

        val refreshCpnWork = PeriodicWorkRequest.Builder(LunchDinnerReminderWorkManager::class.java, 24, TimeUnit.HOURS)
            .setInitialDelay(minutes, TimeUnit.MINUTES)
            .setInputData(workDataOf("my_param" to "lunch"))
            .addTag("tagLunchWorkManager")
            .build()


        val lunchWorker = WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork("lunchWorkManager",
            ExistingPeriodicWorkPolicy.REPLACE, refreshCpnWork)

        lunchWorker.state.observe(
                viewLifecycleOwner,
                object : Observer<Operation.State> {
                    override fun onChanged(state: Operation.State) {
                        when (state) {
                            is Operation.State.SUCCESS -> {
                                showCancelAllNotificationButton()
                                sharedPrefManager.addBooleanToPreference(Constants.LUNCH_WORKER_ENQUED, true)
                                lunchWorker.state.removeObserver(this)
                            }
                            is Operation.State.FAILURE -> {
                                lunchWorker.state.removeObserver(this)
                            }
                        }
                    }
                }
            )
    }

    private fun initializeWorkerForDinnerTime() {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, weekelyMenuRes?.dinner_alarm_hour ?: 19)
        dueDate.set(Calendar.MINUTE, weekelyMenuRes?.dinner_alarm_min ?: 1)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff)


        val refreshLunchNotifyWork = PeriodicWorkRequest.Builder(LunchDinnerReminderWorkManager::class.java, 24, TimeUnit.HOURS)
            .setInputData(workDataOf("my_param" to "dinner"))
            .setInitialDelay(minutes, TimeUnit.MINUTES)
            .addTag("tagDinnerWorkManager")
            .build()


        val dinnerWorker = WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "dinnerWorkManager",
            ExistingPeriodicWorkPolicy.REPLACE, refreshLunchNotifyWork
        )

        dinnerWorker.state.observe(
            viewLifecycleOwner,
            object : Observer<Operation.State> {
                override fun onChanged(state: Operation.State) {
                    when (state) {
                        is Operation.State.SUCCESS -> {
                            showCancelAllNotificationButton()
                            sharedPrefManager.addBooleanToPreference(Constants.DINNER_WORKER_ENQUED, true)
                            dinnerWorker.state.removeObserver(this)
                        }
                        is Operation.State.FAILURE -> {
                            dinnerWorker.state.removeObserver(this)
                        }
                    }
                }
            }
        )
    }

    private fun showCancelAllNotificationButton() {
        if (!sharedPrefManager.getModelClass<UsersFoodReminder>(Constants.USERS_FOOD_REMINDER)?.savedMenuList.isNullOrEmpty()) {
            binding.tvCancelAllNotification.visibility = View.VISIBLE
        }
    }


    private fun setMenuItemsBasedOnDay(day:String) {
        binding.tvMonday.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.oval_gray_border)
        binding.tvTuesday.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.oval_gray_border)
        binding.tvWednesday.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.oval_gray_border)
        binding.tvThursday.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.oval_gray_border)
        binding.tvFriday.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.oval_gray_border)
        binding.tvSaturday.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.oval_gray_border)


        binding.tvMonday.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_text))
        binding.tvTuesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_text))
        binding.tvWednesday.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black_text
            )
        )
        binding.tvThursday.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black_text
            )
        )
        binding.tvFriday.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_text))
        binding.tvSaturday.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black_text
            )
        )
        when (day) {
            "mon" -> {
                binding.tvMonday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.tvMonday.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.green_color_fillled_rounded_corner
                )
                adapter.setItems(
                    weekelyMenuMutableList.filter {it.day?.contains("mon") == true })
            }

            "tue" -> {
                binding.tvTuesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.tvTuesday.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.green_color_fillled_rounded_corner
                )
                adapter.setItems(
                    weekelyMenuMutableList.filter {it.day?.contains("tue") == true })
            }

            "wed" -> {
                binding.tvWednesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.tvWednesday.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.green_color_fillled_rounded_corner
                )
                adapter.setItems(
                    weekelyMenuMutableList.filter {it.day?.contains("wed") == true })
            }

            "thurs" -> {
                binding.tvThursday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.tvThursday.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.green_color_fillled_rounded_corner
                )
                adapter.setItems(
                    weekelyMenuMutableList.filter {it.day?.contains("thurs") == true })
            }

            "fri" -> {
                binding.tvFriday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.tvFriday.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.green_color_fillled_rounded_corner
                )
                adapter.setItems(
                    weekelyMenuMutableList.filter {it.day?.contains("fri") == true })
            }

            "sat" -> {
                binding.tvSaturday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.tvSaturday.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.green_color_fillled_rounded_corner
                )
                adapter.setItems(
                    weekelyMenuMutableList.filter {it.day?.contains("sat") == true })
            }
        }
    }
}
