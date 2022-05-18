package com.cct.group010.finalproject.apduhost;

public class Utils {

    private static String HEX_CHARS = "0123456789ABCDEF";
    private static char[] HEX_CHARS_ARRAY = HEX_CHARS.toCharArray();

    //transform a string with hex characteres in bytes
    public static byte[] hexStringToByteArray(String data){
        byte[] result = new byte[data.length() /2];

        for(int i =0; i < data.length(); i+=2){
            int firstIndex = HEX_CHARS.indexOf(data.charAt(i));
            int secondIndex = HEX_CHARS.indexOf(data.charAt(i + 1));
            ///here we use some bitwise operators to make the conversion
            int octet = firstIndex << 4 | secondIndex;
            result[i >> 1] = (byte)octet;
        }

        return result;
    }
    //transfirm a byte array in hexdecimal string
    public static String toHex(byte[] byteArray) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++){
            int octet = (int) byteArray[i];
            ///here we use some bitwise operators to make the conversion
            int firstIndex = (octet & 0xF0) >>> 4;
            int secondIndex = octet & 0x0F;
            result.append(HEX_CHARS_ARRAY[firstIndex]);
            result.append(HEX_CHARS_ARRAY[secondIndex]);

        }

        return result.toString();
    }

}
