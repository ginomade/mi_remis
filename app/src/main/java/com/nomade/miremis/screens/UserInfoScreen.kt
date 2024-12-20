package com.nomade.miremis.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nomade.miremis.Estado
import com.nomade.miremis.HelperViewModel
import com.nomade.miremis.R
import com.nomade.miremis.utils.Constants.TARJETA_KEY
import com.nomade.miremis.utils.Constants.USER_ADD_ALT_KEY
import com.nomade.miremis.utils.Constants.USER_ADD_HOME_KEY
import com.nomade.miremis.utils.Constants.USER_ADD_WORK_KEY
import com.nomade.miremis.utils.Constants.USER_EMAIL_KEY
import com.nomade.miremis.utils.Constants.USER_LASTNAME_KEY
import com.nomade.miremis.utils.Constants.USER_NAME_KEY
import com.nomade.miremis.utils.Constants.USER_PHONE_KEY
import com.nomade.miremis.utils.SharedPrefsUtil
import com.nomade.miremis.utils.TopBar2
import kotlinx.coroutines.launch

@Composable
fun UserInfoScreen(navController: NavHostController, viewModel: HelperViewModel) {

    var savedName = SharedPrefsUtil.get(USER_NAME_KEY, "")
    var savedLastName = SharedPrefsUtil.get(USER_LASTNAME_KEY, "")
    var savedEmail = SharedPrefsUtil.get(USER_EMAIL_KEY, "")
    var savedPhone = SharedPrefsUtil.get(USER_PHONE_KEY, "")
    var savedAddress1 = SharedPrefsUtil.get(USER_ADD_HOME_KEY, "")
    var savedAddress2 = SharedPrefsUtil.get(USER_ADD_WORK_KEY, "")
    var savedAddress3 = SharedPrefsUtil.get(USER_ADD_ALT_KEY, "")

    var name by remember { mutableStateOf(savedName) }
    var lastName by remember { mutableStateOf(savedLastName) }
    var email by remember { mutableStateOf(savedEmail) }
    var phone by remember { mutableStateOf(savedPhone) }
    var address1 by remember { mutableStateOf(savedAddress1) }
    var address2 by remember { mutableStateOf(savedAddress2) }
    var address3 by remember { mutableStateOf(savedAddress3) }
    val checkedState = remember { mutableStateOf(SharedPrefsUtil.get(TARJETA_KEY, false)) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBar2(navController = navController, title = "Mis Datos") },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.size(5.dp))
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre *") },
                    textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkGray,
                        disabledTextColor = Color.Gray,
                        backgroundColor = if (name.isNotEmpty()) colorResource(id = R.color.clear_green) else Color.LightGray
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Apellido") },
                    textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkGray,
                        disabledTextColor = Color.Gray,
                        backgroundColor = if (lastName.isNotEmpty()) colorResource(id = R.color.clear_green) else Color.LightGray
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )

                TextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono *") },
                    textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkGray,
                        disabledTextColor = Color.Gray,
                        backgroundColor = if (phone.isNotEmpty()) colorResource(id = R.color.clear_green) else Color.LightGray
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                TextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email *") },
                    textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkGray,
                        disabledTextColor = Color.Gray,
                        backgroundColor = if (email.isNotEmpty()) colorResource(id = R.color.clear_green) else Color.LightGray
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                TextField(
                    value = address1,
                    onValueChange = { address1 = it },
                    label = { Text("Dirección Casa") },
                    textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkGray,
                        disabledTextColor = Color.Gray,
                        backgroundColor = if (address1.isNotEmpty()) colorResource(id = R.color.clear_green) else Color.LightGray
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                TextField(
                    value = address2,
                    onValueChange = { address2 = it },
                    label = { Text("Dirección Trabajo") },
                    textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkGray,
                        disabledTextColor = Color.Gray,
                        backgroundColor = if (address2.isNotEmpty()) colorResource(id = R.color.clear_green) else Color.LightGray
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                TextField(
                    value = address3,
                    onValueChange = { address3 = it },
                    label = { Text("Dirección Alternativa") },
                    textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkGray,
                        disabledTextColor = Color.Gray,
                        backgroundColor = if (address3.isNotEmpty()) colorResource(id = R.color.clear_green) else Color.LightGray
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkedState.value,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.Green, // Color cuando está marcado
                            uncheckedColor = Color.Red, // Color cuando no está marcado
                            checkmarkColor = Color.White // Color de la marca de verificación
                        ),
                        onCheckedChange = {
                            checkedState.value = it
                            SharedPrefsUtil.set(TARJETA_KEY, it)
                        } // Cambiamos el estado cuando se hace clic
                    )
                    Text(
                        text = "Pago con tarjeta",
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.main_btn)
                    )
                }
                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    text = "* Campos requeridos",
                    modifier = Modifier.padding(8.dp),
                    color = Color.Red,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.size(5.dp))
                Button(
                    onClick = {

                        SharedPrefsUtil.set(USER_NAME_KEY, name)
                        SharedPrefsUtil.set(USER_LASTNAME_KEY, lastName)
                        SharedPrefsUtil.set(USER_PHONE_KEY, phone)
                        SharedPrefsUtil.set(USER_EMAIL_KEY, email)
                        SharedPrefsUtil.set(USER_ADD_HOME_KEY, address1)
                        SharedPrefsUtil.set(USER_ADD_WORK_KEY, address2)
                        SharedPrefsUtil.set(USER_ADD_ALT_KEY, address3)
                        scope.launch { snackbarHostState.showSnackbar(message = "Datos guardados.") }
                        viewModel.setEstadoViaje(Estado.INICIO)
                        navController.navigateUp()
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .height(50.dp),
                    enabled = name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty()
                ) {
                    Text("Guardar Cambios")
                }
            }

        }
    )


}

