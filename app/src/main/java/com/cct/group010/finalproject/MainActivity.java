package com.cct.group010.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.cct.group010.finalproject.ui.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);

        openLoginActivity();
    }


    public void openLoginActivity() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
}