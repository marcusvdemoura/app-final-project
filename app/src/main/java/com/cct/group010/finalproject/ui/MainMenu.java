package com.cct.group010.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cct.group010.finalproject.R;
import com.cct.group010.finalproject.domain.Reservation;

public class MainMenu extends AppCompatActivity {

    private TextView reservationMoreInfo, reservationStatus,reservationDates;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_main_menu);


        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);


//        System.out.println("PRINTING GUEST");
//        System.out.println("the guest:::"+mPreferences.getString("email",""));
//        System.out.println(mPreferences.getString("name", ""));
//        System.out.println(mPreferences.getInt("id",0));
//
//
//        System.out.println("THIS IS THE RESERVATIONS: ");
//        System.out.println(mPreferences.getString("reservationsData", ""));


        reservationMoreInfo = (TextView) findViewById(R.id.reservationMoreInfo);

        reservationStatus = (TextView) findViewById(R.id.reservationStatus);
        reservationStatus.setText(mPreferences.getString("reservationStatus", "didn't work"));
        reservationDates = (TextView) findViewById(R.id.reservationDates);
        reservationDates.setText(mPreferences.getString("reservationCheckIn", "didn't work") + " - \n" +
                mPreferences.getString("reservationCheckOut", "didn't work"));





        reservationMoreInfo.setOnClickListener(view -> {








        });




    }
}