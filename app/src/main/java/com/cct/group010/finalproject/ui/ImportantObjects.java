package com.cct.group010.finalproject.ui;

import com.cct.group010.finalproject.domain.Bed;
import com.cct.group010.finalproject.domain.Guest;
import com.cct.group010.finalproject.domain.OTA;
import com.cct.group010.finalproject.domain.Property;
import com.cct.group010.finalproject.domain.Reservation;
import com.cct.group010.finalproject.domain.Room;
import com.cct.group010.finalproject.remote.APICall;
import com.cct.group010.finalproject.remote.RetroClass;

import java.util.ArrayList;
import java.util.List;

public class ImportantObjects {

    // Everything is defined within the guest. E.g.: Guest has reservation, which has the room, which has the property.
    protected static final Guest guest = new Guest();


    protected static final APICall apiCall = RetroClass.getAPICall();
}
