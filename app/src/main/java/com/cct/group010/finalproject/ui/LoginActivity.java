package com.cct.group010.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cct.group010.finalproject.R;
import com.cct.group010.finalproject.domain.CustomGuestRequestToken;
import com.cct.group010.finalproject.domain.Guest;
import com.cct.group010.finalproject.domain.OTA;
import com.cct.group010.finalproject.domain.Property;
import com.cct.group010.finalproject.domain.Reservation;
import com.cct.group010.finalproject.domain.Room;
import com.cct.group010.finalproject.model.JwtToken;
import com.cct.group010.finalproject.tokenmanager.TokenManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginButton;
    private TokenManager tokenManager;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_login);

        tokenManager = new TokenManager(getApplicationContext());

        // Assign values to each control on the layout
        loginButton = findViewById(R.id.loginButton);
        email = (EditText) findViewById(R.id.insertEmail);
        password = (EditText) findViewById(R.id.insertPassword);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        loginButton.setOnClickListener(view -> {

            String username = email.getText().toString();
            String pass = password.getText().toString();
            CustomGuestRequestToken guestRequestToken = new CustomGuestRequestToken(username, pass);

            Call<JwtToken> jwtTokenCall = ImportantObjects.apiCall.userLogin(guestRequestToken);

            jwtTokenCall.enqueue(new Callback<JwtToken>() {
                @Override
                public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {

                    if (response.body() != null) {
                        JwtToken jwtToken = response.body();
                        String token = jwtToken.getToken().toString();
                        tokenManager.createSession(username, token);



                        getGuest(token, username);

                        getBed(token);

                        getReservations(token);

                        intentMenuActivity();

                    }

                }

                @Override
                public void onFailure(Call<JwtToken> call, Throwable t) {

                }
            });

        });


    }

    private void intentMenuActivity() {

        Intent i = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(i);

    }

    private void getGuest(String token, String username) {

        Call<JsonObject> guestCall = ImportantObjects.apiCall.getGuest("Bearer " + token);

        guestCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Separating the elements. The JSON request gets a complete JsonObject and Objects within it.
                // First it starts with _embedded then we have the guests and other objects such as pagination...
                JsonElement jsonElement = response.body().get("_embedded");
                JsonObject responseObject = jsonElement.getAsJsonObject();
                JsonArray guests = responseObject.getAsJsonArray("guest");

                // here we loop through the guests returned and get their data.
                for (JsonElement g : guests) {
                    JsonObject guestObject = g.getAsJsonObject();
                    // here we get the links to the other requests where there's a dependency between different classes
                    JsonObject guestLinks = guestObject.get("_links").getAsJsonObject();

                    // here we get the e-mail to compare to the one the user input when logging in. This way we
                    // can get the data from the right guest in the json array
                    String email = guestObject.get("email").toString().replace("\"", "");
                    // logic to get the id, as for security reasons is not displayed on the json request

                    int id = Integer.parseInt(getId("guest", 6, guestObject));


                    if (username.contentEquals(email)) {


                        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        mEditor = mPreferences.edit();

                        mEditor.putString("email", email);

                        mEditor.putString("name", guestObject.get("name").toString().replace("\"", ""));

                        mEditor.putInt("id", id);

                        mEditor.commit();

                        // System.out.println(guest.toString());
                        break;

                    }

                    System.out.println("THE GUEST CALL HAS WORKED");
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });



    }


    private void getReservations(String token){

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        String id = mPreferences.getInt("id",0)+"";

        Call<JsonObject> reservationCall = ImportantObjects.apiCall.getReservation(
                id,
                "Bearer "+token);

        reservationCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject responseObject = response.body().get("_embedded").getAsJsonObject();
                JsonArray reservations = responseObject.getAsJsonArray("reservation");

                System.out.println("this is the response Obj for reservations::");
                System.out.println(responseObject.toString());


                mEditor.putString("reservationsData", responseObject.toString());
                mEditor.commit();

                LocalDate dateNow = LocalDate.now();

                for(JsonElement res: reservations){

                    JsonObject reservationObject = res.getAsJsonObject();


                    String checkin = reservationObject.get("checkin").
                            toString()
                            .replace("-", "")
                            .replace("\"", "");
                    LocalDate checkinDate = LocalDate.of(Integer.parseInt(
                            checkin.substring(0, 4)),
                            Integer.parseInt(checkin.substring(4, 6)),
                            Integer.parseInt(checkin.substring(6, 8)));

                    if (checkinDate.compareTo(dateNow) > 0){
                        String id = (getId("reservation", 12, reservationObject));

                        mEditor.putString("reservationId", id);

                        checkin = reservationObject.get("checkin").
                                toString()
                                .replace("\"", "");
                        mEditor.putString("reservationCheckIn", checkin);

                        String checkout = reservationObject.get("checkout").
                                toString()
                                .replace("\"", "");

                        mEditor.putString("reservationCheckOut", checkout);
                        mEditor.putString("reservationOriginalBookingNumber",
                                reservationObject.get("originalBookingNumber").toString().replace("\"", ""));
                        mEditor.putString("reservationStatus", reservationObject.get(
                                "reservationStatus").toString().replace("\"", ""));

                        mEditor.commit();

                        getProperty(token, id);

                        getRoom(token, id);

                        break;

                    }

                }



            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                System.out.println("::: RESERVATION CALL ERROR :::");

            }
        });

    }

    private OTA getReservationOTA(String token, String id){

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        Call<JsonObject> reservationOtaCall = ImportantObjects.apiCall.getReservationOta(
                id,
                "Bearer "+token);

        OTA ota = new OTA();

        reservationOtaCall.enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject otaObject = response.body().getAsJsonObject();

//                ota.setName(otaObject.get("name").toString().replace("\"", ""));
//                ota.setWebsite(otaObject.get("website").toString().replace("\"", ""));
//                ota.setId((getId("ota", 4, otaObject)));

                mEditor.putString("otaName", otaObject.get("name").toString().replace("\"", ""));
                mEditor.putString("otaWebsite", otaObject.get("website").toString().replace("\"", ""));
                mEditor.commit();




            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                System.out.println("::: THE OTA CALL DIDN'T WORK :::");

            }
        });

        return ota;

    }

    private void getRoom(String token, String resId){

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
//        String id = mPreferences.getInt("id",0)+"";

        Call<JsonObject> reservationRoomCall = ImportantObjects.apiCall.getRoom(
                resId,
                "Bearer "+token);

        Room r = new Room();


        reservationRoomCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject roomObject = response.body().getAsJsonObject();

//                r.setRfidTag(roomObject.get("rfidTag").toString().replace("\"", ""));
//                r.setRoomNumber(roomObject.get("roomNumber").toString().replace("\"", ""));
//                r.setFloor(roomObject.get("floor").toString().replace("\"", ""));
//                r.setType(roomObject.get("type").toString().replace("\"", ""));

                mEditor.putString("roomType", roomObject.get("type").toString().replace("\"", ""));
                mEditor.putString("roomNumber", roomObject.get("roomNumber").toString().replace("\"", ""));
                mEditor.putString("roomFloor", roomObject.get("floor").toString().replace("\"", ""));
                mEditor.putString("roomRfidTag", roomObject.get("rfidTag").toString().replace("\"", ""));
                mEditor.commit();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });





    }

    private void getProperty(String token, String resId){

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        Call<JsonObject> reservationPropertyCall = ImportantObjects.apiCall.getReservationProperty(
                resId,
                "Bearer "+token);

//        Property p = new Property();

        reservationPropertyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject propertyObject = response.body().getAsJsonObject();
//                p.setName(propertyObject.get("name").toString().replace("\"", ""));
//                p.setAddress(propertyObject.get("address").toString().replace("\"", ""));
                mEditor.putString("propertyName", propertyObject.get("name").toString().replace("\"", ""));
                mEditor.putString("propertyAddress", propertyObject.get("address").toString().replace("\"", ""));
                mEditor.commit();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                System.out.println("::: GET PROPERTY DIDN'T WORK :::");

            }
        });
//        return p;

    }

    private void getBed(String token){
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        String id = mPreferences.getInt("id",1)+"";
        Call<JsonObject> guestBedCall = ImportantObjects.apiCall.getGuestBed(
                id,
                "Bearer "+token);

        guestBedCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("the response: " + response);

                JsonObject bedObject = response.body().getAsJsonObject();
                int bedNum = Integer.parseInt(bedObject.get("number").toString().replace("\"", ""));
                mEditor.putInt("bedNumber", bedNum);
                mEditor.commit();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    private String getId(String paramIndexOf, int startCount, JsonObject jsonObject) {


        String idPath = jsonObject.get("_links").getAsJsonObject().get("self")
                .getAsJsonObject().get("href").toString();

        Integer position = idPath.indexOf(paramIndexOf);

        int id = Integer.parseInt(idPath.substring((position + startCount), (idPath.length() - 1)));


        return "" + id;

    }






}


