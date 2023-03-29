import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.ui.reminder.AppViewModel


import com.bizarre.assistedreminderapp.ui.theme.Typography
import com.bizarre.assistedreminderapp.ui.utils.ReminderTopAppBar
import com.bizarre.core_domain.entity.Reminder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ReminderView3(
    navController: NavController,
    viewModel: AppViewModel = hiltViewModel(),

    ){

    androidx.compose.material.Surface() {
        val message = rememberSaveable { mutableStateOf("") }
        var reminderDate = rememberSaveable { mutableStateOf(LocalDateTime.now()) }
        var creationDate = rememberSaveable { mutableStateOf(LocalDateTime.now()) }



       
        val year = reminderDate.value.year
        val month = reminderDate.value.month
        val day = reminderDate.value.dayOfMonth;

    Column(){
        ReminderTopAppBar(navController)
        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm:ss")


        Card() {

            val datePickerDialog = DatePickerDialog(
                LocalContext.current,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    reminderDate = mutableStateOf(LocalDateTime.of(year,month+1,dayOfMonth,0,0,0))
                    Log.d("REMINDER DATE:::","EEEEEEEEEE " + reminderDate.value.toString())

                },reminderDate.value.year,reminderDate.value.monthValue,reminderDate.value.dayOfMonth
            )
            Column(){
                var formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
                Spacer(modifier = Modifier.height(10.dp))

                Row(

                ){
                    Text( reminderDate.value.format(formatter))
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),

                        onClick = {
                            datePickerDialog.show()
                        }
                    ){
                        Text(stringResource(id = R.string.pick_date), style = MaterialTheme.typography.body1)
                    }
                }
               

                    OutlinedTextField(
                    value = message.value,
                    onValueChange = { data -> message.value = data },
                    label = { Text(text = stringResource(id = R.string.message), style = Typography.body1) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),



                )

            }









        }




        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
            Log.d("xxxxxxxxxxxxxxx",reminderDate.toString())
            viewModel.saveReminder(reminder =   Reminder(


                userId = getUserId(viewModel),
                message = message.value,
            location_x = 0.0,
                location_y = 0.0,
            creation_date = creationDate.value,
            reminder_date = reminderDate.value,
            is_seen = false
            ,


            ))
                navController.popBackStack()
            }) {
            
            Text(stringResource(id = R.string.save), style = MaterialTheme.typography.body1)


        }

    
       



    }


    }




}

private fun getUserId(viewModel: AppViewModel): Long {
    return viewModel.users.value[0].userId
}