package com.nomade.miremis.screens

import MyMapScreen
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.nomade.miremis.Estado
import com.nomade.miremis.GeocodingViewModel
import com.nomade.miremis.HelperViewModel
import com.nomade.miremis.MainViewModel
import com.nomade.miremis.R
import com.nomade.miremis.net.Auto
import com.nomade.miremis.net.ViajeObject
import com.nomade.miremis.utils.CancelPedidoDialog
import com.nomade.miremis.utils.CardWithIconAndText
import com.nomade.miremis.utils.CardWithMovilDetails
import com.nomade.miremis.utils.Constants.APP_STATE_KEY
import com.nomade.miremis.utils.Constants.BAJADA_KEY
import com.nomade.miremis.utils.Constants.DISTANCIA_DESTINO_KEY
import com.nomade.miremis.utils.Constants.DISTANCIA_MOVIL_KEY
import com.nomade.miremis.utils.Constants.ES_TAXI_KEY
import com.nomade.miremis.utils.Constants.FICHA_KEY
import com.nomade.miremis.utils.Constants.GET_RUTA_ACTIVE_KEY
import com.nomade.miremis.utils.Constants.MOVIL_KEY
import com.nomade.miremis.utils.Constants.OBSERVACIONES_KEY
import com.nomade.miremis.utils.Constants.PEDIDO_ACTIVO_KEY
import com.nomade.miremis.utils.Constants.PRIVACY_KEY
import com.nomade.miremis.utils.Constants.RESERVA_KEY
import com.nomade.miremis.utils.Constants.SMALL_SCREEN_KEY
import com.nomade.miremis.utils.Constants.TARJETA_KEY
import com.nomade.miremis.utils.Constants.USER_ADD_ALT_KEY
import com.nomade.miremis.utils.Constants.USER_ADD_HOME_KEY
import com.nomade.miremis.utils.Constants.USER_ADD_WORK_KEY
import com.nomade.miremis.utils.Constants.USER_DESTINO_KEY
import com.nomade.miremis.utils.Constants.USER_DIALOG_KEY
import com.nomade.miremis.utils.Constants.USER_DNI_KEY
import com.nomade.miremis.utils.Constants.USER_EMAIL_KEY
import com.nomade.miremis.utils.Constants.USER_GEOPOS_KEY
import com.nomade.miremis.utils.Constants.USER_LASTNAME_KEY
import com.nomade.miremis.utils.Constants.USER_NAME_KEY
import com.nomade.miremis.utils.Constants.USER_ORIGEN_DETECTED_KEY
import com.nomade.miremis.utils.Constants.USER_ORIGEN_KEY
import com.nomade.miremis.utils.Constants.USER_PHONE_KEY
import com.nomade.miremis.utils.DisclosureAlertDialog
import com.nomade.miremis.utils.ErrorTextMensaje
import com.nomade.miremis.utils.HorizontalLoadingIndicator
import com.nomade.miremis.utils.IconWithTextRow
import com.nomade.miremis.utils.MainTopBar
import com.nomade.miremis.utils.PlayNotificationSound
import com.nomade.miremis.utils.PrecioTextMensaje
import com.nomade.miremis.utils.RoundedRectanglesRow
import com.nomade.miremis.utils.SharedPrefsUtil
import com.nomade.miremis.utils.TextMensaje
import com.nomade.miremis.utils.ViajesList
import com.nomade.miremis.utils.calculoPrecioAprox
import com.nomade.miremis.utils.calculoTiempoDeLlegada
import com.nomade.miremis.utils.stringToLatLng
import com.nomade.miremis.utils.validGeo

