import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.ui.home.AppViewModel

import com.bizarre.assistedreminderapp.ui.reminder.ReminderState


import com.bizarre.assistedreminderapp.ui.theme.Typography
import com.bizarre.assistedreminderapp.ui.utils.ReminderTopAppBar
import com.bizarre.core_domain.entity.Reminder
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ReminderView3(


    navController: NavController,
    updateString:String,
    id: String?,
    viewModel: AppViewModel = hiltViewModel(),


    ) {

    val latlng1 =
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("latlng")



    val update = updateString.toBoolean()
    val reminderViewState by viewModel.reminderState.collectAsState()
    when (reminderViewState) {
        is ReminderState.Loading -> {}
        is ReminderState.Success -> {
          val reminders =   (reminderViewState as ReminderState.Success).data


            var reminder0 :Reminder? = null

            for (reminder in reminders){
                if(reminder.reminderId.toInt() == id?.toInt()){
                    reminder0 = reminder
                }
            }
           val  reminder = remember{ mutableStateOf(reminder0) }


            val test: String =
                navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("latlng")?.value.toString()




            if (!update) {

                reminder.value = Reminder(
                    message = "",
                    location_x = 65.0121,
                    location_y = 25.4651,
                    userId = 0,
                    reminder_date = LocalDateTime.now(),
                    creation_date = LocalDateTime.now(),
                    is_seen = false


                )


            }

            val latlng = rememberSaveable {
                mutableStateOf(LatLng(reminder!!.value!!.location_x, reminder!!.value!!.location_y))

            }

         if(latlng == null){

             latlng.value = LatLng( 65.0121,25.4651)
         }

            val openDialog = remember { mutableStateOf(false) }
            androidx.compose.material.Surface() {



                if (test != null && test.contains(",")) {
                    latlng.value =
                        LatLng(test.split(",")[0].toDouble(), test.split(",")[1].toDouble())
                }



                val message = rememberSaveable { mutableStateOf(reminder!!.value!!.message) }
                val reminderDate = rememberSaveable { mutableStateOf(reminder!!.value!!.reminder_date) }
                val creationDate = rememberSaveable { mutableStateOf(reminder!!.value!!.creation_date) }


                val context = LocalContext.current
                val calendar = Calendar.getInstance()

                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH]
                val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]


                val datePicker = DatePickerDialog(
                    context,
                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->

                        reminderDate.value = LocalDateTime.of(
                            selectedYear,
                            selectedMonth + 1,
                            selectedDayOfMonth,
                            0,
                            0,
                            0
                        )

                    }, year, month, dayOfMonth
                )

                Column() {
                    ReminderTopAppBar(navController)
                    val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {

                        // Text( reminderDate.value.format(formatter))
                        Log.d("XXXXXXX________"," LAT LNG:::: 111111")
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),

                            onClick = {
                                datePicker.show()
                            }
                        ) {
                            Text(
                                stringResource(id = R.string.pick_date),
                                style = MaterialTheme.typography.body1
                            )
                        }



                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),

                            onClick = {
                                Log.d("XXXXXXX________"," LAT LNG:::: 0000000")
                                val id1 = reminder!!.value!!.reminderId
                                val lat = latlng.value.latitude.toString()
                                val lng = latlng.value.longitude.toString()


                                val latlngString = lat + "," + lng


                            Log.d("XXXXXXX________"," LAT LNG:::: " + latlngString.toString())


                                navController.navigate("map/${id1.toString()}/$latlngString")


                            }
                        ) {
                            Log.d("XXXXXXX________"," LAT LNG:::: 222222222 " )
                            Text(
                                stringResource(id = R.string.map),
                                style = MaterialTheme.typography.body1
                            )
                        }

                    }
                    Text(

                        "Date: ${reminderDate.value.format(formatter).toString()}"

                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    var text = ""
                    if (latlng != null) {
                        text =
                            latlng.value.latitude.toString() + " " + latlng.value.longitude.toString()
                    }
                    Text("Location: " + text)
                    Spacer(modifier = Modifier.height(50.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction = 0.5f)
                    ) {


                        OutlinedTextField(
                            value = message.value,
                            onValueChange = { data -> message.value = data },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.message),
                                    style = Typography.body1
                                )
                            },
                            modifier = Modifier
                                .fillMaxSize(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),


                            )

                    }




                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val index = 0;

                            val reminder1 =    Reminder(
                                message = message.value,
                                creation_date = creationDate.value,
                                reminder_date = reminderDate.value,
                                location_y = latlng.value.longitude,
                                location_x = latlng.value.latitude,
                                userId = viewModel.user.value!!.userId,
                                is_seen = false,


                                )

                            if(!update){
                                viewModel.saveReminder(
                                 reminder1,

                                )


                            }
                            else{
                                viewModel.updateReminder(
                                    reminder1,

                                )
                            }






                            navController.popBackStack()


                        }) {

                        Text(
                            stringResource(id = R.string.save),
                            style = MaterialTheme.typography.body1
                        )


                    }


                    /*



                     */


                }
            }


        }


    }


}


private fun getUserId(viewModel: AppViewModel): Long {
    return viewModel.users.value[0].userId
}