package com.bizarre.assistedreminderapp.ui.home

import ReminderListView
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel


import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.ui.ProfileImage



import com.bizarre.assistedreminderapp.ui.theme.Typography
import java.net.URLDecoder

@Composable
fun HomeScreen(

    text1:String,
    navController: NavController,

) {


    val name1 = text1.split("_")[0]
    val uri = text1.split("_")[1]//URLDecoder.decode(text1.split("_")[1])

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(route = "reminder") },
                    contentColor = Color.Blue,
                    modifier = Modifier.padding(all = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        ) {


            Icon(  imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(50.dp))
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = Typography.body1,

                            color = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .heightIn(24.dp)
                        )


                    },
                    backgroundColor = backgroundColor,
                    actions = {


                        Box(modifier = Modifier
                            .size(70.dp)
                            .clickable {
                                navController.navigate("profile")
                            }){
                            ProfileImage(uri,70)


                        }

                    }
                )

                Text("Hello $name1}!!!!")
                ReminderListView(navController)














            }
        }

}