@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    helperViewModel: HelperViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = { MainTopBar() },
        floatingActionButton = {
            Box(

                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),

                ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    FloatingActionButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.padding(top = 50.dp) // Añadir un margen al borde

                    ) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        properties = PopupProperties(focusable = true),
                        modifier = Modifier.offset(x = (-10).dp, y = 10.dp)
                    ) {
                        DropdownMenuItem(onClick = {
                            navController.navigate("userInfo")
                            expanded = false
                        }) {
                            Text(
                                "Mis datos",
                                color = if (isSystemInDarkTheme()) colorResource(id = R.color.black) else colorResource(
                                    id = R.color.main_btn
                                )
                            )
                        }
                        DropdownMenuItem(onClick = {
                            navController.navigate("contacto")
                            expanded = false
                        }) {
                            Text(
                                "Contacto",
                                color = if (isSystemInDarkTheme()) colorResource(id = R.color.black) else colorResource(
                                    id = R.color.main_btn
                                )
                            )
                        }
                        DropdownMenuItem(onClick = {
                            navController.navigate("privacidad")
                            expanded = false
                        }) {
                            Text(
                                "Privacidad",
                                color = if (isSystemInDarkTheme()) colorResource(id = R.color.black) else colorResource(
                                    id = R.color.main_btn
                                )
                            )
                        }
                        DropdownMenuItem(onClick = {
                            navController.navigate("reclamos")
                            expanded = false
                        }) {
                            Text(
                                "Reclamos",
                                color = if (isSystemInDarkTheme()) colorResource(id = R.color.black) else colorResource(
                                    id = R.color.main_btn
                                )
                            )
                        }
                    }
                }
            }
        },
        content = { padding ->

            Box(modifier = Modifier.padding(padding)) {
                MainContent(
                    Modifier
                        .padding(padding),
                    navController,
                    mainViewModel,
                    helperViewModel
                )
            }
        }
    )
}

