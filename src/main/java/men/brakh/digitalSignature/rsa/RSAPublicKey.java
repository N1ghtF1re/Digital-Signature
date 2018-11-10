package men.brakh.digitalSignature.rsa;

import java.math.BigInteger;

public class RSAPublicKey {
    private BigInteger e;
    private BigInteger r;

    public RSAPublicKey(BigInteger e, BigInteger r) {
        this.e = e;
        this.r = r;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getR() {
        return r;
    }
}
