package men.brakh.cryptohash.impl;

import org.junit.Test;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class SHA1Test {
    @Test
    public void emptyStringTest() {
        String str = "";
        assertEquals(getReceivedHexValue(str),getActualHexValue(str));
        assertEquals(getReceivedBIValue(str), getActualBIValue(str));
    }

    @Test
    public void someStringTest() {
        String[] strings = new String[] {"A", "B", "1 = 2+4", "SA", "SHA", "SHA-1", "he quick brown fox jumps over the lazy dog"};
        for(int i = 0; i < strings.length; i++) {
            String str = strings[i];
            assertEquals(getReceivedHexValue(str),getActualHexValue(str));
            assertEquals(getReceivedBIValue(str), getActualBIValue(str));
        }
    }

    @Test
    public void bigStringTest() {
        StringBuilder str_sb = new StringBuilder();
        for(int i = 0; i < 1235; i++) {
            str_sb.append((char) (((int)'a' + i % (int)'z') + (int) 'a') );
        }
        String str = str_sb.toString();
        assertEquals(getReceivedHexValue(str),getActualHexValue(str));
        assertEquals(getReceivedBIValue(str), getActualBIValue(str));
    }

    private BigInteger getReceivedBIValue(String value) {
        SHA1 sha1 = new SHA1();
        Charset cs = Charset.forName("UTF-8");
        byte[] data = value.getBytes(cs);
        return sha1.getIntHash(data);
    }

    public BigInteger getActualBIValue(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(value.getBytes(StandardCharsets.UTF_8));
            byte[] actual = md.digest();
            return new BigInteger(1, actual);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getReceivedHexValue(String value) {
        SHA1 sha1 = new SHA1();
        Charset cs = Charset.forName("UTF-8");
        byte[] data = value.getBytes(cs);
        return sha1.getHash(data);
    }

    private String getActualHexValue(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(value.getBytes(StandardCharsets.UTF_8));
            byte[] actual = md.digest();
            int actual_len = actual.length;
            StringBuilder actual_hex = new StringBuilder(actual.length * 2);
            for (int i = 0 ; i < actual_len ; i++) {
                actual_hex.append(String.format("%02X", actual[i]));
            }
            return actual_hex.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}