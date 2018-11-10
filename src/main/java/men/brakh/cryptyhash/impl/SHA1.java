package men.brakh.cryptyhash.impl;

import men.brakh.cryptyhash.CryptoHash;

public class SHA1 implements CryptoHash {

    final int a = 0x67452301;
    final int b = 0xEFCDAB89;
    final int c = 0x98BADCFE;
    final int d = 0x10325476;
    final int e = 0xC3D2E1F0;
    public String getHash(byte[] msg) {
        return null;
    }

    public int getIntHash(byte[] msg) {
        return 0;
    }
}
