package user

import android.provider.ContactsContract
import java.time.LocalDateTime

data class User(

    val firstName: String,
    val lastName: String,
    val password: String,
    val userName: String,
    val profilePic: String,
    val userEmail: String,
)