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

    /**
     * Creating an object for the RSA signature
     * @param p prime number
     * @param q prime number
     * @param privateexp private exponent, 1 < p < (p-1)(q-1), gcd(privateexp, (p-1)(q-1)) = 1
     * @param hashFunction The object to get the hash function (Must implement the interface CryptoHash)
     */
    public RSASignature(BigInteger p, BigInteger q, BigInteger privateexp, CryptoHash hashFunction) {
        if(!DigitalSignatureMath.isPrime(p)) throw new ArithmeticException("P should be prime");
        if(!DigitalSignatureMath.isPrime(q)) throw new ArithmeticException("Q should be prime");

        BigInteger r = p.multiply(q); // r = p * q
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));  // phi = (p-1)(q-1)

        if((privateexp.compareTo(BigInteger.ONE) <= 0) || (privateexp.compareTo(phi) >= 0)) throw new ArithmeticException("D should be from 2 to phi(p*q) - 1");
        if(!privateexp.gcd(phi).equals(BigInteger.ONE)) throw new ArithmeticException("D should be mutually simple with phi(p*q)");

        if(r.bitLength() <= hashFunction.getMinBitLength()) throw new ArithmeticException("Insufficient size r(p*q)");

        BigInteger publicexp = DigitalSignatureMath.getMultiplicativelyInverse(privateexp, phi); // publicexp * privateexit == 1
        publicKey = new RSAPublicKey(publicexp, r);
        privateKey = new RSAPrivateKey(privateexp, r);
        this.hashFunction = hashFunction;
    }


    /**
     * Signs the message and returns the digital signature
     * @param message bytes of message
     * @return  digital signature
     */
    @Override
    public BigInteger sign(byte[] message) {
        System.out.println(hashFunction.getHash(message));
        BigInteger hash = hashFunction.getIntHash(message);
        return DigitalSignatureMath.power(hash, privateKey.getD(), privateKey.getR()); // return hash^d mod r
    }

    /**
     * Verifies signature validity
     * @param key public key of the user who should own the signature
     * @param s digital signature of user
     * @param message bytes of message
     * @param hashFunction The object to get the hash function (Must implement the interface CryptoHash)
     * @return True if the signature is correct
     */
    public static boolean checkSignature(RSAPublicKey key, BigInteger s, byte[] message, CryptoHash hashFunction) {
        BigInteger hashFromSign = DigitalSignatureMath.power(s, key.getE(), key.getR());
        BigInteger actualHash = hashFunction.getIntHash(message);
        return hashFromSign.equals(actualHash);
    }
}
