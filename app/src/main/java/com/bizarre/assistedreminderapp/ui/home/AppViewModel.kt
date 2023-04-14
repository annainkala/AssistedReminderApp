package com.bizarre.assistedreminderapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.*
import com.bizarre.core_domain.entity.Reminder
import androidx.work.*
import com.bizarre.assistedreminderapp.Graph
import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.location.LocationRepository
import com.bizarre.assistedreminderapp.ui.reminder.ReminderState

import com.bizarre.assistedreminderapp.ui.user.UserState

import com.bizarre.assistedreminderapp.ui.utils.NotificationWorker2
import com.bizarre.core_domain.entity.User

import com.bizarre.core_domain.repository.ReminderRepository

import com.bizarre.core_domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val userRepository: UserRepository,

) : ViewModel() {



    private val __reminderState = MutableStateFlow<ReminderState>(ReminderState.Loading)
    val reminderState: StateFlow<ReminderState> = __reminderState

    private val _userList: MutableStateFlow<List<User>> = MutableStateFlow(mutableListOf())
    val users: StateFlow<List<User>> = _userList


    private val _remindeList: MutableStateFlow<List<Reminder>> = MutableStateFlow(mutableListOf())
    val reminders: StateFlow<List<Reminder>> = _remindeList
    private val _userViewState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userViewState

    private val _selectedUser = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _selectedUser
    private val _selectedReminder = MutableStateFlow<Reminder?>(null)
    val reminder: StateFlow<Reminder?> = _selectedReminder


    fun saveReminder(reminder: Reminder) {
        viewModelScope.launch {
            Log.d("SAVE::::::: ", reminder.toString())
            reminderRepository.addReminder(reminder)
            if (reminder.reminder_date.isAfter(LocalDateTime.now())) {
                //setLocationBackgroundWorker(reminder)
            }
            if (reminder.location_x != 0.0 && reminder.location_y != 0.0) {

            }


        }
    }


    fun updateReminder(reminder: Reminder) {
        viewModelScope.launch {
            Log.d("SAVE::::::: ", reminder.toString())
            reminderRepository.updateReminder(reminder)
            loadReminders(_selectedUser.value!!)
            //setLocationBackgroundWorker(reminder)


        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            val result = userRepository.addUser(user)
            Log.d("HHHHHHHHHHH", "SAVE USER:::::: " + user.toString() + result.toString())
            loadUsers()
            // notifyUserOfReminder(reminder)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
            Log.d("HHHHHHHHHHH", "SAVE USER:::::: " + user.toString())
            loadUsers()
            // notifyUserOfReminder(reminder)
        }
    }


    fun onUserSelected(user: User) {
        _selectedUser.value = user
    }
/*
  fun getReminderById(id:Long){


      viewModelScope.launch {
          val reminder = reminderRepository.findReminderById(id)
          _selectedReminder = MutableStateFlow(ReminderState.Success(
              reminder = reminder,
              data = emptyList()
          ).reminder)

      }


    }*/

    fun deleteReminder(reminder: Reminder) {


        viewModelScope.launch {

            reminderRepository.deleteReminder(reminder)
            loadReminders(_selectedUser.value!!)


            //Log.d("DDDDDDDD","DELETE:::::: ")

            // notifyUserOfReminder(reminder)
        }
    }


    init {



        // createNotificationChannel()
      //  setPeriodicNotification()
        createNotificationChannel(Graph.appContext)
        setLocationBackgroundWorker()
        viewModelScope.launch {
            loadUsers()

        }
    }


    private suspend fun loadUsers() {
        combine(
            userRepository.loadUsers()
                .onEach { users1 ->
                    if (users1.isNotEmpty()) {
                        Log.d("ZZZZZZZZZZZZZ:::: ", users.toString())
                        _selectedUser.value = users1.first()
                        loadReminders(_selectedUser.value!!)
                    }
                },
            _selectedUser
        ) { users, selectedUser ->
            _userViewState.value = UserState.Success(selectedUser, users)
            _userList.value = users
        }
            .catch { error -> UserState.Error(error) }
            .launchIn(viewModelScope)
    }


    private suspend fun loadReminders(user: User) {
        combine(
            reminderRepository.loadRemindersByUser(user)
                .onEach { reminders1 ->
                    if (reminders1.isNotEmpty()) {
                        Log.d("ZZZZZZZZZZZZZ:::: ", reminders1.toString())
                        _selectedReminder.value = reminders1.first()

                    }
                },
            _selectedReminder
        ) { reminders, selectedReminder ->
            __reminderState.value = ReminderState.Success(selectedReminder, reminders)
            _remindeList.value = reminders
            LocationRepository.setReminderList(_remindeList.value)
        }
            .catch { error -> ReminderState.Error(error) }
            .launchIn(viewModelScope)
    }

}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "NotificationChannelName"
        val descriptionText = "NotificationChannelDescriptionText"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}


fun createReminderNotification(reminder: Reminder) {
    val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
    val date1 = reminder.reminder_date.toLocalDate().format(formatter)
    val notificationId = 2;
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_id")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("New reminder!!!")
        .setContentText(reminder.message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    with(NotificationManagerCompat.from(Graph.appContext)) {
        if (ActivityCompat.checkSelfPermission(
                Graph.appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("","NOTIFYYYYYYYYYYYYYYYYYYYYYYYYYYY")
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        Log.d("","NOTIFYYYYYYYYYYYYYYYYYYYYYYYYYYY")
        notify(notificationId, builder.build())
    }

}
@SuppressLint("RestrictedApi")
fun setLocationBackgroundWorker() {

    val duration = 15L;//getDuration(reminder)
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val notificationWorker =  PeriodicWorkRequestBuilder<NotificationWorker2>(15, TimeUnit.MINUTES)
        .setInitialDelay(duration, TimeUnit.SECONDS)
        .setConstraints(constraints).addTag("AAA")

        .build()

    workManager
        .enqueueUniquePeriodicWork("AAA", ExistingPeriodicWorkPolicy.REPLACE, notificationWorker)

    //Monitoring for state of work

    LocationRepository().start(Graph.appContext)
    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if ((workInfo != null) ) {

                Log.d(" ","OUTPUT___AAAAA________________::::: " + workInfo.outputData.toString())

                val res = LocationRepository.getCurrentReminder()
                // val myOutputData = workInfo.outputData.getString("KEY_MY_DATA")
            }
        }
}




private fun getDuration(reminder: Reminder): Long {

    val currentTime = LocalDateTime.now()

    val duration =
        currentTime.toEpochSecond(ZoneOffset.UTC) - reminder.reminder_date.toEpochSecond(
            ZoneOffset.UTC)
    Log.d("DURATION_____", "DDDDDDDDD ")

    return duration
}