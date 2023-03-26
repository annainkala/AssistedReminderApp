package com.bizarre.assistedreminderapp.ui.profile

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.from
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizarre.assistedreminderapp.Graph

import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import user.User
import user.UserRepository


class ProfileScreenViewModel(

): ViewModel() {

   private val userRepository = UserRepository(Graph.appContext)

   private val _userState = MutableStateFlow(ProfileScreenViewState())


   val userState :StateFlow<ProfileScreenViewState>
     get() = _userState




   fun getUser():User? {

         viewModelScope.launch {
            val user = userRepository.getUser()

            Log.d("UUUUUUUU",user.toString())
               _userState.value = ProfileScreenViewState(user = user)

         }
      return _userState.value.user

   }



   init {


      viewModelScope.launch {

         getUser()

      }
   }

   fun saveUser(user:User){

      userRepository.updateUser(user)


   }
}


data class ProfileScreenViewState(
   val user:User? = null
)

