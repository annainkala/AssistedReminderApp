package com.bizarre.assistedreminderapp.ui.login

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bizarre.assistedreminderapp.Graph

import com.bizarre.assistedreminderapp.R


import com.bizarre.assistedreminderapp.ui.theme.Typography
import user.User
import user.UserRepository
import java.net.URLEncoder


@Composable
fun LoginScreen(

    navController: NavController,

) {
    Surface(modifier =  Modifier.fillMaxSize()

    ) {


        val username = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }

       Column(
           modifier = Modifier.fillMaxSize(),
           horizontalAlignment = Alignment.Start,
           verticalArrangement = Arrangement.Center
       ){





                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = username.value,
                    onValueChange = { data -> username.value = data },
                    label = { Text(text = stringResource(id = R.string.user_name), style = Typography.body1)},
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { data -> password.value = data },
                    label = {Text(text = stringResource(id = R.string.password), style = Typography.body1)},
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = {


                        var result = checkLogin(username.value, password.value)

                        Log.d("XXXXX " ,result?.userName.toString())

                        if (result!= null)
                        {
                            val encodedUrl = URLEncoder.encode(result.profilePic)
                            var username1= result?.firstName + "_" + encodedUrl
                            "home".replace("{user}",username1)
                            navController.navigate("home/$username1")

                        }


                    },
                    enabled = true,
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.background)
                        .fillMaxWidth().padding(10.dp),
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor =
                    MaterialTheme.colors.secondary),
                    border = BorderStroke(1.dp, MaterialTheme.colors.secondary)
                ) {
                    Text(text =stringResource(id = R.string.login), style = Typography.body1,color=MaterialTheme.colors.primary)
                }
            }
        }


}

fun checkLogin(userName:String,password:String): User? {

    var user = UserRepository(Graph.appContext).getUser();

    Log.d("yyyyyyyy " ,user.toString())
    if (userName == user.userName && password == user.password){
        return user;
    }
    return null

}