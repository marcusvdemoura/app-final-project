package com.cct.group010.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cct.group010.finalproject.R;
import com.cct.group010.finalproject.domain.Bed;
import com.cct.group010.finalproject.domain.CustomGuestRequestToken;
import com.cct.group010.finalproject.domain.Guest;
import com.cct.group010.finalproject.domain.OTA;
import com.cct.group010.finalproject.domain.Property;
import com.cct.group010.finalproject.domain.Reservation;
import com.cct.group010.finalproject.domain.Room;
import com.cct.group010.finalproject.model.JwtToken;
import com.cct.group010.finalproject.remote.APICall;
import com.cct.group010.finalproject.remote.RetroClass;
import com.cct.group010.finalproject.tokenmanager.TokenManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginButton;
    private ImageView logo;
    private TokenManager tokenManager;
    private static final Guest guest = new Guest();
    private static final Property property = new Property();
    private static final Bed bed = new Bed();
    private static final List<Room> room = new ArrayList<>();
    private static final APICall apiCall = RetroClass.getAPICall();

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


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = email.getText().toString();
                String pass = password.getText().toString();
                CustomGuestRequestToken guestRequestToken = new CustomGuestRequestToken(username, pass);


                retrofit2.Call<JwtToken> jwtTokenCall = apiCall.userLogin(guestRequestToken);

                jwtTokenCall.enqueue(new Callback<JwtToken>() {
                    @Override
                    public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                        if (response.body() != null) {

                            JwtToken jwtToken = response.body();
                            tokenManager.createSession(username, jwtToken.getToken().toString());

                            Call<JsonObject> guestCall = apiCall.getGuest("Bearer " + jwtToken.getToken().toString());


                            guestCall.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                                    getGuest(response, username);

                                    // reservation list start code!!!

                                    String p = guest.getId().toString();

                                    String authorization = "Bearer " + jwtToken.getToken().toString();

                                    getReservation(authorization);


                                    for (Reservation r : guest.getReservationList()) {
                                        getReservationOta(authorization, r);
                                    }


//                                    intentMenuActivity();
//                                    finish();
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


            }

        });


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

            int id = getId("guest", 6, guestObject);


            if (username.contentEquals(email)) {

                guest.setEmail(email);
                guest.setName(guestObject.get("name").toString().replace("\"", ""));
                guest.setId((id));

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

        Call<JsonObject> reservationCall = apiCall.getReservation(
                guest.getId().toString(),
                authorization);


        reservationCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                JsonObject responseObject = response.body().get("_embedded").getAsJsonObject();

                JsonArray reservations = responseObject.getAsJsonArray("reservation");

                Reservation r = new Reservation();

                LocalDate dateNow = LocalDate.now();

                for (int i = 0; i < reservations.size(); i++) {

                    JsonObject reservationObject = reservations
                            .get(i).getAsJsonObject();


                    String checkout = reservationObject.get("checkout").
                            toString()
                            .replace("-", "")
                            .replace("\"", "");
                    int month = Integer.parseInt(checkout.substring(4, 6));
                    int year = Integer.parseInt(checkout.substring(0, 4));
                    int day = Integer.parseInt(checkout.substring(6, 8));
                    LocalDate checkoutDate = LocalDate.of(year, month, day);

                    int id = getId("reservation", 12, reservationObject);
                    r.setId((id));


                    String originalBookingNumber = reservationObject
                            .get("originalBookingNumber").toString().replace("\"", "");
                    r.setOriginalBookingNumber(originalBookingNumber);
                    Integer numberOfGuests = Integer.parseInt(
                            reservationObject
                                    .get("numberOfGuests").toString().replace("\"", "")
                    );
                    r.setNumberOfGuests(numberOfGuests);
                    String reservationStatus = reservationObject
                            .get("reservationStatus").toString().replace("\"", "");
                    r.setReservationStatus(reservationStatus);

                    String checkin = reservationObject.get("checkin").
                            toString()
                            .replace("-", "")
                            .replace("\"", "");
                    LocalDate checkinDate = LocalDate.of(Integer.parseInt(
                            checkin.substring(0, 4)),
                            Integer.parseInt(checkin.substring(4, 6)),
                            Integer.parseInt(checkin.substring(6, 8)));
                    r.setCheckin(checkinDate);
                    r.setCheckout(checkoutDate);
                    guest.getReservationList().add(r);


                }

                for (Reservation res : guest.getReservationList()) {
                    if (res.getCheckin().compareTo(dateNow) > 0) {
                        guest.setNextReservation(res);
                        break;
                    }
                }
                System.out.println("the reservation call has worked");

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("the reservation call hasn't worked");
            }
        });


    }


    private void getReservationOta(String authorization, Reservation r) {


        Call<JsonObject> reservationOtaCall = apiCall.getReservationOta(
                r.getId().toString(),
                authorization);

        reservationOtaCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject otaObject = response.body().getAsJsonObject();
                r.getOta().setName(otaObject.get("name").toString().replace("\"", ""));
                r.getOta().setWebsite(otaObject.get("website").toString().replace("\"", ""));
                r.getOta().setId((getId("ota", 4, otaObject)));


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Failure: " + t);


            }
        });


    }

    private int getId(String pathIndexOf, int startCount, JsonObject jsonObject) {


        String idPath = jsonObject.get("_links").getAsJsonObject().get("self")
                .getAsJsonObject().get("href").toString();

        Integer position = idPath.indexOf(pathIndexOf);

        int id = Integer.parseInt(idPath.substring((position + startCount), (idPath.length() - 1)));


        return id;

    }
}