@Composable
fun MainContent(
    modifier: Modifier,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    viewModel: HelperViewModel
) {
    var textDestino by rememberSaveable { mutableStateOf("") }
    var textOrigen by rememberSaveable { mutableStateOf("") }
    var textObservaciones by rememberSaveable { mutableStateOf("") }
    val addressViewModel: GeocodingViewModel = viewModel()
    var estadoGlobal by rememberSaveable { mutableStateOf(Estado.INICIO) }
    var geoMovil by rememberSaveable { mutableStateOf(listOf<String>()) }
    var assignedMovilLocation by rememberSaveable { mutableStateOf<LatLng?>(null) }
    var message by rememberSaveable { mutableStateOf("") }
    var movilAsignado by rememberSaveable { mutableStateOf(false) }
    var autoAsignado by remember { mutableStateOf(Auto()) }
    var distanciaAutoAsignado by remember { mutableStateOf(0) }
    var messageCount by remember { mutableStateOf(0) }
    var precioRemis by remember { mutableStateOf("") }
    val animationDuration = 1000
    val cancelPedido by viewModel.cancelPedido.collectAsState()
    val cancelPedidoConfirmado by viewModel.cancelPedidoConfirmado.collectAsState()
    val currentAddress by viewModel.currentAddress.collectAsState()
    val loading by mainViewModel.loadingState.collectAsState()
    val updatedPrecio by viewModel.updatePrecio.collectAsState()
    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    var flgEnableSoundNotification by rememberSaveable { mutableStateOf(false) }

    var hayMensajes by rememberSaveable { mutableStateOf(false) }
    var hayViajesHistorico by rememberSaveable { mutableStateOf(false) }
    var viajesHist by remember { mutableStateOf(listOf<ViajeObject>()) }

    var changedOrigen by remember { mutableStateOf(false) }

    var rect1State by remember { mutableStateOf(false) }
    var rect2State by remember { mutableStateOf(false) }
    var rect3State by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val smallScreen = SharedPrefsUtil.get(SMALL_SCREEN_KEY, false)

    if (SharedPrefsUtil.get(PRIVACY_KEY, false)) {
        val disclosure = SharedPrefsUtil.get(USER_DIALOG_KEY, false)
        if (!disclosure) {
            DisclosureAlertDialog()
        }
        ValidarDatosCargados(viewModel)
    } else {
        navController.navigate("privacidad")
    }

    val add = currentAddress ?: ""
    if (add.startsWith("-")) {
        textOrigen = if (changedOrigen) textOrigen else ("")
    } else {
        textOrigen = if (changedOrigen) textOrigen else (currentAddress ?: "")
    }

    viewModel.getEstadoViaje().observe(LocalLifecycleOwner.current) { estado ->
        run {
            estadoGlobal = estado
            SharedPrefsUtil.set(APP_STATE_KEY, estadoGlobal.toString())
            if(estadoGlobal == Estado.SIN_DATOS) {
                navController.navigate("userInfo")
            }
        }
    }

    mainViewModel.getResultTarifas().observe(LocalLifecycleOwner.current) { result ->
        run {
            SharedPrefsUtil.set(BAJADA_KEY, result.bajada)
            SharedPrefsUtil.set(FICHA_KEY, result.ficha)
            //TEST//
            //SharedPrefsUtil.set(ES_TAXI_KEY, result.tipo!!.lowercase().toString() == "taxi")
            SharedPrefsUtil.set(ES_TAXI_KEY, true)
        }
    }

    mainViewModel.getResultTarifaRemis().observe(LocalLifecycleOwner.current) { result ->
        run {
            val ret = result.Numeral ?: 0
            if (ret != 0) {
                precioRemis = ret.toString()
                viewModel.setUpdatePrecio(true)
            } else {
                precioRemis = "Error al obtener el precio."
            }
        }
    }

    mainViewModel.getResultMoviles().observe(LocalLifecycleOwner.current) { moviles ->
        run {
            if (moviles.isNotEmpty()) {
                geoMovil = moviles.map { it.coorLibre ?: "" }
            }
        }
    }

    mainViewModel.getViajes().observe(LocalLifecycleOwner.current) { data ->
        run {
            if (data.isNotEmpty()) {
                hayViajesHistorico = true
                viajesHist = data
            } else {
                hayViajesHistorico = false
            }
        }
    }

    mainViewModel.getPedidoResponse().observe(LocalLifecycleOwner.current) { pedido ->
        run {
            when (pedido.status) {
                "pending" -> {
                    //estado que devuelve server cuando ya hay un pedido en curso
                    viewModel.setEstadoViaje(Estado.PENDIENTE)
                    if (pedido.reserva!!.isNotEmpty()) {
                        SharedPrefsUtil.set(RESERVA_KEY, pedido.reserva)
                    }
                }

                "success" -> {
                    //viewModel.setEstadoViaje(Estado.PENDIENTE)
                    if (pedido.reserva!!.isNotEmpty()) {
                        SharedPrefsUtil.set(RESERVA_KEY, pedido.reserva)
                    }
                }

                "error" -> {
                    message = pedido.message ?: "Ocurrio un error"
                    viewModel.setEstadoViaje(Estado.ERROR)
                }
            }
        }
    }

    mainViewModel.getCancelResponse().observe(LocalLifecycleOwner.current) { response ->
        run {
            if (response) {
                viewModel.setCancelPedidoConfirmado(true)
                mainViewModel.setCompletedCancelResponse()
            }
        }
    }

    mainViewModel.getResultEstadoMovil().observe(LocalLifecycleOwner.current) { estadoMovil ->
        run {
            if (estadoMovil.isNotEmpty()) {
                val geoM = estadoMovil.get(0).Coordenadas ?: ""
                val geoUser = SharedPrefsUtil.get(USER_GEOPOS_KEY, "")

                if (geoM.contains(",") && geoUser.contains(",")) {
                    if (!validGeo(geoM)) {
                        viewModel.setEstadoViaje(Estado.PENDIENTE)
                    } else {
                        distanciaAutoAsignado = SharedPrefsUtil.get(DISTANCIA_MOVIL_KEY, 0)
                    }
                }
                if (validGeo(geoM)) {
                    assignedMovilLocation = stringToLatLng(geoM)
                    viewModel.triggerResetMap()
                } else {
                    assignedMovilLocation = null
                    viewModel.triggerResetMap()
                }
            } else {
                assignedMovilLocation = null
                viewModel.triggerResetMap()
            }
        }
    }

    mainViewModel.getUltimoPedidoResponse().observe(LocalLifecycleOwner.current) { result ->
        run {

            when (result.status) {
                "info" -> {
                    if (SharedPrefsUtil.get(
                            PEDIDO_ACTIVO_KEY,
                            false
                        )
                    ) {

                        message = "Aguarde. Estamos asignando su movil."//result.message!!

                    } else if (estadoGlobal != Estado.CON_ORIGEN) {
                        setInitialState(viewModel, mainViewModel)
                        assignedMovilLocation = null
                    }
                    if (!result.reserva.isNullOrEmpty()) {
                        if (result.reserva!!.isNotEmpty()) {
                            SharedPrefsUtil.set(RESERVA_KEY, result.reserva)
                        }
                    }

                }

                "success" -> {
                    message = result.message!!

                    if (result.idMovil == "0"
                    ) {
                        viewModel.setEstadoViaje(Estado.INICIO)
                        //SharedPrefsUtil.set(PEDIDO_ACTIVO_KEY, false)
                    } else if ((result.idMovil == null || result.idMovil == "") && !result.reserva.isNullOrEmpty()) {
                        message = "Aguarde. Estamos asignando su movil."
                    } else {
                        if (result.auto != null) {
                            autoAsignado = result.auto!!.get(0)
                            viewModel.setEstadoViaje(Estado.ASIGNADO)
                        }
                        SharedPrefsUtil.set(MOVIL_KEY, result.idMovil)
                        if (!movilAsignado) flgEnableSoundNotification = true

                        movilAsignado = true

                        if (!result.reserva.isNullOrEmpty()) {
                            if (result.reserva!!.isNotEmpty()) {
                                SharedPrefsUtil.set(RESERVA_KEY, result.reserva)
                            }
                        }
                        //SharedPrefsUtil.set(PEDIDO_ACTIVO_KEY, false)
                    }

                    SharedPrefsUtil.set(PEDIDO_ACTIVO_KEY, false)
                }

                "error" -> {
                    message = result.message ?: "Ocurrio un error"
                    viewModel.setEstadoViaje(Estado.ERROR)
                }

                "empty" -> {
                    message = result.message ?: "Ocurrio un error"
                    viewModel.setEstadoViaje(Estado.ERROR)
                }

                "reset" -> {
                    /*setInitialState(viewModel, mainViewModel)
                    assignedMovilLocation = null
                    SharedPrefsUtil.set(PEDIDO_ACTIVO_KEY, false)*/
                }
            }

        }
    }

    mainViewModel.getResultHayMensaje().observe(LocalLifecycleOwner.current) { result ->
        run {
            if (result.lowercase().contains("no", true)) {
                hayMensajes = false
                messageCount = 0
            } else {
                hayMensajes = true
                if (messageCount == 0) {
                    flgEnableSoundNotification = true
                    messageCount++
                }
            }
        }
    }

    if (flgEnableSoundNotification) {
        PlayNotificationSound()
        flgEnableSoundNotification = false
    }

    if (cancelPedido) {
        ShowCancelDialog(viewModel = viewModel, mainViewModel = mainViewModel)

    }

    if (cancelPedidoConfirmado) {
        SharedPrefsUtil.set(USER_ORIGEN_KEY, "")
        SharedPrefsUtil.set(PEDIDO_ACTIVO_KEY, false)
        Toast.makeText(context, "Se envíó la cancelación.", Toast.LENGTH_SHORT).show()
        viewModel.setCancelPedidoConfirmado(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.white))
            .padding(3.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Box(modifier = Modifier.weight(if (smallScreen) 0.4f else 0.45f)) {
                MyMapScreen(
                    geoMovil,
                    assignedMovilLocation ?: LatLng(0.0, 0.0),
                    viewModel
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .weight(if (smallScreen) 0.6f else 0.55f)
                    .verticalScroll(rememberScrollState())
                    .safeDrawingPadding()
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ),
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(5.dp))
                rect1State = estadoGlobal == Estado.INICIO
                rect2State = estadoGlobal == Estado.CON_ORIGEN
                rect3State = estadoGlobal == Estado.ASIGNADO
                if (loading) {
                    AnimatedVisibility(
                        visible = estadoGlobal == Estado.INICIO,
                        enter = fadeIn(animationSpec = tween(durationMillis = animationDuration / 2)) + expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration / 2)
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = animationDuration / 2)) + shrinkOut(
                            shrinkTowards = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration / 2)
                        )
                    ) {
                        Spacer(modifier = Modifier.height(5.dp))
                        HorizontalLoadingIndicator()
                    }
                } else {
                    RoundedRectanglesRow(
                        rect1State,
                        rect2State,
                        rect3State
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = 4.dp
                ) {

                    Spacer(modifier = Modifier.height(15.dp))

                    //INICIO
                    AnimatedVisibility(
                        visible = estadoGlobal == Estado.INICIO,
                        enter = fadeIn(animationSpec = tween(durationMillis = animationDuration)) + expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = animationDuration)) + shrinkOut(
                            shrinkTowards = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        )
                    ) {
                        precioRemis = "0"
                        viewModel.setUpdatePrecio(false)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(id = R.color.main_back)),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                text = "Origen",
                                fontSize = 18.sp,
                                style = MaterialTheme.typography.button,
                                color = colorResource(id = R.color.main_btn)
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            ) {


                                TextField(
                                    value = textOrigen,
                                    maxLines = 3,
                                    onValueChange = {
                                        textOrigen = it
                                        changedOrigen = true
                                    },
                                    colors = TextFieldDefaults.textFieldColors(
                                        textColor = Color.DarkGray,
                                        disabledTextColor = Color.Gray,
                                        backgroundColor = Color.White
                                    ),
                                    label = { Text("Tu ubicación") },
                                    textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                                    modifier = Modifier
                                        .weight(0.8f)
                                )
                                OutlinedButton(
                                    onClick = {
                                        Log.d("MainScreen", "origen: ${textOrigen}")
                                        setOrigenPredef(
                                            textOrigen,
                                            viewModel,
                                            addressViewModel,
                                            context,
                                            true
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

                            Spacer(modifier = Modifier.height(5.dp))

                            val home = SharedPrefsUtil.get(USER_ADD_HOME_KEY, "")
                            val work = SharedPrefsUtil.get(USER_ADD_WORK_KEY, "")
                            val alt = SharedPrefsUtil.get(USER_ADD_ALT_KEY, "")

                            if (home != "") {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
                                    elevation = 4.dp
                                ) {
                                    IconWithTextRow(
                                        icon = Icons.Default.Home,
                                        contentDescription = "home",
                                        text = home,
                                        onClick = {
                                            setOrigenPredef(
                                                home,
                                                viewModel,
                                                addressViewModel,
                                                context
                                            )

                                        }
                                    )
                                }
                            }
                            if (work != "") {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
                                    elevation = 4.dp
                                ) {
                                    IconWithTextRow(
                                        icon = Icons.Default.Work,
                                        contentDescription = "work",
                                        text = work,
                                        onClick = {
                                            setOrigenPredef(
                                                work,
                                                viewModel,
                                                addressViewModel,
                                                context
                                            )

                                        }
                                    )
                                }
                            }
                            if (alt != "") {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
                                    elevation = 4.dp
                                ) {
                                    IconWithTextRow(
                                        icon = Icons.Default.PeopleAlt,
                                        contentDescription = "alternativa",
                                        text = alt,
                                        onClick = {
                                            setOrigenPredef(
                                                alt,
                                                viewModel,
                                                addressViewModel,
                                                context
                                            )

                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(15.dp))
                            CardWithIconAndText(
                                icon = Icons.Default.Message,
                                contentDescription = "message",
                                text = if (hayMensajes) "Mensajes (1)" else "Mensajes",
                                hayMensajes,
                                onClick = { navController.navigate("mensajes") }
                            )
                            Spacer(modifier = Modifier.height(15.dp))

                            if (hayViajesHistorico && viajesHist.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(5.dp),
                                        text = "Historial de Viajes",
                                        fontSize = 16.sp,
                                        color = colorResource(id = R.color.main_btn)
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    ViajesList(viajesHist)
                                }
                            }
                        }
                    }
                    //CON_ORIGEN
                    AnimatedVisibility(
                        visible = estadoGlobal == Estado.CON_ORIGEN,
                        enter = fadeIn(animationSpec = tween(durationMillis = animationDuration)) + expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = animationDuration)) + shrinkOut(
                            shrinkTowards = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(id = R.color.main_back)),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))
                            OutlinedButton(
                                onClick = {
                                    SharedPrefsUtil.set(USER_DESTINO_KEY, textDestino)
                                    SharedPrefsUtil.set(OBSERVACIONES_KEY, textObservaciones)
                                    EnviarPedidoViaje(mainViewModel)
                                    viewModel.setEstadoViaje(Estado.PENDIENTE)
                                },
                                Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(5.dp),
                                shape = RoundedCornerShape(20),
                                elevation = ButtonDefaults.elevation(8.dp)
                            ) {
                                Row {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "Pedir viaje",
                                        fontSize = 24.sp,
                                        color = colorResource(id = R.color.main_btn)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                text = "¿A dónde vamos?",
                                fontSize = 24.sp,
                                style = MaterialTheme.typography.button,
                                color = colorResource(id = R.color.main_btn)
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            ) {

                                TextField(
                                    value = textDestino,
                                    onValueChange = {
                                        textDestino = it
                                    },
                                    placeholder = { Text("Buscar Destino") },
                                    textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        textColor = Color.DarkGray,
                                        disabledTextColor = Color.Gray,
                                        backgroundColor = Color.White
                                    ),
                                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        imeAction = ImeAction.Done,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    modifier = Modifier
                                        .weight(0.8f)
                                )
                                OutlinedButton(
                                    onClick = {
                                        keyboardController?.hide()
                                        SetDestino(
                                            viewModel,
                                            addressViewModel,
                                            mainViewModel,
                                            context,
                                            textDestino
                                        )
                                    },
                                    Modifier
                                        .weight(0.2f)
                                        .height(58.dp),
                                    shape = RoundedCornerShape(20)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.icons8_search_48),
                                        contentDescription = "",
                                        Modifier.fillMaxSize()
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            TextField(
                                value = textObservaciones,
                                onValueChange = {
                                    textObservaciones = it
                                },
                                label = { Text("Observaciones") },
                                textStyle = TextStyle.Default.copy(fontSize = 22.sp),
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.DarkGray,
                                    disabledTextColor = Color.Gray,
                                    backgroundColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            )
                            if (updatedPrecio) {
                                Spacer(modifier = Modifier.height(5.dp))
                                if (SharedPrefsUtil.get(ES_TAXI_KEY, true)) {
                                    PrecioTextMensaje(precio = calculoPrecioAprox())
                                } else {
                                    PrecioTextMensaje(precio = precioRemis)
                                }
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            //TextMensaje("Presione abajo para pedir un móvil")

                            Spacer(modifier = Modifier.height(15.dp))
                            OutlinedButton(
                                onClick = {
                                    setInitialState(viewModel, mainViewModel)
                                    movilAsignado = false
                                    textDestino = ""
                                },
                                Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(5.dp),
                                shape = RoundedCornerShape(20),
                                elevation = ButtonDefaults.elevation(8.dp)
                            ) {
                                Row {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "Volver",
                                        fontSize = 24.sp,
                                        color = colorResource(id = R.color.main_btn)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                    }

                    //Sin Datos
                    AnimatedVisibility(
                        visible = estadoGlobal == Estado.SIN_DATOS,
                        enter = fadeIn(animationSpec = tween(durationMillis = animationDuration)) + expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = animationDuration)) + shrinkOut(
                            shrinkTowards = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(id = R.color.main_back)),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))
                            CardWithIconAndText(
                                icon = Icons.Default.AddTask,
                                contentDescription = "AddTask",
                                text = "Por favor, para iniciar ingrese sus datos personales."
                            )

                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }

                    //Pendiente
                    AnimatedVisibility(
                        visible = estadoGlobal == Estado.PENDIENTE,
                        enter = fadeIn(animationSpec = tween(durationMillis = animationDuration)) + expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = animationDuration)) + shrinkOut(
                            shrinkTowards = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(id = R.color.main_back)),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))
                            CardWithIconAndText(
                                icon = Icons.Default.Pending,
                                contentDescription = "Star Icon",
                                text = message
                            )
                            OutlinedButton(
                                onClick = {
                                    viewModel.setCancelPedido(true)
                                    setInitialState(viewModel, mainViewModel)
                                    textDestino = ""
                                    movilAsignado = false
                                },
                                Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(5.dp),
                                shape = RoundedCornerShape(20),
                                elevation = ButtonDefaults.elevation(8.dp)
                            ) {
                                Row {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "Cancelar",
                                        fontSize = 24.sp,
                                        color = colorResource(id = R.color.main_btn)
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }

                    //ASIGNADO
                    AnimatedVisibility(
                        visible = estadoGlobal == Estado.ASIGNADO,
                        enter = fadeIn(animationSpec = tween(durationMillis = animationDuration)) + expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = animationDuration)) + shrinkOut(
                            shrinkTowards = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(id = R.color.main_back)),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))
                            CardWithIconAndText(
                                icon = Icons.Filled.Notifications,
                                contentDescription = "Asignado",
                                text = "Asignado"
                            )
                            if (distanciaAutoAsignado > 0) {
                                val llegada = calculoTiempoDeLlegada(distanciaAutoAsignado)
                                Spacer(modifier = Modifier.height(15.dp))
                                CardWithIconAndText(
                                    icon = Icons.Filled.Notifications,
                                    contentDescription = "Star Icon",
                                    text = "Su movil llega en ${llegada} minutos"
                                )
                            }
                            /*Spacer(modifier = Modifier.height(15.dp))
                            if(validLocation(assignedMovilLocation)){
                                Text(
                                    text = "Coord de movil OK",
                                    fontSize = 24.sp,
                                    color = Color.Red
                                )
                            } else {
                                Text(
                                    text = "SIN Coord de movil",
                                    fontSize = 24.sp,
                                    color = Color.Red
                                )
                            }*/

                            Spacer(modifier = Modifier.height(15.dp))
                            CardWithIconAndText(
                                icon = Icons.Default.Message,
                                contentDescription = "message",
                                text = if (hayMensajes) "Mensajes (1)" else "Mensajes",
                                hayMensajes,
                                onClick = { navController.navigate("mensajes") }
                            )

                            Spacer(modifier = Modifier.height(15.dp))
                            CardWithMovilDetails(auto = autoAsignado)
                            OutlinedButton(
                                onClick = {
                                    viewModel.setCancelPedido(true)
                                    movilAsignado = false
                                },
                                Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(5.dp),
                                shape = RoundedCornerShape(20),
                                elevation = ButtonDefaults.elevation(8.dp)
                            ) {
                                Row {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "Cancelar",
                                        fontSize = 24.sp,
                                        color = colorResource(id = R.color.main_btn)
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }

                    //EN_CURSO
                    AnimatedVisibility(
                        visible = estadoGlobal == Estado.EN_CURSO,
                        enter = fadeIn(animationSpec = tween(durationMillis = animationDuration)) + expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = animationDuration)) + shrinkOut(
                            shrinkTowards = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(id = R.color.main_back)),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))
                            TextMensaje("Viaje en curso")
                            OutlinedButton(
                                onClick = {
                                    setInitialState(viewModel, mainViewModel)
                                    movilAsignado = false
                                },
                                Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(5.dp),
                                shape = RoundedCornerShape(20),
                                elevation = ButtonDefaults.elevation(8.dp)
                            ) {
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "Cancelar",
                                        fontSize = 24.sp,
                                        color = colorResource(id = R.color.main_btn)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }

                    //ERROR
                    AnimatedVisibility(
                        visible = estadoGlobal == Estado.ERROR,
                        enter = fadeIn(animationSpec = tween(durationMillis = animationDuration)) + expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = animationDuration)) + shrinkOut(
                            shrinkTowards = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(id = R.color.main_back)),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))

                            ErrorTextMensaje(message)
                            Spacer(modifier = Modifier.height(15.dp))
                            OutlinedButton(
                                onClick = {
                                    setInitialState(viewModel, mainViewModel)
                                    movilAsignado = false
                                },
                                Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(5.dp),
                                shape = RoundedCornerShape(20),
                                elevation = ButtonDefaults.elevation(8.dp)
                            ) {
                                Row {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "Volver",
                                        fontSize = 24.sp,
                                        color = colorResource(id = R.color.main_btn)
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }

                    //FINALIZADO
                    AnimatedVisibility(
                        visible = estadoGlobal == Estado.FINALIZADO,
                        enter = fadeIn(animationSpec = tween(durationMillis = animationDuration)) + expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = animationDuration)) + shrinkOut(
                            shrinkTowards = Alignment.Center,
                            animationSpec = tween(durationMillis = animationDuration)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(id = R.color.main_back)),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))
                            TextMensaje("Viaje finalizado")
                            OutlinedButton(
                                onClick = {
                                    viewModel.setEstadoViaje(Estado.INICIO)
                                },
                                Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(5.dp),
                                shape = RoundedCornerShape(20),
                                elevation = ButtonDefaults.elevation(8.dp)
                            ) {
                                Row {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "Nuevo viaje",
                                        fontSize = 24.sp,
                                        color = colorResource(id = R.color.main_btn)
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }// end main content
        }
    }

}

