package men.brakh.cryptohash.impl;

import men.brakh.cryptohash.CryptoHash;

import java.math.BigInteger;


public class SHA1 implements CryptoHash {

    /**
     * Basic initialization of 512-bit blocks (Supplement 0, 1 and text length):
     *  Append the bit '1' to the message e.g. by adding 0x80 if message length is a multiple of 8 bits.
     *  Append 0 ≤ k < 512 bits '0', such that the resulting message length in bits is congruent to −64 ≡ 448 (mod 512)
     *  Append ml, the original message length, as a 64-bit big-endian integer Thus, the total length is a multiple of 512 bits.
     *  @param data Array of message's bytes
     * @return Formed array
     */
    private byte[] initializeBlocks(byte[] data) {
        int defaultLength = data.length;	// length in bytes
        long defaultBirsLength = defaultLength * 8;	// length in bits

        byte[] with_one = new byte[defaultLength+1];
        System.arraycopy(data, 0, with_one, 0, defaultLength);
        with_one[with_one.length - 1] = (byte) 0x80;	// append 1
        int new_length = with_one.length*8;		// get new length in bits

        // find length multiple of 512
        while (new_length % 512 != 448) {
            new_length += 8;
        }

        // size of block with appended zeros
        byte[] with_zeros = new byte[new_length/8];
        System.arraycopy(with_one, 0 , with_zeros, 0, with_one.length);

        // add 64 bits for original length
        byte[] output = new byte[with_zeros.length + 8];
        for (int i = 0; i < 8; i++) {
            output[output.length -1 - i] = (byte) ((defaultBirsLength >>> (8 * i)) & 0xFF);
        }
        System.arraycopy(with_zeros, 0 , output, 0, with_zeros.length);

        return output;
    }

    /**
     * Hash the byte array
     * @param data Array of message's bytes
     * @return Array of hash's bytes
     */
    private byte[] encode(byte[] data) {
        byte[] output = initializeBlocks(data);

        int size = output.length;
        int blocksCount = size * 8 /512;

        int h0 = 0x67452301;
        int h1 = 0xEFCDAB89;
        int h2 = 0x98BADCFE;
        int h3 = 0x10325476;
        int h4 = 0xC3D2E1F0;

        // hash each successive 512 block
        for (int i = 0; i < blocksCount; i++) {
            int[] w = new int[80];

            // First 16 words == first 16 words in block
            for (int j = 0; j < 16; j++) {
                w[j] =  ((output[i*512/8 + 4*j] << 24) & 0xFF000000) | ((output[i*512/8 + 4*j+1] << 16) & 0x00FF0000);
                w[j] |= ((output[i*512/8 + 4*j+2] << 8) & 0xFF00) | (output[i*512/8 + 4*j+3] & 0xFF);
            }

            // extend 16 words into 80 words
            for (int j = 16; j < 80; j++) {
                w[j] = left_rotate(w[j-3] ^ w[j-8] ^ w[j-14] ^ w[j-16], 1);
            }

            // initialize initial values
            int a = h0;
            int b = h1;
            int c = h2;
            int d = h3;
            int e = h4;
            int f = 0;
            int k = 0;

            //main loop
            for (int j = 0; j < 80; j++)
            {
                if (j <= 19) {
                    f = (b & c) | ((~b) & d);
                    k = 0x5A827999;
                }
                else if(j <= 39) {
                    f = b ^ c ^ d;
                    k = 0x6ED9EBA1;
                }
                else if(j <= 59) {
                    f = (b & c) | (b & d) | (c & d);
                    k = 0x8F1BBCDC;
                }
                else {
                    f = b ^ c ^ d;
                    k = 0xCA62C1D6;
                }

                int temp = left_rotate(a, 5) + f + e + k + w[j];
                e = d;
                d = c;
                c = left_rotate(b, 30);
                b = a;
                a = temp;
            }

            //add chunk's hash to result
            h0 = h0 + a;
            h1 = h1 + b;
            h2 = h2 + c;
            h3 = h3 + d;
            h4 = h4 + e;
        }

        // RESULT:
        byte[] hash = new byte[20];
        for (int j = 0; j < 4; j++) {
            hash[j] = (byte) ((h0 >>> 24-j*8) & 0xFF);

        }
        for (int j = 0; j < 4; j++) {
            hash[j+4] = (byte) ((h1 >>> 24-j*8) & 0xFF);
        }
        for (int j = 0; j < 4; j++) {
            hash[j+8] = (byte) ((h2 >>> 24-j*8) & 0xFF);
        }
        for (int j = 0; j < 4; j++) {
            hash[j+12] = (byte) ((h3 >>> 24-j*8) & 0xFF);

        }
        for (int j = 0; j < 4; j++) {
            hash[j+16] = (byte) ((h4 >>> 24-j*8) & 0xFF);
        }

        return hash;

    }

    /**
     * Performs a circular left shift
     * @param n The number to shift
     * @param d The number of shift bits
     * @return n rol d
     */
    private static int left_rotate(int n, int d) {
        return (n << d) | (n >>> (32 - d));
    }


    /**
     * Get hash in BigInteger format
     * @param msg Array of message's bytes
     * @return  hash in BigInteger format
     */
    public BigInteger getIntHash(byte[] msg) {
        byte[] hash = encode(msg);

        return new BigInteger(1, hash);
    }

    /**
     * Get hash in hex string format
     * @param msg Array of message's bytes
     * @return  hash in hex string format
     */
    public String getHash(byte[] msg) {
        byte[] hash = encode(msg);

        StringBuilder hex = new StringBuilder(hash.length * 2);
        int len = hash.length;
        for (int i = 0 ; i < len ; i++) {
            hex.append(String.format("%02X", hash[i]));
        }
        return hex.toString();

    }


}
