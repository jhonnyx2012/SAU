package uneg.software.sau.interfaces;


import com.google.gson.JsonElement;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import uneg.software.sau.models.Alarma;
import uneg.software.sau.models.Area;
import uneg.software.sau.models.BaseResponse;
import uneg.software.sau.models.LoginResponse;

/**
 * Created by Jhonny on 18/8/2015.
 */
public interface ApiRestInterface {
    @FormUrlEncoded
    @POST("/login")
    void doLogin(
            @Field("username") String username,
            @Field("password") String password,
            Callback<LoginResponse> callback);

    @FormUrlEncoded
    @POST("/register")
    void registrarUsuario(
            @Field("nombre") String nombre,
            @Field("apellido") String apellido,
            @Field("username") String username,
            @Field("correo") String correo,
            @Field("telefono") String telefono,
            @Field("password") String clave,
            @Field("nivel") String nivel,
            Callback<BaseResponse> callback);

    @GET("/alarma/getAlarmasAceptadas")
    void getAlarmas(
            @Query("area") int area,
            @Query("show_cerca") boolean show_cerca,
            Callback<ArrayList<Alarma>> callback);



    @FormUrlEncoded
    @POST("/alarma/subirFoto")
    void subirFoto(
            @Field("foto") String foto,
            Callback<BaseResponse> callback);

    @FormUrlEncoded
    @POST("/alarma")
    void sendAlarma(
            @Field("usuario") String usuario,
            @Field("descripcion") String descripcion,
            @Field("lat") String lat,
            @Field("lon") String lon,
            @Field("area") String area,
            @Field("fotos") String[] fotos,
            Callback<Alarma> callback);

    @FormUrlEncoded
    @POST("/recuperar")
    void solicitarCambioPass(
            @Field("correo") String correo,
            Callback<BaseResponse> callback);

    @GET("/area")
    void getAreas(
            Callback<ArrayList<Area>> callback);
}