private fun SetDestino(
    viewModel: HelperViewModel,
    addressViewModel: GeocodingViewModel,
    mainViewmodel: MainViewModel,
    context: Context,
    text: String
) {
    if (text != "") {
        viewModel.setEstadoViaje(Estado.CON_ORIGEN)
        SharedPrefsUtil.set(USER_DESTINO_KEY, text)
        if (SharedPrefsUtil.get(ES_TAXI_KEY, true)) {

        } else {
            mainViewmodel.buscarTarifaRemis(SharedPrefsUtil.get(USER_ORIGEN_KEY, ""), text)
        }
        addressViewModel.geocodeFromAddress(
            context,
            text
        ) { geocodingResult ->
            if (geocodingResult != null) {
                viewModel.setDestinoGeopos(
                    LatLng(
                        geocodingResult.lat!!.toDouble(),
                        geocodingResult.lon!!.toDouble()
                    )
                )
            }
        }
    }
}

private fun setOrigenPredef(
    text: String,
    viewModel: HelperViewModel,
    addressViewModel: GeocodingViewModel,
    context: Context,
    default: Boolean = false
) {
    SharedPrefsUtil.set(USER_ORIGEN_KEY, text)
    SharedPrefsUtil.set(RESERVA_KEY, "")
    viewModel.setEstadoViaje(Estado.CON_ORIGEN)
    if (!default) {
        if (text.isNotEmpty()) {
            addressViewModel.geocodeFromAddress(
                context,
                text
            ) { geocodingResult ->
                if (geocodingResult != null) {
                    viewModel.setOrigenGeopos(
                        LatLng(
                            geocodingResult.lat!!.toDouble(),
                            geocodingResult.lon!!.toDouble()
                        )
                    )
                }
            }
        }
    } else {
        val geoUser = SharedPrefsUtil.get(USER_GEOPOS_KEY, "")
        if (geoUser != "") {
            viewModel.setOrigenGeopos(
                LatLng(
                    geoUser.split(",")[0].toDouble(),
                    geoUser.split(",")[1].toDouble()
                )
            )
        }
    }
}

