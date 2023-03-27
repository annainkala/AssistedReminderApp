import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bizarre.assistedreminderapp.Graph
import com.bizarre.assistedreminderapp.R
import com.bizarre.assistedreminderapp.ui.profile.ProfileScreenViewModel
import com.bizarre.assistedreminderapp.ui.reminder.ReminderViewModel


import com.bizarre.assistedreminderapp.ui.theme.Typography
import com.bizarre.assistedreminderapp.ui.utils.ReminderTopAppBar
import com.bizarre.core_domain.entity.Reminder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ReminderView(
    navController: NavController,
viewModel: ReminderViewModel = hiltViewModel(),

){
   val userViewModel=  ProfileScreenViewModel()
    androidx.compose.material.Surface() {
        val message = rememberSaveable { mutableStateOf("") }
        var reminderDate = rememberSaveable { mutableStateOf(LocalDateTime.now()) }
        var creationDate = rememberSaveable { mutableStateOf(LocalDateTime.now()) }
        val creatorEmail = rememberSaveable { mutableStateOf("") }


       
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
                    reminderDate = mutableStateOf(LocalDateTime.of(year,month,day,0,0,0))

                },reminderDate.value.year,reminderDate.value.monthValue,reminderDate.value.dayOfMonth
            )
            Column(){
                var formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss")
                Spacer(modifier = Modifier.height(10.dp))

                Row(){
                    Text( LocalDateTime.now().format(formatter))
                    Button(
                        onClick = {
                            datePickerDialog.show()
                        }
                    ){
                        Text("Pcik a day")
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
            0,
            message.value,
            0.0,0.0,
            creationDate.value,
            reminderDate.value,
            userViewModel.getUser()?.userName.toString(),
            false
            ,


            ))
                navController.popBackStack()
            }) {
            
            Text(stringResource(id = R.string.save), style = MaterialTheme.typography.body1)


        }

    
       



    }


    }




}
