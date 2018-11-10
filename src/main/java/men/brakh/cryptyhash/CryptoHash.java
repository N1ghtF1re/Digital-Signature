package men.brakh.cryptyhash;

public interface CryptoHash {
    String getHash(byte[] msg);
    int getIntHash(byte[] msg);
}
