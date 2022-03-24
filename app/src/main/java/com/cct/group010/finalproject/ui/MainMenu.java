package com.cct.group010.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.cct.group010.finalproject.R;
import com.cct.group010.finalproject.domain.Reservation;

public class MainMenu extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_main_menu);


        button = findViewById(R.id.button);


        button.setOnClickListener(view -> {

            System.out.println(ImportantObjects.guest.toString());
            System.out.println(ImportantObjects.guest.getReservationList().get(0).toString());


        });
    }
}