package com.bizarre.assistedreminderapp.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bizarre.assistedreminderapp.R



@Composable
fun ProfileImage(uri:String,size:Int) {


Log.d("URI______",uri.toString())




        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = stringResource(R.string.profile_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size.dp)
                    .clip(CircleShape)
                    .border(0.dp, Color.Red, CircleShape)
            )
        }
    }

