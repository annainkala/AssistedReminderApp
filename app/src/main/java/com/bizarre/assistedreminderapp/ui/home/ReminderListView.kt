import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bizarre.assistedreminderapp.ui.ReminderViewModel

import com.bizarre.assistedreminderapp.ui.reminder.ReminderViewState

import com.bizarre.core_domain.entity.Reminder
import java.time.format.DateTimeFormatter


@Composable
fun ReminderListView(
    navController: NavController,
    viewModel: ReminderViewModel = hiltViewModel(),
){

        viewModel.getReminders()

        val reminderViewState by viewModel.reminderState.collectAsState()
        when (reminderViewState) {
            is ReminderViewState.Loading -> {}
            is  ReminderViewState.Success -> {
                val reminderList = (reminderViewState as ReminderViewState.Success).data

                LazyColumn(
                    contentPadding = PaddingValues(0.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    items(reminderList) { item ->
                       ReminderListItem(reminder = item,viewModel)
                    }
                }
            }
        }



}



@Composable
fun SimpleCheckbox(reminder:Reminder,viewModel: ReminderViewModel) {
    val isChecked = remember { mutableStateOf(reminder.is_seen) }

    Checkbox(checked = isChecked.value, onCheckedChange = { isChecked.value = it
        viewModel.saveReminder(reminder)
    })

}

@Composable
fun ReminderListItem(reminder:Reminder,viewModel: ReminderViewModel){

    val formatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm:ss")

    Card(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        Row (horizontalArrangement = Arrangement.SpaceBetween){
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom
            ){

                Text(reminder.message)
                Text(reminder.creation_date.format(formatter))


            }
            SimpleCheckbox(reminder,viewModel)
        }
    }







}


