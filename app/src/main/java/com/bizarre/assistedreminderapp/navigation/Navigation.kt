package com.bizarre.assistedreminderapp.navigation




import ReminderView3
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.bizarre.assistedreminderapp.ui.home.HomeScreen


import com.bizarre.assistedreminderapp.ui.login.LoginScreen
import com.bizarre.assistedreminderapp.ui.map.MapScreen
import com.bizarre.assistedreminderapp.ui.profile.ProfileScreen


@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }

        composable(
            "home/{user}",
            // Fetching the argument which has been passed
        ) {
            val user = it.arguments?.getString("user")


            HomeScreen(
                user.toString(),
                navController
            )    // Using that argument in the destination Composabel
        }









        composable("profile") {
            ProfileScreen(navController = navController)
        }



        composable(
            "map/{id}/{latlng}",
            // Fetching the argument which has been passed
        ) {
            val id :String= it.arguments?.getString("id")!!
            val latlng:String = it.arguments?.getString("latlng")!!



            MapScreen(
                navController,
                id,
                latlng

            )    // Using that argument in the destination Composabel
        }




        composable(
            "reminder/{updateString}/{id}",
            // Fetching the argument which has been passed
        ) {

        val updateString:Boolean=it.arguments!!.getBoolean("updateString")!!
            val id:Int?=it.arguments!!.getInt("id")


            ReminderView3(

                navController,
                updateString,
                id


            )    // Using that argument in the destination Composabel
        }
    }
}
