package com.cct.group010.finalproject.remote;

import com.cct.group010.finalproject.domain.CustomGuestRequestToken;
import com.cct.group010.finalproject.domain.Guest;
import com.cct.group010.finalproject.model.JwtToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APICall {

    @POST("/authenticate-guest")
    Call<JwtToken> userLogin(@Body CustomGuestRequestToken customGuestRequestToken);

    @GET("/guest")
    Call<Guest[]> getGuest(@Header("Authorization") String authorization);


}
