package com.bizarre.assistedreminderapp.ui.login

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.*
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
import coil.compose.AsyncImage
import com.bizarre.assistedreminderapp.Graph

import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.ui.ProfileImage
import com.bizarre.assistedreminderapp.ui.reminder.AppViewModel


import com.bizarre.assistedreminderapp.ui.theme.Typography
import com.bizarre.assistedreminderapp.ui.user.UserState
import com.bizarre.assistedreminderapp.ui.utils.ReminderTopAppBar
import com.bizarre.core_domain.entity.User
import com.bizarre.core_domain.repository.UserRepository

import java.net.URLEncoder


@Composable
fun ProfileScreen(

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


                val username = rememberSaveable { mutableStateOf(user?.userName) }
                val password = rememberSaveable { mutableStateOf(user?.password)  }
                val firstname = rememberSaveable { mutableStateOf(user?.firstName)  }
                val lastName = rememberSaveable { mutableStateOf(user?.lastName)  }
                val imageUri1 = rememberSaveable { mutableStateOf(user?.profilePic)  }
                val userEmail = rememberSaveable { mutableStateOf(user?.userEmail)  }




                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ){

                    ReminderTopAppBar(navController)

                    var imageUri by remember {
                        mutableStateOf<Uri?>(null)
                    }
                    val imageLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.PickVisualMedia(),
                        onResult = { uri -> imageUri = uri
                            imageUri1.value = imageUri.toString()}
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        if(imageUri == null){
                            ProfileImage(user?.profilePic.toString(),200)
                        }
                        else{
                            AsyncImage(

                                model = imageUri,
                                contentDescription = "image"
                            )
                        }

                        if(imageUri == null){
                            Button(onClick = {

                                imageLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                                )

                            }) {

                                Text(text = "Pick an Image")

                            }
                        }

                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ){
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = firstname.value.toString(),
                            onValueChange = { data -> firstname.value = data },
                            label = { Text(text = stringResource(id = R.string.first_name), style = Typography.body1)},
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(

                            value = lastName.value.toString(),
                            onValueChange = { data -> lastName.value = data },
                            label = {Text(text = stringResource(id = R.string.last_name), style = Typography.body1)},
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = username.value.toString(),
                            onValueChange = { data -> username.value = data },
                            label = { Text(text = stringResource(id = R.string.user_name), style = Typography.body1)},
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = password.value.toString(),
                            onValueChange = { data -> password.value = data },
                            label = {Text(text = stringResource(id = R.string.password), style = Typography.body1)},
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = userEmail.value.toString(),
                            onValueChange = { data -> userEmail.value = data },
                            label = {Text(text = stringResource(id = R.string.email), style = Typography.body1)},
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = {

                                Log.d("","IMAGEURI::: " + imageUri1.value.toString())

                                val user =  User(
                                    firstName = firstname.value.toString(),
                                    lastName = lastName.value.toString(),
                                    password = password.value.toString(),
                                    userName = username.value.toString(),
                                    profilePic = imageUri1.value.toString(),
                                    userEmail = userEmail.value.toString(),
                                    userId = user?.userId!!,


                                    )

                                viewModel.saveUser(user)


                                val encodedUrl = URLEncoder.encode(user.profilePic)
                                var username1= user?.firstName + "_" + encodedUrl

                                //  "home".replace("{user}","sss")
                                "home".replace("{user}",username1)
                                navController.navigate("home/$username1")






                            },
                            enabled = true,
                            modifier = Modifier
                                .background(color = MaterialTheme.colors.background)
                                .fillMaxWidth()
                                .padding(10.dp),
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.outlinedButtonColors(backgroundColor =
                            MaterialTheme.colors.secondary),
                            border = BorderStroke(1.dp, MaterialTheme.colors.secondary)
                        ) {
                            Text(text =stringResource(id = R.string.login), style = Typography.body1, color = MaterialTheme.colors.secondary)
                        }
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











