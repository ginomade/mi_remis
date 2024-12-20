package com.nomade.miremis.utils

import android.location.Location
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.nomade.miremis.R
import com.nomade.miremis.net.Auto
import com.nomade.miremis.net.MensajeObject
import com.nomade.miremis.net.ViajeObject
import com.nomade.miremis.utils.Constants.BAJADA_KEY
import com.nomade.miremis.utils.Constants.DISTANCIA_DESTINO_KEY
import com.nomade.miremis.utils.Constants.FICHA_KEY
import com.nomade.miremis.utils.Constants.USER_DIALOG_KEY
import java.util.regex.Pattern

@Composable
fun CardWithIconAndText(
    icon: ImageVector,
    contentDescription: String?,
    text: String,
    bold: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier // No se aplica Modifier.clickable
                }
            ),
        elevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.Blue,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                fontSize = 20.sp,
                fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
                style = MaterialTheme.typography.body1,
                color = if (bold) Color.Green else colorResource(id = R.color.main_btn)
            )
        }
    }
}

@Composable
fun CardWithMovilDetails(auto: Auto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.movil1),
                    contentDescription = "",
                    Modifier.size(85.dp)
                )
                Row {
                    Text(
                        text = "Movil: ",
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.main_btn)
                    )
                    Text(
                        text = auto.movil.toString(),
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.body1,
                        color = colorResource(id = R.color.black)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row {
                    Text(
                        text = "Marca: ",
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.main_btn)
                    )
                    Text(
                        text = auto.marca.toString(),
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.body1,
                        color = colorResource(id = R.color.black)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Row {
                    Text(
                        text = "Modelo: ",
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.main_btn)
                    )
                    Text(
                        text = auto.modelo.toString(),
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.body1,
                        color = colorResource(id = R.color.black)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Row {
                    Text(
                        text = "Color: ",
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.main_btn)
                    )
                    Text(
                        text = auto.color.toString(),
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.body1,
                        color = colorResource(id = R.color.black)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Row {
                    Text(
                        text = "Dominio: ",
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.main_btn)
                    )
                    Text(
                        text = auto.dominio.toString(),
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.body1,
                        color = colorResource(id = R.color.black)
                    )
                }
            }
        }
    }
}

@Composable
fun CardWithIconAndTextError(icon: ImageVector, contentDescription: String?, text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.Red,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1,
                color = Color.Red
            )
        }
    }
}

