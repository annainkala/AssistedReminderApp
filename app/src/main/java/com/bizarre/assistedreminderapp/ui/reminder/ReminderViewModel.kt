package com.bizarre.assistedreminderapp.ui.reminder

import com.bizarre.core_domain.entity.Reminder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizarre.assistedreminderapp.Graph
import com.bizarre.assistedreminderapp.ui.profile.ProfileScreenViewState
import com.bizarre.core_domain.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import user.UserRepository
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(private val reminderRepository: ReminderRepository,):ViewModel() {
    private val _reminderViewState = MutableStateFlow<ReminderViewState>(ReminderViewState.Loading)

    val reminderState: StateFlow<ReminderViewState> = _reminderViewState

    private val userRepository = UserRepository(Graph.appContext)

    private val _userState = MutableStateFlow(ProfileScreenViewState())


    val userState :StateFlow<ProfileScreenViewState>
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


