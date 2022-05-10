package com.cct.group010.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    public static final String ERROR = "No NFC tag Detected";
    public static final String ERROR_PROCESSING = "Error during wirting";
    public static final String SUCCESS = "Text Written Successfully";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_login);

        tokenManager = new TokenManager(getApplicationContext());

        // Assign values to each control on the layout
        loginButton = findViewById(R.id.loginBtn);
        email = (EditText) findViewById(R.id.insertEmail);
        password = (EditText) findViewById(R.id.insertPassword);
        context = this;
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

                    writeModeOn();
                    try {
                        if (myTag == null) {
                            Toast.makeText(context, ERROR, Toast.LENGTH_LONG).show();
                        } else {
                            write("PlainText|" + "TAG WILL BE HERE", myTag);
                            Toast.makeText(context, SUCCESS, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(context, ERROR_PROCESSING, Toast.LENGTH_LONG).show();
                    }


                }

                public void onFailure(Call<JwtToken> call, Throwable t) {

                }
            });
        });
//************************************ Start *******************************************************
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                writeModeOn();
//                try {
//                    if (myTag == null) {
//                        Toast.makeText(context, ERROR, Toast.LENGTH_LONG).show();
//                    } else {
//                        write("PlainText|" + "TAG WILL BE HERE", myTag);
//                        Toast.makeText(context, SUCCESS, Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception e) {
//                    Toast.makeText(context, ERROR_PROCESSING, Toast.LENGTH_LONG).show();
//                }
//            }
//
//        });
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "This device does not support this shit", Toast.LENGTH_LONG).show();
            finish();
        }
       // readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] {tagDetected};

        }

//    private void readFromIntent(Intent intent) {
//        String action = intent.getAction();
//        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
//                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
//                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
//            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//            NdefMessage[] msgs = null;
//            if (rawMsgs != null) {
//                msgs = new NdefMessage[rawMsgs.length];
//                for (int i = 0; i < rawMsgs.length; i++) {
//                    msgs[i] = (NdefMessage) rawMsgs[i];
//                }
//            }
//            buildTagViews(msgs);
//        }
//    }
//    private void buildTagViews(NdefMessage[] msgs) {
//        if (msgs == null || msgs.length == 0) return;
//
//        String text = "";
//        byte[] payload = msgs[0].getRecords()[0].getPayload();
//        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; //get the text encoding
//        int languageCodeLength = payload[0] & 0063;//get the language code eg = "EN"
//
//        try {
//            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
//        } catch (UnsupportedEncodingException e) {
//            Log.e("Unsupported", e.toString());
//        }
//
//        textView.setText("NFC Content: " + text);
//    }

    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = {createRecord(text)};
        NdefMessage message = new NdefMessage(records);
        //get an instance of ndef for the tag
        Ndef ndef = Ndef.get(tag);
        //enable i/o
        ndef.connect();
        //write the message
        ndef.writeNdefMessage(message);
        //close connection
        ndef.close();
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];

        //set status byte see ndef spec for actual bits
        payload[0] = (byte) langLength;

        //copy langbytes and lengbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);

        return recordNFC;
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        //readFromIntent(intent);
//        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
//            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        }
//    }


    @Override
    public void onPause() {
        super.onPause();
        writeModeOff();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        writeModeOn();
//    }

    private void writeModeOn() {
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    private void writeModeOff() {
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }
//************************************ END *******************************************************
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


    private void getReservations(String token) {

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        String id = mPreferences.getInt("id", 0) + "";

        Call<JsonObject> reservationCall = ImportantObjects.apiCall.getReservation(
                id,
                "Bearer " + token);

        reservationCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonElement jsonElement = response.body().get("_embedded"); //returning null first call
                JsonObject responseObject = jsonElement.getAsJsonObject();
//                JsonObject responseObject = response.body().get("_embedded").getAsJsonObject();
                JsonArray reservations = responseObject.getAsJsonArray("reservation");

                System.out.println("this is the response Obj for reservations::");
                System.out.println(responseObject.toString());


                mEditor.putString("reservationsData", responseObject.toString());
                mEditor.commit();

                LocalDate dateNow = LocalDate.now();

                for (JsonElement res : reservations) {

                    JsonObject reservationObject = res.getAsJsonObject();


                    String checkin = reservationObject.get("checkin").
                            toString()
                            .replace("-", "")
                            .replace("\"", "");
                    LocalDate checkinDate = LocalDate.of(Integer.parseInt(
                            checkin.substring(0, 4)),
                            Integer.parseInt(checkin.substring(4, 6)),
                            Integer.parseInt(checkin.substring(6, 8)));

                    if (checkinDate.compareTo(dateNow) > 0) {
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

    private OTA getReservationOTA(String token, String id) {

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        Call<JsonObject> reservationOtaCall = ImportantObjects.apiCall.getReservationOta(
                id,
                "Bearer " + token);

        OTA ota = new OTA();

        reservationOtaCall.enqueue(new Callback<JsonObject>() {
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

    private void getRoom(String token, String resId) {

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
//        String id = mPreferences.getInt("id",0)+"";

        Call<JsonObject> reservationRoomCall = ImportantObjects.apiCall.getRoom(
                resId,
                "Bearer " + token);

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

    private void getProperty(String token, String resId) {

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        Call<JsonObject> reservationPropertyCall = ImportantObjects.apiCall.getReservationProperty(
                resId,
                "Bearer " + token);

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

    private void getBed(String token) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        String id = mPreferences.getInt("id", 1) + "";
        Call<JsonObject> guestBedCall = ImportantObjects.apiCall.getGuestBed(
                id,
                "Bearer " + token);

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


