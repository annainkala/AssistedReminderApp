package com.bizarre.assistedreminderapp.ui.login


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.bizarre.assistedreminderapp.Graph
import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.ui.home.AppViewModel
import com.bizarre.assistedreminderapp.ui.theme.Typography
import com.bizarre.assistedreminderapp.ui.user.UserState
import java.net.URLEncoder


@Composable
fun LoginScreen(

    navController: NavController,
    viewModel: AppViewModel = hiltViewModel(),

    ) {
    val viewState by viewModel.userState.collectAsState()


    when (viewState) {
        is UserState.Success -> {
            val user = (viewState as UserState.Success).selectedUser


            // val users = (viewState as UserState.Success).data


            Surface(
                modifier = Modifier.fillMaxSize()

            ) {


                val username = rememberSaveable { mutableStateOf("") }
                val password = rememberSaveable { mutableStateOf("") }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {


                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = username.value,
                        onValueChange = { data -> username.value = data },
                        label = {
                            Text(
                                text = stringResource(id = R.string.user_name),
                                style = Typography.body1
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { data -> password.value = data },
                        label = {
                            Text(
                                text = stringResource(id = R.string.password),
                                style = Typography.body1
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = {


                            if(user == null){

                                val prefs = PreferenceManager.getDefaultSharedPreferences(Graph.appContext)

                                val username1 = prefs.getString("userName","Un")
                                val password1 = prefs.getString("password","pw")

                                if (username1 == username.value
                                    && password1 == password.value) {



                                    "profile".replace("{user}", username1)
                                    navController.navigate("profile")

                                }


                            }

                                else {
                                    val user2 = viewModel.users.value[0]
                                    Log.d("66666666666","RRRRRRR 1: " +
                                            viewModel.users.value.toString())
                                    Log.d("XXXXXXX","RRRRRRR 1: " +
                                            user2.userName.toString() + " " + user2.password)

                                    if (user2.userName == username.value &&
                                        user2.password == password.value) {


                                        val uri1 = URLEncoder.encode(user!!.profilePic)
                                        var user1 = user?.firstName
                                       // "home".replace("{user}", username1)
                                        navController.navigate("home/$user1/$uri1")

                                    }

                                }







                        },
                        enabled = true,
                        modifier = Modifier
                            .background(color = MaterialTheme.colors.background)
                            .fillMaxWidth()
                            .padding(10.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor =
                            MaterialTheme.colors.secondary
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colors.secondary)
                    ) {
                        Text(
                            text = stringResource(id = R.string.login),
                            style = Typography.body1,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }


        }


        is UserState.Error -> {

        }
        is UserState.Loading -> {

        }
    }
}











