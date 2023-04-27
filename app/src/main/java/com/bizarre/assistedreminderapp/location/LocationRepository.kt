package com.bizarre.assistedreminderapp.location

import android.content.Context
import android.util.Log
import com.bizarre.core_domain.entity.Reminder
import com.google.android.gms.maps.model.LatLng

class LocationRepository(){




companion object{
var update:Boolean = false

    var reminder:Reminder? = null
    var reminders = emptyList<Reminder>()
    fun setReminderList(reminders1:List<Reminder>) {

     reminders = reminders1

    }



var _selectedreminder:Reminder? = null


    fun setSelectedReminder(reminder:Reminder){

        _selectedreminder = reminder

    }

fun getSelectedReminder():Reminder{
    return _selectedreminder!!;
}


    fun getReminderList():List<Reminder>{

        Log.d(" ", "REMINDRES::::::::::" + reminders.size.toString())
        return reminders


    }







}



}