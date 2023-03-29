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
import com.bizarre.assistedreminderapp.Graph
import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.ui.user.UserState
import com.bizarre.core_domain.entity.User

import com.bizarre.core_domain.repository.ReminderRepository

import com.bizarre.core_domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val reminderRepository: ReminderRepository,
                                       private val userRepository:UserRepository,):ViewModel() {

    private val _userState2 = MutableStateFlow(UserPreferenceState())

    val userState2 :StateFlow<UserPreferenceState>
        get() = _userState2

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
            // notifyUserOfReminder(reminder)


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


    fun onUserSelected(user: User) {
        _selectedUser.value = user
    }



    fun loadRemindersFor(user: User?) {
        if (user != null) {
            viewModelScope.launch {
                val reminders = reminderRepository.loadAllReminders()
                __reminderState.value =
                    ReminderState.Success(
                        reminders
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

private fun createSuccessNotification(){
    val notificationId= 1
    val builder = NotificationCompat.Builder(Graph.appContext,"CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("WWWWWWW")
        .setContentText("Successfully completed!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with(NotificationManagerCompat.from(Graph.appContext)){
        notify(notificationId, builder.build())
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


data class UserPreferenceState(
    val username:String = "",
val password:String = "",
val firstname:String = ""
)