fun EnviarPedidoViaje(viewModel: MainViewModel) {
    var savedEmail = SharedPrefsUtil.get(USER_EMAIL_KEY, "")
    var celu = SharedPrefsUtil.get(USER_PHONE_KEY, "")
    var userGeo = SharedPrefsUtil.get(USER_GEOPOS_KEY, "")
    var destino = SharedPrefsUtil.get(USER_DESTINO_KEY, "")
    var origen = SharedPrefsUtil.get(USER_ORIGEN_KEY, "")
    var observaciones = SharedPrefsUtil.get(OBSERVACIONES_KEY, "")
    var nombre = SharedPrefsUtil.get(USER_NAME_KEY, "")
    var apellido = SharedPrefsUtil.get(USER_LASTNAME_KEY, "")
    var pasajero = "${nombre} ${apellido}"
    var dni = SharedPrefsUtil.get(USER_DNI_KEY, "")
    var tarjeta = SharedPrefsUtil.get(TARJETA_KEY, false)

    SharedPrefsUtil.set(PEDIDO_ACTIVO_KEY, true)

    Log.d("PEDIDO", "pedido: ${celu}, ${savedEmail}, ${origen}, ${destino}, ${userGeo}")
    viewModel.enviarPedido(
        celu,
        savedEmail,
        origen,
        destino,
        userGeo,
        observaciones,
        pasajero,
        dni,
        tarjeta
    )
}

