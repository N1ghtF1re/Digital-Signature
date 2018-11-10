package men.brakh.digitalSignature.rsa;

import men.brakh.cryptohash.CryptoHash;
import men.brakh.digitalSignature.DigitalSignatureMath;
import men.brakh.digitalSignature.SignatureAlgorithm;

import java.math.BigInteger;
import java.security.PublicKey;

public class RSASignature implements SignatureAlgorithm {
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    private CryptoHash hashFunction;



    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public CryptoHash getHashFunction() {
        return hashFunction;
    }

    public RSASignature(BigInteger p, BigInteger q, BigInteger privateexp, CryptoHash hashFunction) {
        if(!DigitalSignatureMath.isPrime(p)) throw new ArithmeticException("P should be prime");
        if(!DigitalSignatureMath.isPrime(q)) throw new ArithmeticException("Q should be prime");

        BigInteger r = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));  // phi = (p-1)(q-1)

        if((privateexp.compareTo(BigInteger.ONE) <= 0) || (privateexp.compareTo(phi) >= 0)) throw new ArithmeticException("D should be from 2 to phi(p*q) - 1");
        if(!privateexp.gcd(phi).equals(BigInteger.ONE)) throw new ArithmeticException("D should be mutually simple with phi(p*q)");


        BigInteger publicexp = DigitalSignatureMath.getMultiplicativelyInverse(privateexp, phi); // publicexp * privateexit == 1
        publicKey = new RSAPublicKey(publicexp, r);
        privateKey = new RSAPrivateKey(privateexp, r);
        this.hashFunction = hashFunction;
    }


    @Override
    public BigInteger sign(byte[] message) {
        System.out.println(hashFunction.getHash(message));
        BigInteger hash = hashFunction.getIntHash(message).mod(publicKey.getR());
        return DigitalSignatureMath.power(hash, privateKey.getD(), privateKey.getR()); // return hash^d mod r
    }

    public static boolean checkSignature(RSAPublicKey key, BigInteger s, byte[] message, CryptoHash hashFunction) {
        BigInteger hashFromSign = DigitalSignatureMath.power(s, key.getE(), key.getR());
        BigInteger actualHash = hashFunction.getIntHash(message).mod(key.getR());
        return hashFromSign.equals(actualHash);
    }
}
