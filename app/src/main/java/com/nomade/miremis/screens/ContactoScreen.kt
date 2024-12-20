package com.nomade.miremis.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nomade.miremis.R
import com.nomade.miremis.utils.TopBar2

@Composable
fun ContactDetails(navController: NavHostController) {
    Scaffold(
        topBar = { TopBar2(navController = navController, title = "Contacto") },

        content = { padding ->
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.main_back))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(70.dp))
                    Text(
                        text = "Datos de Contacto",
                        style = MaterialTheme.typography.h4,
                        color = colorResource(id = R.color.main_btn),
                        modifier = Modifier.padding(22.dp)
                    )

                    Text(
                        text = "Reservas: 02901 422222",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 18.dp)
                    )
                    Text(
                        text = "Aeropuerto: 02901 436170",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 18.dp)
                    )
                    Text(
                        text = "Administración: 02901 434100",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        })
}

@Preview(showBackground = true)
@Composable
fun ContactDetailsPreview() {
    //ContactDetails()
}