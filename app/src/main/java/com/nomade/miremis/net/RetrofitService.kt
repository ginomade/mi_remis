package com.nomade.miremis.net

import com.google.gson.GsonBuilder
import com.nomade.miremis.utils.Constants.BASE_ADD
import com.nomade.miremis.utils.Constants.CANCEL_PEDIDO_ADD
import com.nomade.miremis.utils.Constants.ENVIAR_MENSAJES_ADD
import com.nomade.miremis.utils.Constants.GEOCODING_ADD
import com.nomade.miremis.utils.Constants.GEOCODING_BASE_ADD
import com.nomade.miremis.utils.Constants.HAY_MENSAJES_ADD
import com.nomade.miremis.utils.Constants.HISTORIAL_ADD
import com.nomade.miremis.utils.Constants.HISTORIAL_MENSAJES_ADD
import com.nomade.miremis.utils.Constants.LEER_MENSAJES_ADD
import com.nomade.miremis.utils.Constants.MENSAJES_ADD
import com.nomade.miremis.utils.Constants.MOVIL_VIAJE_ADD
import com.nomade.miremis.utils.Constants.PEDIDO_ADD
import com.nomade.miremis.utils.Constants.RECLAMOS_ADD
import com.nomade.miremis.utils.Constants.REVERSE_GEOCODING_ADD
import com.nomade.miremis.utils.Constants.TARIFA_ADD
import com.nomade.miremis.utils.Constants.TARIFA_REMIS_ADD
import com.nomade.miremis.utils.Constants.ULTIMO_VIAJE_ADD
import com.nomade.miremis.utils.Constants.VALIDAR_USER_ADD
import com.nomade.miremis.utils.Constants.VER_LIBRES_ADD
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface RetrofitService {

    @POST(VALIDAR_USER_ADD)
    fun getUserStatus(
        @Query("IMEI") imei: String,
        @Query("version") version: String
    ): Call<String?>?


    @POST(MENSAJES_ADD)
    fun buscarMensajes(
        @Query("IMEI") imei: String
    ): Call<String?>?

    @POST(VER_LIBRES_ADD)
    fun buscarMoviles(
        @Query("email") email: String,
        @Query("usergeopos") usergeopos: String
    ): Call<String?>?

    @POST(MOVIL_VIAJE_ADD)
    fun buscarEstadoMovil(
        @Query("usermovil") usermovil: String,
        @Query("usergeopos") usergeopos: String
    ): Call<String?>?

    @POST(PEDIDO_ADD)
    fun enviarPedido(
        @Query("Celular") celu: String,
        @Query("IMEI") imei: String,
        @Query("Fecha") fecha: String,
        @Query("Hora") hora: String,
        @Query("Origen") origen: String,
        @Query("Destino") destino: String,
        @Query("usergeopos") geopos: String,
        @Query("Observaciones") observaciones: String,
        @Query("Pasajero") pasajero: String,
        @Query("DNI") dni: String,
        @Query("Tarjeta") tarjeta: String
    ): Call<String?>?

    @POST(CANCEL_PEDIDO_ADD)
    fun cancelarPedido(
        @Query("Reserva") reserva: String,
        @Query("IMEI") imei: String,
        @Query("Celular") celu: String
    ): Call<String?>?

    @POST(ULTIMO_VIAJE_ADD)
    fun buscarMovilViaje(
        @Query("IMEI") imei: String
    ): Call<String?>?

    @POST(HAY_MENSAJES_ADD)
    fun hayMensajes(
        @Query("IMEI") imei: String,
        @Query("Reserva") reserva: String
    ): Call<String?>?

    @POST(LEER_MENSAJES_ADD)
    fun leerMensajes(
        @Query("Reserva") reserva: String,
        @Query("IMEI") imei: String
    ): Call<String?>?

    @POST(ENVIAR_MENSAJES_ADD)
    fun enviarMensajes(
        @Query("IMEI") imei: String,
        @Query("Reserva") reserva: String,
        @Query("usergeopos") usergeopos: String,
        @Query("Mensaje") mensaje: String
    ): Call<String?>?

    @POST(HISTORIAL_MENSAJES_ADD)
    fun buscarHistorialMensajes(
        @Query("IMEI") imei: String,
        @Query("Reserva") reserva: String
    ): Call<String?>?

    @POST(HISTORIAL_ADD)
    fun buscarHistorialViajes(
        @Query("IMEI") imei: String
    ): Call<String?>?

    @POST(RECLAMOS_ADD)
    fun enviarReclamo(
        @Query("IMEI") imei: String,
        @Query("Celular") celular: String,
        @Query("Descripcion") desc: String,
        @Query("Pasajero") pasajero: String
    ): Call<String?>?

    @POST(TARIFA_ADD)
    fun buscarTarifas(): Call<String?>?

    @POST(TARIFA_REMIS_ADD)
    fun buscarTarifaRemis(
        @Query("Origen") origen: String,
        @Query("Destino") destino: String
    ): Call<String?>?

    @GET(GEOCODING_ADD)
    fun geoCoding(
        @Query("q") q: String,
        @Query("limit") lon: Int,
        @Query("format") zoom: String,
        @Query("addressdetails") addressdetails: Int
    ): Call<String?>?

    @GET(REVERSE_GEOCODING_ADD)
    fun reverseGeoCoding(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("zoom") zoom: Int,
        @Query("addressdetails") addressdetails: Int
    ): Call<String?>?

    object RetrofitInstance {
        var gson = GsonBuilder()
            .setLenient()
            .create()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Tiempo de espera para establecer conexión
            .readTimeout(30, TimeUnit.SECONDS) // Tiempo de espera para leer datos
            .writeTimeout(30, TimeUnit.SECONDS) // Tiempo de espera para escribir datos
            .build()

        val api: RetrofitService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_ADD)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitService::class.java)
        }
    }

    object RetrofitGeoInstance {
        var gson = GsonBuilder()
            .setLenient()
            .create()

       /* val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Tiempo de espera para establecer conexión
            .readTimeout(30, TimeUnit.SECONDS) // Tiempo de espera para leer datos
            .writeTimeout(30, TimeUnit.SECONDS) // Tiempo de espera para escribir datos
            .("Authorization", "Bearer YOUR_API_KEY")
            .addHeader("User-Agent", "Your-App-Name")
            .build()*/

        val headerInterceptor = Interceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("Cache-Control","no-cache")
                .addHeader("User-Agent",System.getProperty("http.agent")!!)
                .build()
            chain.proceed(request)
        }

        // Configurar el cliente OkHttp con el interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()

        val api: RetrofitService by lazy {
            Retrofit.Builder()
                .baseUrl(GEOCODING_BASE_ADD)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitService::class.java)
        }
    }

}