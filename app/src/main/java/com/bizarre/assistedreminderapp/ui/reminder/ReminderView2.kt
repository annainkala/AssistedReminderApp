import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.bizarre.assistedreminderapp.location.LocationRepository
import com.bizarre.assistedreminderapp.ui.home.AppViewModel

import com.bizarre.assistedreminderapp.ui.reminder.ReminderState


import com.bizarre.assistedreminderapp.ui.theme.Typography
import com.bizarre.assistedreminderapp.ui.utils.ReminderTopAppBar
import com.bizarre.core_domain.entity.Reminder
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ReminderView3(


    navController: NavController,
    update:Boolean,
    viewModel: AppViewModel = hiltViewModel(),


    ) {

    val latlng1 =
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("latlng")



    val reminderViewState by viewModel.reminderState.collectAsState()
    when (reminderViewState) {
        is ReminderState.Loading -> {}
        is ReminderState.Success -> {
            var reminder = (reminderViewState as ReminderState.Success).selectedReminder


            if (!update) {

                reminder = Reminder(
                    message = "",
                    location_x = 0.0,
                    location_y = 0.0,
                    userId = 0,
                    reminder_date = LocalDateTime.now(),
                    creation_date = LocalDateTime.now(),
                    is_seen = false


                )


            }


            Log.d("FFFFFFFFFFF", "SSSSSSSSS")
            Log.d("FFFFFFFFFFF 2:", "SSSSSSSSS " + reminder.toString())

            val openDialog = remember { mutableStateOf(false) }
            androidx.compose.material.Surface() {


                val lat = rememberSaveable { mutableStateOf(reminder!!.location_x) }
                val lng = rememberSaveable { mutableStateOf(reminder!!.location_x) }


                val test: String =
                    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("latlng")?.value.toString()
                Log.d("VVVVVVVVVVV ", " LATLNG: " + test.toString())


                val message = rememberSaveable { mutableStateOf(reminder!!.message) }
                val reminderDate = rememberSaveable { mutableStateOf(reminder!!.reminder_date) }
                val creationDate = rememberSaveable { mutableStateOf(reminder!!.creation_date) }

                val latlng = rememberSaveable {
                    mutableStateOf(LatLng(reminder!!.location_x, reminder!!.location_y))

                }

                if (test != null && test.contains(",")) {
                    latlng.value =
                        LatLng(test.split(",")[0].toDouble(), test.split(",")[1].toDouble())
                }


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
                                navController.navigate("map/{id}")
                            }
                        ) {
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