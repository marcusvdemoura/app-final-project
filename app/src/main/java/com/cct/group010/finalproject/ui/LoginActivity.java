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
import com.cct.group010.finalproject.domain.CustomGuestRequestToken;
import com.cct.group010.finalproject.domain.Guest;
import com.cct.group010.finalproject.model.JwtToken;
import com.cct.group010.finalproject.remote.APICall;
import com.cct.group010.finalproject.remote.RetroClass;
import com.cct.group010.finalproject.tokenmanager.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginButton;
    private ImageView logo;
    private TokenManager tokenManager;
    private static final Guest guest = new Guest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
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

                final APICall apiCall = RetroClass.getAPICall();

                retrofit2.Call<JwtToken> jwtTokenCall = apiCall.userLogin(guestRequestToken);

                jwtTokenCall.enqueue(new Callback<JwtToken>() {
                    @Override
                    public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                        if (response.body() != null){

                            JwtToken jwtToken = response.body();
                            tokenManager.createSession(username, jwtToken.getToken().toString());

                            Call<Guest[]> staffCall = apiCall.getGuest("Bearer "+jwtToken.getToken().toString());

                            staffCall.enqueue(new Callback<Guest[]>() {
                                @Override
                                public void onResponse(Call<Guest[]> call, Response<Guest[]> response) {
                                    System.out.println("THIS HAS WORKED!!!!");
//                                    intentMenuActivity();
//                                    finish();
                                }

                                @Override
                                public void onFailure(Call<Guest[]> call, Throwable t) {
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

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}