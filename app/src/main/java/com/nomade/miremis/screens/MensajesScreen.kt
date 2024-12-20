package com.nomade.miremis.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.nomade.miremis.MainViewModel
import com.nomade.miremis.R
import com.nomade.miremis.net.MensajeObject
import com.nomade.miremis.utils.Constants.RESERVA_KEY
import com.nomade.miremis.utils.Constants.USER_EMAIL_KEY
import com.nomade.miremis.utils.Constants.USER_GEOPOS_KEY
import com.nomade.miremis.utils.MensajesList
import com.nomade.miremis.utils.SharedPrefsUtil
import com.nomade.miremis.utils.TopBar2
import com.nomade.miremis.utils.validate
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@Composable
fun MensajesScreen(navController: NavHostController) {

    val mainViewModel: MainViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    var mensajes by rememberSaveable { mutableStateOf(listOf<MensajeObject>()) }
    var messageCount by remember { mutableStateOf(0) }
    var textMensajeUser by rememberSaveable { mutableStateOf("") }
    var hayMensajes by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val animationDuration = 1000
    var errorMessage by remember { mutableStateOf<String?>(null) }

    mainViewModel.leerHistoricoMensajes(
        SharedPrefsUtil.get(USER_EMAIL_KEY, ""),
        SharedPrefsUtil.get(RESERVA_KEY, "")
    )

    mainViewModel.getResultNuevoMensaje().observe(LocalLifecycleOwner.current) { result ->
        run {
            if (result.mensaje.toString().lowercase().contains("error")) {
                textMensajeUser = "Error al enviar mensaje"
            } else {
                textMensajeUser = ""
                scope.launch { snackbarHostState.showSnackbar(message = "Mensaje enviado") }
                mainViewModel.leerHistoricoMensajes(
                    SharedPrefsUtil.get(USER_EMAIL_KEY, ""),
                    SharedPrefsUtil.get(RESERVA_KEY, "")
                )
            }
        }
    }

    mainViewModel.getMensajes().observe(LocalLifecycleOwner.current) { data ->
        run {
            if (data.isNotEmpty()) {
                mensajes = data

                mainViewModel.leerMensajes(
                    SharedPrefsUtil.get(USER_EMAIL_KEY, ""),
                    SharedPrefsUtil.get(RESERVA_KEY, "")
                )

                if (messageCount < data.size) {
                    //flgEnableSoundNotification = true
                }
                messageCount = data.size
            }
        }
    }

    mainViewModel.getResultHayMensaje().observe(LocalLifecycleOwner.current) { result ->
        run {
            if (result.lowercase().contains("no")) {
                hayMensajes = false
            } else {
                hayMensajes = true
                mainViewModel.leerMensajes(
                    SharedPrefsUtil.get(USER_EMAIL_KEY, ""),
                    SharedPrefsUtil.get(RESERVA_KEY, "")
                )
                mainViewModel.leerHistoricoMensajes(
                    SharedPrefsUtil.get(USER_EMAIL_KEY, ""),
                    SharedPrefsUtil.get(RESERVA_KEY, "")
                )
            }
        }
    }


    Scaffold(
        topBar = { TopBar2(navController = navController, title = "Mensajes") },
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

                AnimatedVisibility(
                    visible = hayMensajes || mensajes.isNotEmpty(),
                    enter = fadeIn(animationSpec = tween(durationMillis = animationDuration)) + expandIn(
                        expandFrom = Alignment.Center,
                        animationSpec = tween(durationMillis = animationDuration)
                    ),
                    exit = fadeOut(animationSpec = tween(durationMillis = animationDuration)) + shrinkOut(
                        shrinkTowards = Alignment.Center,
                        animationSpec = tween(durationMillis = animationDuration)
                    )
                ) {
                    MensajesList(mensajes)
                }

                AnimatedVisibility(
                    visible = !hayMensajes && mensajes.isEmpty(),
                    enter = fadeIn(animationSpec = tween(durationMillis = animationDuration)) + expandIn(
                        expandFrom = Alignment.Center,
                        animationSpec = tween(durationMillis = animationDuration)
                    ),
                    exit = fadeOut(animationSpec = tween(durationMillis = animationDuration)) + shrinkOut(
                        shrinkTowards = Alignment.Center,
                        animationSpec = tween(durationMillis = animationDuration)
                    )
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = "No Hay Mensajes",
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.main_btn)
                    )
                }

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
                        placeholder = { Text("Enviar Mensaje...") },
                        textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.DarkGray,
                            disabledTextColor = Color.Gray,
                            backgroundColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(0.8f)
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
                            mainViewModel.enviarMensaje(
                                SharedPrefsUtil.get(USER_EMAIL_KEY, ""),
                                SharedPrefsUtil.get(RESERVA_KEY, ""),
                                SharedPrefsUtil.get(USER_GEOPOS_KEY, ""),
                                textMensajeUser
                            )

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
            }
        }
    )


}




