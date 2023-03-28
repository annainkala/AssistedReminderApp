
package com.bizarre.core_domain.repository
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.bizarre.core_domain.entity.User

class UserPreferenceRepository(context:Context) {

    private val pref: SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    fun addUserLogin() {

        var editor = pref.edit()
        editor.putString("firstName", "Lois")

        editor.putString("userName", "Un")
        editor.putString("password", "pw")

        editor.commit()
        var s2 = pref.getString("firstName","")
        Log.d("KLIKKKKKKKKKKKKKK 2",s2 .toString())


    }



    fun getUserName(): String {

      return pref.getString("userName", "").toString()
    }

    fun geName(): String {

        return pref.getString("firstName", "").toString()
    }

    fun getPassword(): String {

        return pref.getString("password", "").toString()
    }
}






