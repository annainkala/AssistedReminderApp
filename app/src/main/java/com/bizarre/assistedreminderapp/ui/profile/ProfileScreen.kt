package com.bizarre.assistedreminderapp.ui.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
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
            val users =  (viewState as UserState.Success).data

            Log.d(" ","USERRRRR::::: " +users.toString() )

            // val users = (viewState as UserState.Success).data

            val username = rememberSaveable { mutableStateOf(user?.userName) }
            val password = rememberSaveable { mutableStateOf(user?.password) }
            val firstname = rememberSaveable { mutableStateOf(user?.firstName) }
            val lastName = rememberSaveable { mutableStateOf(user?.lastName) }
            val imageUri1 = rememberSaveable { mutableStateOf(user?.profilePic) }
            val userEmail = rememberSaveable { mutableStateOf(user?.userEmail) }

            Surface(
                modifier = Modifier.fillMaxSize()

            ) {



                Log.d("PIC::::: ", "PPPP " + imageUri1.value)


                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    ReminderTopAppBar(navController)

                    var imageUri by remember {
                        mutableStateOf<Uri?>(null)
                    }
                    val imageLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.PickVisualMedia(),
                        onResult = { uri ->
                            imageUri = uri
                            imageUri1.value = imageUri.toString()
                        }
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AsyncImage(
                            modifier = Modifier.clip(CircleShape).size(200.dp),

                            model = imageUri1.value,
                            contentDescription = "image"
                        )


                            OutlinedButton(

                                modifier = Modifier.background(color=MaterialTheme.colors.background),
                                onClick = {

                                    imageLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                                    )

                                }) {

                                Text(text = stringResource(id = R.string.pick_image),
                                    style = MaterialTheme.typography.body1)

                            }


                    }

                    Column() {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 70.dp)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = firstname.value.toString(),
                                onValueChange = { data -> firstname.value = data },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.first_name),
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

                                value = lastName.value.toString(),
                                onValueChange = { data -> lastName.value = data },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.last_name),
                                        style = Typography.body1
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text
                                ),

                                )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = username.value.toString(),
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
                                value = password.value.toString(),
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
                            OutlinedTextField(
                                value = userEmail.value.toString(),
                                onValueChange = { data -> userEmail.value = data },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.email),
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


                        }


                    }


                }
            }

            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)){


                OutlinedButton(
                    onClick = {

                        Log.d("","IMAGEURI::: " + imageUri1.value.toString())
                        var firstName1 = user!!.firstName
                        if(firstname.value != "") {
                            firstName1 = firstname.value.toString()
                        }
                        var lastName1 = user!!.lastName
                        if(lastName.value != "") {
                            lastName1 = lastName.value.toString()
                        }

                        var userName1 = user!!.userName
                        if(username.value != "") {
                            userName1 = username.value.toString()
                        }
                        var password1 = user!!.password
                        if(password.value != "") {
                            password1= password.value.toString()
                        }

                        var email1 = user!!.userEmail
                        if(userEmail.value != "") {
                            email1= userEmail.value.toString()
                        }

                        var profilePic1 = user!!.profilePic
                        if(imageUri1.value != "") {
                            profilePic1= imageUri1.value.toString()
                        }



                        val user =  User(
                            firstName = firstName1,
                            lastName = lastName1,
                            password =password1,
                            userName = userName1,
                            profilePic = profilePic1,
                            userEmail = email1,
                            userId = user?.userId!!,


                            )

                        Log.d("XXXXXXX","XXXXXXXXX " + user.toString())

                        viewModel.updateUser(user)


                        val encodedUrl = URLEncoder.encode(user.profilePic)
                        var username1= user?.firstName + "_" + encodedUrl

                        //  "home".replace("{user}","sss")
                        "home".replace("{user}",username1)
                        navController.navigate("home/$username1")






                    },
                    enabled = true,
                    modifier = Modifier

                        .background(color = MaterialTheme.colors.primary)
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(70.dp)
                        .align(Alignment.BottomCenter),
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor =
                    MaterialTheme.colors.background),
                    border = BorderStroke(1.dp, MaterialTheme.colors.secondary)
                ) {
                    Text(text =stringResource(id = R.string.save), style = Typography.body1,
                        color = MaterialTheme.colors.secondary)
                }

            }

        }


        is UserState.Error -> {

        }
        is UserState.Loading -> {

        }
    }
}











