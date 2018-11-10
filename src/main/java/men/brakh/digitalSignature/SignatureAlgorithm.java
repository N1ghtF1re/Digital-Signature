package men.brakh.digitalSignature;

import java.math.BigInteger;

public interface SignatureAlgorithm {
    BigInteger sign(byte[] message);
}
