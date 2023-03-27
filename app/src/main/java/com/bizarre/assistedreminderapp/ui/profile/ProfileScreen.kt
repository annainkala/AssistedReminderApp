package com.bizarre.assistedreminderapp.ui.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bizarre.assistedreminderapp.Graph

import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.ui.ProfileImage
import com.bizarre.assistedreminderapp.ui.reminder.ReminderViewModel

import com.bizarre.assistedreminderapp.ui.theme.Typography
import com.bizarre.assistedreminderapp.ui.utils.ReminderTopAppBar
import user.User
import user.UserRepository
import java.net.URLEncoder


@Composable
fun ProfileScreen(
    viewModel: ReminderViewModel= hiltViewModel(),
    navController: NavController,


    ) {
    Surface(modifier =  Modifier.fillMaxSize())
    {


        val username = rememberSaveable { mutableStateOf( viewModel.getUser()?.userName) }
        val password = rememberSaveable { mutableStateOf(viewModel.getUser()?.password)  }
        val firstname = rememberSaveable { mutableStateOf(viewModel.getUser()?.firstName)  }
        val lastName = rememberSaveable { mutableStateOf(viewModel.getUser()?.lastName)  }
        val imageUri1 = rememberSaveable { mutableStateOf(viewModel.getUser()?.profilePic)  }
        val userEmail = rememberSaveable { mutableStateOf(viewModel.getUser()?.userEmail)  }




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
                    ProfileImage(viewModel.getUser()?.profilePic.toString(),200)
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
                            firstname.value.toString(),
                            lastName.value.toString(),
                            password.value.toString(),
                            username.value.toString(),
                            profilePic = imageUri1.value.toString(),
                            userEmail.value.toString(),

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






