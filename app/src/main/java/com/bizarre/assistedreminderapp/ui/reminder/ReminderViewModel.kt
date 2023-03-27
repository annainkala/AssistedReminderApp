package com.bizarre.assistedreminderapp.ui.reminder

import android.util.Log
import com.bizarre.core_domain.entity.Reminder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizarre.assistedreminderapp.Graph
import com.bizarre.assistedreminderapp.ui.user.UserState

import com.bizarre.core_domain.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import user.User
import user.UserRepository
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(private val reminderRepository: ReminderRepository,):ViewModel() {
    private val _reminderViewState = MutableStateFlow<ReminderViewState>(ReminderViewState.Loading)

    val reminderState: StateFlow<ReminderViewState> = _reminderViewState
    private val userRepository = UserRepository(Graph.appContext)
    private val _userState = MutableStateFlow(UserState())


    val userState :StateFlow<UserState>
        get() = _userState



    val state:StateFlow<ReminderViewState>
        get() = _reminderViewState



    init {



        dummyReminders().forEach {
            viewModelScope.launch {
                saveReminder(it)
            }
        }

        getReminders()


    }




    fun getUser(): User? {

        viewModelScope.launch {
            val user = userRepository.getUser()

            Log.d("UUUUUUUU",user.toString())
            _userState.value = UserState(user = user)

        }
        return _userState.value.user

    }


    fun saveUser(user:User){

        userRepository.updateUser(user)


    }


    fun saveReminder(reminder:Reminder){
        viewModelScope.launch {
            reminderRepository.addReminder(reminder)
            //  notifyUserOfPayment(payment)
        }
    }

    fun getReminders() {

            viewModelScope.launch {
                val reminders = reminderRepository.loadAllReminders()
                _reminderViewState.value =
                    ReminderViewState.Success(
                        reminders
                    )
            }

    }


}



fun checkLogin(userName:String,password:String): User? {

    var user = UserRepository(Graph.appContext).getUser();

    Log.d("yyyyyyyy " ,user.toString())
    if (userName == user.userName && password == user.password){
        return user;
    }
    return null

}
private fun dummyReminders() : List<Reminder> {
    return listOf(
        Reminder(
            reminderId = 0,
            message = "xxxxxxxxx",
            location_x = 0.55,
            location_y = 0.3,
            reminder_date =  LocalDateTime.now(),
            creation_date =  LocalDateTime.now(),
            creator_email = "@gdf",
            is_seen = false


        ),)

}


