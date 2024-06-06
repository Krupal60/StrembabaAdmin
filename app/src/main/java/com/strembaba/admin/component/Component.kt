package com.strembaba.admin.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.strembaba.admin.R

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun textfield(
    modifier: Modifier,
    label: String,
    keyboardType: KeyboardType,
    DefultValue: String
): String {
    // Create a mutable state to store the text field value.
    val textFieldValue = remember { mutableStateOf("") }

    LaunchedEffect(DefultValue) {
        textFieldValue.value = DefultValue
    }

    OutlinedTextField(
        value = textFieldValue.value,
        onValueChange = { textFieldValue.value = it },
        label = { Text(label) },
        singleLine = false,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = false,
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        )
    )
    return textFieldValue.value
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun genresTextfield(
    modifier: Modifier,
    label: String,
    keyboardType: KeyboardType,
    genres: String
): String {
    // Create a mutable state to store the text field value.
    val textFieldValue = remember { mutableStateOf("") }

    LaunchedEffect(genres) {
        textFieldValue.value =  genres.replace("[","").replace("]","")
    }
    // Create a list to store the genres.

    OutlinedTextField(
        value = textFieldValue.value,
        onValueChange = { textFieldValue.value = it },
        label = { Text(label) },
        singleLine = false,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = false,
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        )
    )

    return textFieldValue.value
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ratingTextfield(
    modifier: Modifier,
    label: String,
    keyboardType: KeyboardType,
    rating: String
): String {
    // Create a mutable state to store the text field value.
    val textFieldValue = remember { mutableStateOf("") }

    LaunchedEffect(rating) {
        textFieldValue.value =  rating
    }
    OutlinedTextField(
        value = textFieldValue.value,
        onValueChange = { textFieldValue.value = it },
        label = { Text(label) },
        maxLines = 1,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = false,
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        )
    )
    return textFieldValue.value
}
@Composable
fun CircularProgressButton(modifier: Modifier, text: String, onclick: () -> Unit, value: Boolean) {
Button(modifier = modifier.height(45.dp), onClick = { onclick() }) {
    Text(text)
    val strokeWidth = 4.dp



    AnimatedVisibility(visible = value) {
        Spacer(modifier = modifier.padding(start = 8.dp))
        CircularProgressIndicator(
            modifier = modifier.then(modifier.size(28.dp)),
            color = Color.Black,
            strokeWidth = strokeWidth
        )
    }
}
}

@Composable
fun DeleteCircularProgressButton(modifier: Modifier, text: String, onclick: () -> Unit, value: Boolean) {
Button(modifier = modifier.height(45.dp), onClick = { onclick() }) {
    Icon(
        painter = painterResource(id = R.drawable.delete),
        contentDescription = text
    )
    val strokeWidth = 4.dp


    AnimatedVisibility(visible = value) {
        Spacer(modifier = modifier.padding(start = 8.dp))
        CircularProgressIndicator(
            modifier = modifier.then(modifier.size(28.dp)),
            color = Color.Black,
            strokeWidth = strokeWidth
        )
    }
}
}