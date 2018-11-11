package men.brakh.cryptohash.impl;

import men.brakh.cryptohash.CryptoHash;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 implements CryptoHash {

    @Override
    public String getHash(byte[] msg) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.reset();
        md.update(msg);
        byte[] hashbytes = md.digest();

        StringBuilder hex = new StringBuilder();
        for (int i = 0 ; i < hashbytes.length ; i++) {
            hex.append(String.format("%02X", hashbytes[i]));
        }
        return hex.toString();
    }

    @Override
    public BigInteger getIntHash(byte[] msg) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.reset();
        md.update(msg);
        byte[] hashbytes = md.digest();

        return new BigInteger(1, hashbytes);
    }
}
