package com.bizarre.assistedreminderapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bizarre.assistedreminderapp.navigation.Navigation


import com.bizarre.assistedreminderapp.ui.theme.AssistedReminderAppTheme
import com.bizarre.core_domain.entity.User
import com.bizarre.core_domain.repository.UserRepository

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            AssistedReminderAppTheme() {

             //   UserRepository(this).addUser(User("Lois","Griffin", password = "pw", userName = "Un", profilePic = "", userEmail = ""))

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize() ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
   AssistedReminderAppTheme() {
        Greeting("Android")
    }
}