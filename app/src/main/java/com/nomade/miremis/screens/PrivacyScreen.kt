package com.nomade.miremis.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.nomade.miremis.R
import com.nomade.miremis.utils.Constants.PRIVACY_KEY
import com.nomade.miremis.utils.SharedPrefsUtil
import com.nomade.miremis.utils.TopBar2

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PrivacyScreen(url: String, navController: NavHostController) {

    val checkedState = remember { mutableStateOf(SharedPrefsUtil.get(PRIVACY_KEY, false)) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBar2(navController = navController, title = "Políticas de Privacidad") },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(70.dp))
                AndroidView(factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        loadUrl(url)
                    }
                },
                    modifier = Modifier.weight(0.8f))
                Spacer(modifier = Modifier.size(10.dp))
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
                            SharedPrefsUtil.set(PRIVACY_KEY, it)
                        } // Cambiamos el estado cuando se hace clic
                    )
                    Text(
                        text = "He leído y acepto estas condiciones.",
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.main_btn)
                    )
                }
                Button(
                    onClick = {

                        //scope.launch { snackbarHostState.showSnackbar(message = "Datos guardados.") }

                        navController.navigateUp()
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .height(40.dp)
                        .width(100.dp),
                    enabled = checkedState.value
                ) {
                    Text("Aceptar")
                }
                Spacer(modifier = Modifier.size(20.dp))
            }
        }
    )
}

