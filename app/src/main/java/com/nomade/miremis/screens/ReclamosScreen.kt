package com.nomade.miremis.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.nomade.miremis.MainViewModel
import com.nomade.miremis.R
import com.nomade.miremis.utils.Constants.USER_EMAIL_KEY
import com.nomade.miremis.utils.Constants.USER_LASTNAME_KEY
import com.nomade.miremis.utils.Constants.USER_NAME_KEY
import com.nomade.miremis.utils.Constants.USER_PHONE_KEY
import com.nomade.miremis.utils.SharedPrefsUtil
import com.nomade.miremis.utils.TopBar2
import com.nomade.miremis.utils.validate
import kotlinx.coroutines.launch

@Composable
fun ReclamosScreen(navController: NavHostController) {

    val mainViewModel: MainViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    var textMensajeUser by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var enable by remember {
        mutableStateOf(true)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    mainViewModel.getResultReclamo().observe(LocalLifecycleOwner.current) { result ->
        run {
            if (result.toString().lowercase().contains("ok")) {
                textMensajeUser = ""
                scope.launch { snackbarHostState.showSnackbar(message = "Mensaje enviado") }
            } else {
                val msg = result.toString().substring(result.toString().indexOf(">")  +1)
                scope.launch { snackbarHostState.showSnackbar(message = msg) }
            }
        }
    }

    if (SharedPrefsUtil.get(USER_EMAIL_KEY, "") != "" &&
        SharedPrefsUtil.get(USER_PHONE_KEY, "") != "" &&
        SharedPrefsUtil.get(USER_NAME_KEY, "") != "" &&
        SharedPrefsUtil.get(USER_LASTNAME_KEY, "") != ""
    ) {
        enable = true
    } else {
        enable = false
    }

    Scaffold(
        topBar = { TopBar2(navController = navController, title = "Reclamos") },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                if (enable) {
                Text(
                    modifier = Modifier
                        .padding(10.dp),
                    text = "Describa brevemente su reclamo",
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.main_btn)
                )
                Spacer(modifier = Modifier.height(15.dp))


                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {

                        TextField(
                            value = textMensajeUser,
                            onValueChange = {
                                textMensajeUser = it
                                errorMessage = validate(it)
                            },
                            placeholder = { Text("Su Mensaje...") },
                            textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.DarkGray,
                                disabledTextColor = Color.Gray,
                                backgroundColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(0.8f)
                                .height(100.dp)
                        )
                        errorMessage?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        OutlinedButton(

                            onClick = {
                                keyboardController?.hide()
                                var nombre = SharedPrefsUtil.get(USER_NAME_KEY, "")
                                var apellido = SharedPrefsUtil.get(USER_LASTNAME_KEY, "")
                                var pasajero = "${nombre} ${apellido}"
                                if(textMensajeUser != "") {
                                    mainViewModel.enviarReclamo(
                                        SharedPrefsUtil.get(USER_EMAIL_KEY, ""),
                                        SharedPrefsUtil.get(USER_PHONE_KEY, ""),
                                        textMensajeUser,
                                        pasajero
                                    )
                                }
                            },
                            Modifier
                                .weight(0.2f)
                                .height(58.dp),
                            shape = RoundedCornerShape(20)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_arrow),
                                contentDescription = "",
                                Modifier.fillMaxSize()
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Por favor, complete antes sus datos personales.",
                        color = Color.Red,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    )


}





