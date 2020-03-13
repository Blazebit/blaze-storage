package com.blazebit.storage.rest.impl;

public final class HexUtils {

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private HexUtils() {
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if ((len & 1) != 0) {
            throw new IllegalArgumentException("Invalid hex code. Expected even number of characters!");
        }
        byte[] data = new byte[len >> 1];
        for (int i = 0; i < len; i++) {
            int charIdx = i << 1;
            int c1 = Character.digit(s.charAt(charIdx), 16);
            int c2 = Character.digit(s.charAt(charIdx + 1), 16);
            if (c1 == -1) {
                throw new IllegalArgumentException("Invalid non-hex character '" + c1 + "' at index: " + charIdx);
            }
            if (c2 == -1) {
                throw new IllegalArgumentException("Invalid non-hex character '" + c2 + "' at index: " + (charIdx + 1));
            }
            data[i] = (byte) ((c1 << 4) + c2);
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
