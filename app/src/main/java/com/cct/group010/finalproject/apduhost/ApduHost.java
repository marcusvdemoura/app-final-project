package com.cct.group010.finalproject.apduhost;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.math.BigInteger;
import java.util.Arrays;

public class ApduHost extends HostApduService {

    // Application Protocol Data Unit
    String TAG = "Host Card Emulator - Connection Stoped";
    String STATUS_SUCCESS = "9000";
    String STATUS_FAILED = "6F00";
    String CLA_NOT_SUPPORTED = "6E00";
    String INS_NOT_SUPPORTED = "6D00";
    String AID  = "D2760000850100";
    String AID2 = "D2760000850101";
    String SELECT_INS = "A4";
    String DEFAULT_CLA = "00";
    int MIN_APDU_LENGTH = 12;

    /**
     * This method will be called when a command APDU has been received from a remote device. A
     * response APDU can be provided directly by returning a byte-array in this method. In general
     * response APDUs must be sent as quickly as possible, given the fact that the user is likely
     * holding his device over an NFC reader when this method is called.
     *
     * <p class="note">If there are multiple services that have registered for the same AIDs in
     * their meta-data entry, you will only get called if the user has explicitly selected your
     * service, either as a default or just for the next tap.
     *
     * <p class="note">This method is running on the main thread of your application. If you
     * cannot return a response APDU immediately, return null and use the {@link
     * #sendResponseApdu(byte[])} method later.
     *
     * @param apdu The APDU that received from the remote device
     * @param extras A bundle containing extra data. May be null.
     * @return a byte-array containing the response APDU, or null if no response APDU can be sent
     * at this point.
     */
    //The `processCommandApdu` method will be called every time a card reader sends an APDU command that is filtered by our manifest filter.
    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {

        if (apdu == null) {
            return Utils.hexStringToByteArray(STATUS_FAILED);
        }
        int intValue = new BigInteger(apdu).intValue();
        String hexCommandApdu = Utils.toHex(apdu);
        System.out.println("APDU length: " +apdu.length +
                           ", APDU:  " + apdu +
                           " to int: " + intValue +
                           " hexCommand " + hexCommandApdu );
        System.out.println(hexCommandApdu.length());
        System.out.println(hexCommandApdu.substring(10, 24));
        System.out.println(Utils.hexStringToByteArray(STATUS_SUCCESS));
        if (hexCommandApdu.length() < MIN_APDU_LENGTH) {
            return Utils.hexStringToByteArray(STATUS_FAILED);
        }

        if (hexCommandApdu.substring(0, 2) != DEFAULT_CLA) {
            return Utils.hexStringToByteArray(CLA_NOT_SUPPORTED);
        }

        if (hexCommandApdu.substring(2, 4) != SELECT_INS) {
            return Utils.hexStringToByteArray(INS_NOT_SUPPORTED);
        }

        if (hexCommandApdu.substring(10, 24) == AID || hexCommandApdu.substring(10, 24) == AID2)  {
            return Utils.hexStringToByteArray(STATUS_SUCCESS);
        } else {
            return Utils.hexStringToByteArray(STATUS_FAILED);
        }
    }

    /**
     * Called if the connection to the NFC card is lost, in order to let the application know the
     * cause for the disconnection (either a lost link, or another AID being selected by the
     * reader).
     *
     * @param reason Either DEACTIVATION_LINK_LOSS or DEACTIVATION_DESELECTED
     */
    @Override
    public void onDeactivated(int reason) {
        Log.i(TAG, "Deactivated: " + reason);
    }

//     sendResponseApdu();
}
