package com.cct.group010.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cct.group010.finalproject.R;
import com.cct.group010.finalproject.domain.CustomGuestRequestToken;
import com.cct.group010.finalproject.domain.Guest;
import com.cct.group010.finalproject.domain.Reservation;
import com.cct.group010.finalproject.domain.Room;
import com.cct.group010.finalproject.model.JwtToken;
import com.cct.group010.finalproject.tokenmanager.TokenManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginButton;
    private ImageView logo;
    private TokenManager tokenManager;



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
                        tokenManager.createSession(username, jwtToken.getToken().toString());

                        Call<JsonObject> guestCall = ImportantObjects.apiCall.getGuest("Bearer " + jwtToken.getToken().toString());


                        guestCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                                getGuest(response, username);

                                // reservation list start code!!!

                                String p = ImportantObjects.guest.getId().toString();

                                String authorization = "Bearer " + jwtToken.getToken().toString();

                                getReservation(authorization);

                                getBed(authorization, ImportantObjects.guest);
                                ImportantObjects.guest.setBed(ImportantObjects.bed);


                                System.out.println("I came here!!!!!!!!");


                                for (Reservation res : ImportantObjects.guest.getReservationList()) {
//                                    System.out.println("I came here!!!!!!!!" + res.getId());
//                                    getReservationOta(authorization, res);


                                }







                                intentMenuActivity();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                System.out.println("THIS IS AN ERROR!!!!");
                                System.out.println(t.getMessage());
                            }
                        });
                    }
                }


                @Override
                public void onFailure(Call<JwtToken> call, Throwable t) {
                    System.out.println("THIS IS A FAILURE :");
                    showToast(t.getMessage());
                }
            });


        });


    }

    private void intentMenuActivity() {

        Intent i = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(i);

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void getGuest(Response<JsonObject> response, String username) {

        // Separating the elements. The JSON request gets a complete JsonObject and Objects within it.
        // First it starts with _embedded then we have the guests and other objects such as pagination...
        JsonElement jsonElement = response.body().get("_embedded");
        JsonObject responseObject = jsonElement.getAsJsonObject();
        JsonArray guests = responseObject.getAsJsonArray("guest");

        // here we loop through the guests returned and get their data.

        for (int i = 0; i < guests.size(); i++) {

            JsonObject guestObject = guests.get(i).getAsJsonObject();
            // here we get the links to the other requests where there's a dependency between different classes
            JsonObject guestLinks = guestObject.get("_links").getAsJsonObject();

            // here we get the e-mail to compare to the one the user input when logging in. This way we
            // can get the data from the right guest in the json array
            String email = guestObject.get("email").toString().replace("\"", "");
            // logic to get the id, as for security reasons is not displayed on the json request

            int id = Integer.parseInt(getId("guest", 6, guestObject));



            if (username.contentEquals(email)) {

                ImportantObjects.guest.setEmail(email);
                ImportantObjects.guest.setName(guestObject.get("name").toString().replace("\"", ""));
                ImportantObjects.guest.setId((id));


                String reservationsPath = guestObject.get("_links").getAsJsonObject()
                        .get("reservationList")
                        .getAsJsonObject().get("href").toString();

//                System.out.println(guest.toString());
                break;

            }

            System.out.println("THE GUEST CALL HAS WORKED");

        }

    }


    private void getReservation(String authorization) {

        Call<JsonObject> reservationCall = ImportantObjects.apiCall.getReservation(
                ImportantObjects.guest.getId().toString(),
                authorization);


        reservationCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                JsonObject responseObject = response.body().get("_embedded").getAsJsonObject();

                JsonArray reservations = responseObject.getAsJsonArray("reservation");

                LocalDate dateNow = LocalDate.now();

                for (int i = 0; i < reservations.size(); i++) {

                    JsonObject reservationObject = reservations
                            .get(i).getAsJsonObject();

                    ImportantObjects.reservation.setId(getId("reservation", 12, reservationObject));

                    getReservationOta(authorization, ImportantObjects.reservation);
                    ImportantObjects.reservation.setOta(ImportantObjects.ota);
                    getRoom(authorization, ImportantObjects.reservation);
                    ImportantObjects.reservation.setRoom(ImportantObjects.room);

                    String checkout = reservationObject.get("checkout").
                            toString()
                            .replace("-", "")
                            .replace("\"", "");
                    int month = Integer.parseInt(checkout.substring(4, 6));
                    int year = Integer.parseInt(checkout.substring(0, 4));
                    int day = Integer.parseInt(checkout.substring(6, 8));
                    LocalDate checkoutDate = LocalDate.of(year, month, day);


                    System.out.println("THIS IS THE RESERVATION ID: " + getId("reservation", 12, reservationObject));


                    String originalBookingNumber = reservationObject
                            .get("originalBookingNumber").toString().replace("\"", "");
                    ImportantObjects.reservation.setOriginalBookingNumber(originalBookingNumber);
                    Integer numberOfGuests = Integer.parseInt(
                            reservationObject
                                    .get("numberOfGuests").toString().replace("\"", "")
                    );
                    ImportantObjects.reservation.setNumberOfGuests(numberOfGuests);
                    String reservationStatus = reservationObject
                            .get("reservationStatus").toString().replace("\"", "");
                    ImportantObjects.reservation.setReservationStatus(reservationStatus);

                    String checkin = reservationObject.get("checkin").
                            toString()
                            .replace("-", "")
                            .replace("\"", "");
                    LocalDate checkinDate = LocalDate.of(Integer.parseInt(
                            checkin.substring(0, 4)),
                            Integer.parseInt(checkin.substring(4, 6)),
                            Integer.parseInt(checkin.substring(6, 8)));
                    ImportantObjects.reservation.setCheckin(checkinDate);
                    ImportantObjects.reservation.setCheckout(checkoutDate);

                    getProperty(authorization, ImportantObjects.reservation);
                    ImportantObjects.reservation.setProperty(ImportantObjects.property);

                    System.out.println("THIS IS THE RESERVATION");
                    System.out.println(ImportantObjects.reservation.toString());

                    ImportantObjects.guest.getReservationList().add(ImportantObjects.reservation);


                }

                for (Reservation res : ImportantObjects.guest.getReservationList()) {
                    if (res.getCheckin().compareTo(dateNow) > 0) {
                        ImportantObjects.guest.setNextReservation(res);
                        break;
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("the reservation call hasn't worked");
            }
        });


    }


    private void getReservationOta(String authorization, Reservation r) {




        Call<JsonObject> reservationOtaCall = ImportantObjects.apiCall.getReservationOta(
                r.getId(),
                authorization);

        reservationOtaCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject otaObject = response.body().getAsJsonObject();

                ImportantObjects.ota.setName(otaObject.get("name").toString().replace("\"", ""));
                ImportantObjects.ota.setWebsite(otaObject.get("website").toString().replace("\"", ""));
                ImportantObjects.ota.setId((getId("ota", 4, otaObject)));
                System.out.println("this is the OTA: "+ ImportantObjects.ota.toString());



            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Failure: " + t);


            }
        });


    }

    private void getRoom(String authorization, Reservation res){

        String id = res.getId();
        Call<JsonObject> reservationRoomCall = ImportantObjects.apiCall.getRoom(
                id,
                authorization);

        reservationRoomCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject roomObject = response.body().getAsJsonObject();
                ImportantObjects.room.setType(roomObject.get("type").toString().replace("\"", ""));
                ImportantObjects.room.setRoomNumber(roomObject.get("roomNumber").toString().replace("\"", ""));
                ImportantObjects.room.setFloor(roomObject.get("floor").toString().replace("\"", ""));
                ImportantObjects.room.setNumberBeds(Integer.parseInt(roomObject.get("numberBeds").toString().replace("\"", "")));
                ImportantObjects.room.setRfidTag(roomObject.get("rfidTag").toString().replace("\"", ""));


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });



    }


    private void getProperty(String authorization, Reservation res){

        String id = res.getId();
        Call<JsonObject> reservationPropertyCall = ImportantObjects.apiCall.getReservationProperty(
                id,
                authorization);

        reservationPropertyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject propertyObject = response.body().getAsJsonObject();

                ImportantObjects.property.setId(getId("properties", 11, propertyObject));
                ImportantObjects.property.setName(propertyObject.get("name").toString().replace("\"", ""));
                ImportantObjects.property.setAddress(propertyObject.get("address").toString().replace("\"", ""));
                System.out.println("THIS IS THE PROPERTY:");
                System.out.println(ImportantObjects.property.toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }


    private void getBed(String authorization, Guest guest){
        String id = ""+guest.getId();
        Call<JsonObject> guestBedCall = ImportantObjects.apiCall.getGuestBed(
                id,
                authorization);

        guestBedCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject bedObject = response.body().getAsJsonObject();

                ImportantObjects.bed.setRoomNumber(bedObject.get("roomNumber").toString().replace("\"", ""));
                ImportantObjects.bed.setNumber(Integer.parseInt(bedObject.get("number").toString().replace("\"", "")));
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


        return ""+id;

    }
}


