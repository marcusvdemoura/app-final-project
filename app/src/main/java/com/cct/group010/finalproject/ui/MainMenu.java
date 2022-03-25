package com.cct.group010.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cct.group010.finalproject.R;
import com.cct.group010.finalproject.domain.Reservation;

public class MainMenu extends AppCompatActivity {

    private TextView reservationMoreInfo, reservationStatus,reservationDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_main_menu);


        reservationMoreInfo = (TextView) findViewById(R.id.reservationMoreInfo);

        reservationStatus = (TextView) findViewById(R.id.reservationStatus);
        reservationDates = (TextView) findViewById(R.id.reservationDates);
        reservationStatus.setText(ImportantObjects.guest.getNextReservation().getReservationStatus());

        String date = ImportantObjects.guest.getNextReservation().getCheckin().toString() + " -\n "
                + ImportantObjects.guest.getNextReservation().getCheckout().toString();
        reservationDates.setText(date);





        reservationMoreInfo.setOnClickListener(view -> {

            System.out.println(ImportantObjects.guest.toString());

            System.out.println(ImportantObjects.guest.getReservationList().get(0).toString());

            System.out.println("THIS IS THE DATA I WANT TO PARSE inside de clickable:");
            System.out.println(ImportantObjects.guest.toString());





        });




    }
}