package com.cct.group010.finalproject.remote;

import com.cct.group010.finalproject.domain.CustomGuestRequestToken;
import com.cct.group010.finalproject.model.JwtToken;
import com.google.gson.JsonObject;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICall {



    @POST("/authenticate-guest")
    Call<JwtToken> userLogin(@Body CustomGuestRequestToken customGuestRequestToken);

    @GET("/guest")
    Call<JsonObject> getGuest(@Header("Authorization") String authorization);

    @GET("/reservation/{id}/room")
    Call<JsonObject> getRoom(@Path("id") String id, @Header("Authorization") String authorization);

    @GET("/guest/{id}/bed")
    Call<JsonObject> getGuestBed(@Path("id") String id,@Header("Authorization") String authorization);


    @GET("/guest/{id}/reservationList")
    Call<JsonObject> getReservation(@Path("id") String id, @Header("Authorization") String authorization);

    @GET("/reservation/{id}/ota")
    Call<JsonObject> getReservationOta(@Path("id") String id, @Header("Authorization") String authorization);

    @GET("/reservation/{id}/property")
    Call<JsonObject> getReservationProperty(@Path("id") String id, @Header("Authorization") String authorization);





}
