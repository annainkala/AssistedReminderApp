package user

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class UserRepository(context:Context) {

    private val pref: SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

  fun addUser(user: User) {
      var s = user.firstName
        Log.d("KLIKKKKKKKKKKKKKK", s.toString())

        var editor = pref.edit()
      editor.putString("firstName", user.firstName)
      editor.putString("lastName", user.lastName)
        editor.putString("userName", user.userName)
        editor.putString("password", user.password)
      editor.putString("userEmail", user.userEmail)
        editor.commit()
      var s2 = pref.getString("firstName","")
        Log.d("KLIKKKKKKKKKKKKKK 2",s2 .toString())


    }

fun updateUser(user: User) {


        var editor = pref.edit()
        if (user.firstName != "")
            editor.putString("firstName", user.firstName)
        if (user.lastName != "")
            editor.putString("lastName", user.lastName)
        if (user.password != "")
            editor.putString("username", user.password)
        if (user.userName != "")
            editor.putString("password", user.userName)
    if (user.profilePic != "")
        editor.putString("profilePic", user.profilePic)
    if (user.profilePic != "")
        editor.putString("userEmail", user.userEmail)
        editor.commit()
    }

 fun getUser(): User {
     var s2 = pref.getString("firstName","")
     Log.d("KLIKKKKKKKKKKKKKK 3 ",s2 .toString())

     var user = User(


         pref.getString("firstName", "").toString(),
         pref.getString("lastName", "").toString(),
         pref.getString("password", "").toString(),
         pref.getString("userName", "").toString(),
         pref.getString("profilePic", "").toString(),
         pref.getString("userEmail", "").toString(),

         )
     Log.d("KLIKKKKKKKKKKKKKK 4 ",user.firstName .toString())
        return user
    }
}






