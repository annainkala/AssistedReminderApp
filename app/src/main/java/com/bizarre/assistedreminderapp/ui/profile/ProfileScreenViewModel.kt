package com.bizarre.assistedreminderapp.ui.profile


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.from
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo

import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.bizarre.assistedreminderapp.Graph
import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.ui.utils.NotificationWorker

import user.User
import user.UserRepository
import java.util.concurrent.TimeUnit

class ProfileScreenViewModel(

): ViewModel() {


   private val userRepository = UserRepository(Graph.appContext)
   private val _userState = MutableStateFlow(ProfileScreenViewState())


   val userState :StateFlow<ProfileScreenViewState>
      get() = _userState




   fun getUser(): User? {

      viewModelScope.launch {
         val user = userRepository.getUser()

         Log.d("UUUUUUUU",user.toString())
         _userState.value = ProfileScreenViewState(user = user)

      }
      return _userState.value.user

   }



   init {
      createNotificationChannel(context = Graph.appContext)
      // setOnetimeNotification()
      viewModelScope.launch {

         getUser()

      }
   }

   fun saveUser(user:User){

      userRepository.updateUser(user)


   }





   fun checkLogin(userName:String,password:String): User? {

      var user = UserRepository(Graph.appContext).getUser();

      Log.d("yyyyyyyy " ,user.toString())
      if (userName == user.userName && password == user.password){
         return user;
      }
      return null

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
      .setContentTitle("Notification success")
      .setContentText("Successfully completed!")
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
   with(NotificationManagerCompat.from(Graph.appContext)){
      notify(notificationId, builder.build())
   }
}

data class ProfileScreenViewState(
   val user:User? = null
)

