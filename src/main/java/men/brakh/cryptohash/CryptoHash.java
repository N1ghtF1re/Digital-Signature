package men.brakh.cryptohash;

import java.math.BigInteger;

public interface CryptoHash {
    String getHash(byte[] msg);
    BigInteger getIntHash(byte[] msg);

    int getMinBitLength();
}
