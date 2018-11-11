package men.brakh.digitalSignature.rsa;

import java.math.BigInteger;

/**
 * PRIVATE KEY
 * D : PRIVATE EXPONENT
 * R : P * Q
 */
public class RSAPrivateKey {
    private BigInteger d;
    private BigInteger r;

    public BigInteger getD() {
        return d;
    }

    public BigInteger getR() {
        return r;
    }

    public RSAPrivateKey(BigInteger d, BigInteger r) {
        this.d = d;
        this.r = r;
    }

}
