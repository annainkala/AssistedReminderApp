package com.bizarre.assistedreminderapp.location

import android.content.Context
import android.util.Log
import com.bizarre.assistedreminderapp.Graph
import com.bizarre.assistedreminderapp.ui.home.AppViewModel
import com.bizarre.assistedreminderapp.ui.reminder.ReminderState
import com.bizarre.core_domain.entity.Reminder
import com.google.android.gms.maps.model.LatLng

class LocationRepository(){



fun start(context1:Context){

    LocationClient().getLocation(context1)
}




companion object{

    var loc = LatLng(0.00,0.00)
    var reminder:Reminder? = null
    var reminders = emptyList<Reminder>()
    fun setReminderList(reminders1:List<Reminder>) {

     reminders = reminders1

    }


    fun setLocation2(loc2:LatLng){
        Log.d("","LAT2:::: " + loc.toString())
        loc = loc2

    }

    fun setCurrentReminder(reminder1:Reminder){

        reminder = reminder1
        Log.d("","SET CURRENT REMINDER:::: "  + reminder.toString())

    }


    fun getCurrentReminder():Reminder?{


        Log.d("","GET CURRENT REMINDER:::: " + reminder.toString() )
        return  reminder
    }

    fun getLocation2():LatLng{

        return loc

    }

    fun getReminderList():List<Reminder>{

        Log.d(" ", "REMINDRES::::::::::" + reminders.size.toString())
        return reminders


    }

}



}