fun setInitialState(viewModel: HelperViewModel, mainViewModel: MainViewModel) {
    viewModel.setEstadoViaje(Estado.INICIO)
    viewModel.triggerResetMap()
    viewModel.setUpdatePrecio(false)
    mainViewModel.stopLoading()
    //SharedPrefsUtil.set(USER_GEOPOS_KEY, "")
    SharedPrefsUtil.set(USER_DESTINO_KEY, "")
    SharedPrefsUtil.set(USER_ORIGEN_KEY, "")
    SharedPrefsUtil.set(OBSERVACIONES_KEY, "")
    SharedPrefsUtil.set(USER_ORIGEN_DETECTED_KEY, "")
    SharedPrefsUtil.set(DISTANCIA_DESTINO_KEY, 0)
    SharedPrefsUtil.set(GET_RUTA_ACTIVE_KEY, 0)

    viewModel.setDestinoGeopos(
        LatLng(
            0.0,
            0.0
        )
    )
    viewModel.setOrigenGeopos(
        LatLng(
            0.0,
            0.0
        )
    )
}

@Composable
fun ShowCancelDialog(viewModel: HelperViewModel, mainViewModel: MainViewModel) {
    CancelPedidoDialog(onClick = {
        cancelarPedido(viewModel, mainViewModel)
        viewModel.setCancelPedido(false)

    }, onNoClick = { viewModel.setCancelPedido(false) })
}

fun cancelarPedido(viewModel: HelperViewModel, mainViewModel: MainViewModel) {
    val reserva = SharedPrefsUtil.get(RESERVA_KEY, "")
    var savedEmail = SharedPrefsUtil.get(USER_EMAIL_KEY, "")
    var celu = SharedPrefsUtil.get(USER_PHONE_KEY, "")
    mainViewModel.cancelarPedido(reserva, savedEmail, celu)
    setInitialState(viewModel, mainViewModel)
}


fun ValidarDatosCargados(helperViewModel: HelperViewModel) {
    var savedEmail = SharedPrefsUtil.get(USER_EMAIL_KEY, "")
    var celu = SharedPrefsUtil.get(USER_PHONE_KEY, "")
    var name = SharedPrefsUtil.get(USER_NAME_KEY, "")
    if (savedEmail == "" || celu == "" || name == "") {
        helperViewModel.setEstadoViaje(Estado.SIN_DATOS)
    } else {

    }
}