@Composable
fun MessageItem(message: String, autor: String, time: String, messageIn: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .padding(8.dp),
        elevation = 4.dp,
        backgroundColor = if (messageIn) colorResource(id = R.color.white)
        else colorResource(id = R.color.teal_700_b)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            /*Text(
                text = autor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(2.dp))*/
            Text(
                text = message,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = time,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(2.dp),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun MensajesList(mensajes: List<MensajeObject>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        items(mensajes) { mensaje ->
            MessageItem(mensaje.textomsj!!, "", mensaje.horamsj!!, false)
        }
    }
}

@Composable
fun ViajeItem(fecha: String, hora: String, movil: Int, direccion: String, obs: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .padding(4.dp),
        elevation = 4.dp,
        backgroundColor = colorResource(id = R.color.white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.DarkGray)
            )
            Row {
                Text(
                    text = fecha,
                    modifier = Modifier
                        .padding(1.dp),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = hora,
                    modifier = Modifier
                        .padding(1.dp),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(2.dp))
                Row {
                    Text(
                        text = "Movil:",
                        modifier = Modifier
                            .padding(1.dp),
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = movil.toString(),
                        modifier = Modifier
                            .padding(1.dp),
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(1.dp))
            Row {
                Text(
                    text = "Dirección:",
                    modifier = Modifier
                        .padding(1.dp),
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = direccion,
                    modifier = Modifier
                        .padding(1.dp),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(1.dp))
            Row {
                Text(
                    text = "Observaciones:",
                    modifier = Modifier
                        .padding(1.dp),
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = obs,
                    modifier = Modifier
                        .padding(1.dp),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun ViajesList(viajes: List<ViajeObject>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        items(viajes) { viaje ->
            ViajeItem(
                viaje.fecha ?: "",
                viaje.hora ?: "",
                viaje.idMovil ?: 0,
                viaje.direccion ?: "",
                viaje.observaciones ?: ""
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewerPreview() {
    val autoAsignado = Auto("52", "Corolla", "2021", "blanco", "AA000AA")
    //CardWithMovilDetails(autoAsignado)
    //DisclosureAlertDialog()
    //textText()
    Column {
        CardWithIconAndText(Icons.Default.Pending, "", "mensajes", true)
        MessageItem("mensaje entrante", "movil", "12:22:00", false)
        ViajeItem("2024-09-04", "08:35:09", 215, "San Martin 929", "Yowen 1906")
        PrecioTextMensaje("50.00")
    }
}

@Composable
fun IconWithTextRow(
    icon: ImageVector,
    contentDescription: String?,
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(50.dp)
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = colorResource(id = R.color.main_btn)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            fontSize = 20.sp,
            style = MaterialTheme.typography.body1,
            color = colorResource(id = R.color.main_btn)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                    contentDescription = "",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.body1,
                    color = if(isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(id = R.color.main_btn),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar2(modifier: Modifier = Modifier, navController: NavHostController, title: String) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.body1,
                    color = colorResource(id = R.color.main_btn),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        modifier = modifier,
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {

                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

            } else {
                null
            }
        }
    )
}

@Composable
fun TextMensaje(texto: String) {
    Row() {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            text = texto,
            fontSize = 20.sp,
            style = MaterialTheme.typography.body1,
            color = colorResource(id = R.color.main_btn)
        )
    }
}

@Composable
fun PrecioTextMensaje(precio: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier
                .padding(5.dp),
            text = "Valor aproximado",
            fontSize = 14.sp,
            style = MaterialTheme.typography.body1,
            color = colorResource(id = R.color.main_btn)
        )
        Spacer(modifier = Modifier.width(8.dp))
        if (precio.lowercase().contains("error")) {
            Text(
                modifier = Modifier
                    .padding(1.dp),
                text = precio,
                fontSize = 22.sp,
                style = MaterialTheme.typography.body1,
                color = Color.Red
            )
        } else {
            Text(
                modifier = Modifier
                    .padding(1.dp),
                text = "$",
                fontSize = 22.sp,
                style = MaterialTheme.typography.body1,
                color = colorResource(id = R.color.main_btn)
            )
            Spacer(modifier = Modifier.width(1.dp))
            Text(
                modifier = Modifier
                    .padding(1.dp),
                text = precio,
                fontSize = 22.sp,
                style = MaterialTheme.typography.body1,
                color = colorResource(id = R.color.main_btn)
            )
        }

    }
}

@Composable
fun ErrorTextMensaje(texto: String) {
    Row() {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            text = texto,
            fontSize = 20.sp,
            style = MaterialTheme.typography.body1,
            color = Color.Red
        )
    }
}


@Composable
fun textText() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            text = "¿A dónde vamos?",
            fontSize = 24.sp,
            style = MaterialTheme.typography.button,
            color = colorResource(id = R.color.main_btn)
        )
    }
}

fun calculoTiempoDeLlegada(distancia: Int): Int {
    val tiempoPorKm = 5
    return ((distancia * tiempoPorKm) / 1000).toInt()
}

fun calculoPrecioAprox(): String {
    var res = 0f
    val bajadaFloat = SharedPrefsUtil.get(BAJADA_KEY, "").toFloatOrNull() ?: 0f
    val fichaFloat = SharedPrefsUtil.get(FICHA_KEY, "").toFloatOrNull() ?: 0f
    val distanciaFloat = SharedPrefsUtil.get(DISTANCIA_DESTINO_KEY, 0).toFloat()
    if (distanciaFloat.toInt() != 0) {
        res = bajadaFloat + (fichaFloat * distanciaFloat / 100)
    }

    return "%.0f".format(res)
}

fun stringToLatLng(geo1: String): LatLng {
    val ret = LatLng(geo1.split(",")[0].toDouble(), geo1.split(",")[1].toDouble())
    return ret
}

fun validGeo(geo: String): Boolean {
    return geo.isNotEmpty() && geo != "0.0, 0.0" && geo != "0.0" && geo != "0,0" && geo.length > 10
}

fun distanceBetween(latLng1: LatLng, latLng2: LatLng): Float {
    val location1 = Location("").apply {
        latitude = latLng1.latitude
        longitude = latLng1.longitude
    }

    val location2 = Location("").apply {
        latitude = latLng2.latitude
        longitude = latLng2.longitude
    }

    return location1.distanceTo(location2)
}

@Composable
fun DisclosureAlertDialog() {
    // Estado para controlar la visibilidad del diálogo
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false // Acción al descartar el diálogo
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false // Acción al presionar el botón de confirmar
                        SharedPrefsUtil.set(USER_DIALOG_KEY, true)
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false // Acción al presionar el botón de cancelar
                    }
                ) {
                    Text("Cancelar")
                }
            },
            text = {
                Text(stringResource(id = R.string.mensaje_disclosure))
            }
        )
    }
}

@Composable
fun CancelPedidoDialog(onClick: () -> Unit, onNoClick: () -> Unit) {
    // Estado para controlar la visibilidad del diálogo
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false // Acción al descartar el diálogo
            },
            confirmButton = {
                TextButton(
                    onClick = onClick/*{
                        onClick
                        //showDialog = false // Acción al presionar el botón de confirmar
                    }*/
                ) {
                    Text("Si")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onNoClick
                ) {
                    Text("No")
                }
            },
            text = {
                Text(stringResource(id = R.string.mensaje_cancel_pedido))
            }
        )
    }
}

@Composable
fun HorizontalLoadingIndicator() {
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth() // Ocupa todo el ancho de la pantalla
            .padding(16.dp) // Margen opcional alrededor de la barra de progreso
    )
}

@Composable
fun PlayNotificationSound() {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.chime_notification_marimba) }

    // Reproduce el audio
    mediaPlayer.start()

    // Libera los recursos cuando termine de reproducir
    mediaPlayer.setOnCompletionListener {
        it.release()
    }
}

val urlPattern = "^(http|https)://.*$"
val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
val maliciousPattern = ".*(\\<|\\>|script|\\{|\\}).*"
fun validate(input: String): String? {
    return when {
        Pattern.matches(urlPattern, input) -> "No se permiten URLs."
        Pattern.matches(emailPattern, input) -> "No se permiten correos electrónicos."
        Pattern.matches(maliciousPattern, input) -> "Texto malicioso detectado."
        else -> null
    }
}
