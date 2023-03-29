package com.bizarre.assistedreminderapp.ui.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bizarre.core_domain.entity.Reminder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.bizarre.assistedreminderapp.Graph
import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.ui.user.UserState
import com.bizarre.assistedreminderapp.ui.utils.NotificationWorker
import com.bizarre.core_domain.entity.User

import com.bizarre.core_domain.repository.ReminderRepository

import com.bizarre.core_domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val reminderRepository: ReminderRepository,
                                       private val userRepository:UserRepository,):ViewModel() {





    private val __reminderState = MutableStateFlow<ReminderState>(ReminderState.Loading)
    val reminderState: StateFlow<ReminderState> = __reminderState

    private val _userList: MutableStateFlow<List<User>> = MutableStateFlow(mutableListOf())
    val users: StateFlow<List<User>> = _userList

    private val _userViewState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userViewState

    private val _selectedUser = MutableStateFlow<User?>(null)

    fun saveReminder(reminder: Reminder) {
        viewModelScope.launch {
            Log.d("SAVE::::::: ", reminder.toString())
            reminderRepository.addReminder(reminder)
            setOneTimeNotification(reminder)


        }
    }

    fun saveUser(user:User) {
        viewModelScope.launch {
           val result =  userRepository.addUser(user)
            Log.d("HHHHHHHHHHH","SAVE USER:::::: " + user.toString() + result.toString())
            loadUsers()
            // notifyUserOfReminder(reminder)
        }
    }

    fun updateUser(user:User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
            Log.d("HHHHHHHHHHH","SAVE USER:::::: " + user.toString() )
            loadUsers()
            // notifyUserOfReminder(reminder)
        }
    }


    fun onUserSelected(user: User) {
        _selectedUser.value = user
    }

fun deleteReminder(reminder:Reminder){
    viewModelScope.launch {
         reminderRepository.deleteReminder(reminder)
        loadRemindersFor(_selectedUser.value!!)
        //Log.d("DDDDDDDD","DELETE:::::: ")

        // notifyUserOfReminder(reminder)
    }
}

    fun loadRemindersFor(user: User?) {
        if (user != null) {
            viewModelScope.launch {
                val reminders = reminderRepository.loadRemindersByUser(user)
                __reminderState.value =
                    ReminderState.Success(
                        data = reminders.first()
                    )
            }
        }
    }

    init {
       // createNotificationChannel()


        viewModelScope.launch {
            loadUsers()


        }



    }



private suspend fun loadUsers() {
        combine(
            userRepository.loadUsers()
                .onEach { users ->
                    if (users.isNotEmpty()) {
                        Log.d("ZZZZZZZZZZZZZ:::: ", users.toString())
                        _selectedUser.value = users.first()
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
}


private fun createNotificationChannel(context: Context){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val name = "NotificationChannelName"
        val descriptionText = "NotificationChannelDescriptionText"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("CHANNEL_ID",name,importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}


private fun createReminderNotification(reminder: Reminder){
    val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
    val date1 = reminder.reminder_date.toLocalDate().format(formatter)
    val notificationId = 2;
    val builder = NotificationCompat.Builder(Graph.appContext,"CHANNEL_id")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("New reminder!!!")
        .setContentText("Your reminder for $$(reminder.message) on $(date1) has been saved!!!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    with(NotificationManagerCompat.from(Graph.appContext)){
        notify(notificationId,builder.build())
    }

}
private fun setOneTimeNotification(reminder:Reminder) {

    val duration = getDuration(reminder)
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(duration, TimeUnit.SECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(notificationWorker)

    //Monitoring for state of work
    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                createReminderNotification(reminder)
            } else {
                //  createErrorNotification()
            }
        }
}

fun getDuration(reminder:Reminder):Long{

    val currentTime = LocalDateTime.now()

    val duration = currentTime.toEpochSecond(ZoneOffset.UTC)-reminder.reminder_date.toEpochSecond(ZoneOffset.UTC)
    Log.d("DURATION_____" , "DDDDDDDDD ")

    return duration
}
