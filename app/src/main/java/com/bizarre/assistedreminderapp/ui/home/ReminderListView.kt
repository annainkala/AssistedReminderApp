import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.ui.home.AppViewModel


import com.bizarre.assistedreminderapp.ui.reminder.ReminderState

import com.bizarre.core_domain.entity.Reminder
import com.bizarre.core_domain.entity.User
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun ReminderListView(
    navController: NavController,
    user: User,
    viewModel: AppViewModel = hiltViewModel(),
){



        val reminderViewState by viewModel.reminderState.collectAsState()
        when (reminderViewState) {
            is ReminderState.Loading -> {}
            is  ReminderState.Success -> {
                val reminderList = (reminderViewState as ReminderState.Success).data
                Log.d("ZZZZZZZZZ ", " " + user.toString())
                LazyColumn(
                    contentPadding = PaddingValues(0.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    items(reminderList) { item ->
                       ReminderListItem(reminder = item,viewModel,navController)


                    }
                }
            }
        }



}

@Composable
fun ReminderListItem(reminder:Reminder,viewModel: AppViewModel, navController: NavController){

    val formatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm:ss")
    val openDialog = remember { mutableStateOf(false)  }
    val isChecked = remember {
        mutableStateOf(false)
    }
    val currentTime = LocalDateTime.now()

    Log.d(""," TTIME 1:::::: " + reminder.reminder_date.toString() + " " + currentTime.toString())




        Log.d(""," TTIME 2:::::: " + reminder.reminder_date.toString() + " " + currentTime.toString())


        OutlinedButton(modifier = Modifier .pointerInput(Unit){
            /*  detectTapGestures(
                  onLongPress = {
                      openDialog.value = true
                  }
              )*/
        }, onClick = {
            Log.d("IIIIIII: ", "ITEM:::: " + reminder.reminderId.toString())

            if(isChecked.value){
               openDialog.value = true
            }
            else{


                val updateString = "true"
                val id = reminder.reminderId

                navController.navigate("reminder/$updateString/$id")



            }

          //  openDialog.value = true
        }) {


            Row (horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()){
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Bottom
                ){
                    Text(reminder.title ,style = MaterialTheme.typography.body1)
                    Text(reminder.message, style = MaterialTheme.typography.body1)
                    Text(reminder.reminder_date.format(formatter), style = MaterialTheme.typography.body1)


                }
               isChecked.value =  simpleCheckbox(reminder,viewModel,isChecked)
            }


    }

    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                openDialog.value = false
            },
            /*title = {
                Text(stringResource(id = R.string.delete_message)
                    , style = MaterialTheme.typography.body1)
            },*/

            confirmButton = {
                OutlinedButton(

                    onClick = {
                        openDialog.value = false
                        viewModel.deleteReminder(reminder)

                    }) {
                    Text(stringResource(id = R.string.delete_message)
                        , style = MaterialTheme.typography.body1)
                }

            },



            )
    }

}


@Composable
fun simpleCheckbox(reminder:Reminder, viewModel: AppViewModel, isChecked:MutableState<Boolean>):Boolean {


    Checkbox(checked = isChecked.value, onCheckedChange = { isChecked.value = it


    })
return isChecked.value
}









