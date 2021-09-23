package com.trian.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trian.component.ui.theme.BluePrimary
import compose.icons.Octicons
import compose.icons.octicons.Person24

@Composable
fun BottomSheetCancelOrder(
    m: Modifier=Modifier,
){
    val bankName = remember { mutableStateOf(TextFieldValue(""))}
    val accountNumber = remember { mutableStateOf(TextFieldValue(""))}
    val accountName = remember{ mutableStateOf(TextFieldValue(""))}
    Box(
        modifier = m.background(color = Color.White)
    ){
        Column(
            modifier = m.padding(10.dp)
        ){
            Text(text = "Cancellation",style = MaterialTheme.typography.h1.copy(
                fontSize = 18.sp,
                letterSpacing = 0.1.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            ))
            Spacer(modifier = m.height(10.dp))
            TextField(
                value = bankName.value,
                leadingIcon = {Icon(Octicons.Person24, contentDescription ="" )},
                onValueChange = {bankName.value=it},
                placeholder = {Text(text = "Bank name")},
                singleLine = true,
                modifier = m.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = BluePrimary.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
            )
            Spacer(modifier = m.height(20.dp))
            TextField(
                value = bankName.value,
                leadingIcon = {Icon(Octicons.Person24, contentDescription ="" )},
                onValueChange = {bankName.value=it},
                placeholder = {Text(text = "Account Number")},
                singleLine = true,
                modifier = m.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = BluePrimary.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
            )
            Spacer(modifier = m.height(20.dp))
            TextField(
                value = bankName.value,
                leadingIcon = {Icon(Octicons.Person24, contentDescription ="" )},
                onValueChange = {bankName.value=it},
                placeholder = {Text(text = "Account name")},
                singleLine = true,
                modifier = m.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = BluePrimary.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
            )
        }
    }
}


@Preview
@Composable
private fun PriviewBottomSheetCancelOrder(){
    BottomSheetCancelOrder()
}