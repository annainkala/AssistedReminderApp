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
            "home/{user1}/{uri1}",
            // Fetching the argument which has been passed
        ) {
            val user1 = it.arguments?.getString("user1")
            val uri1 = it.arguments?.getString("uri1")


            HomeScreen(
                user1.toString(),
                uri1.toString(),
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

        val updateString:String=it.arguments!!.getString("updateString")!!
            val id:String?=it.arguments!!.getString("id")


            ReminderView3(

                navController,
                updateString,
                id


            )    // Using that argument in the destination Composabel
        }
    }
}
