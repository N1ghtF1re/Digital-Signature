package men.brakh.digitalSignature;


import java.math.BigInteger;

/**
 * Math package for digital signature
 * @author Pankratiew Alexandr
 */

public class DigitalSignatureMath {

    /**
     * Calculating the square root of BigInteger number
     * @param x Number
     * @return sqrt(x)
     */
    public static BigInteger sqrt(BigInteger x) {
        BigInteger div = BigInteger.ZERO.setBit(x.bitLength()/2);
        BigInteger div2 = div;
        // Loop until we hit the same value twice in a row, or wind
        // up alternating.
        for(;;) {
            BigInteger y = div.add(x.divide(div)).shiftRight(1);
            if (y.equals(div) || y.equals(div2))
                return y;
            div2 = div;
            div = y;
        }
    }

    /**
     * Modular exponentiation
     *
     * @param a number
     * @param z power
     * @param m module
     * @return a^z mod m
     */
    public static BigInteger power(BigInteger a, BigInteger z, BigInteger m) {
        BigInteger a1 = a;
        BigInteger z1 = z;
        BigInteger x = BigInteger.ONE;
        while (!z1.equals(BigInteger.ZERO)) { // if z1 != 0
            while (z1.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) { // z % 2 = 0
                z1 = z1.divide(BigInteger.valueOf(2)); // z1 = z1 /2
                a1 = a1.multiply(a1).mod(m);  //a1 = (a1 * a1) % m;
            }
            z1 = z1.add(BigInteger.valueOf(-1)); // z1 = z1 - 1;
            x = x.multiply (a1).mod(m); // x =(x * a1) % m;
        }
        return x;
    }

    /**
     * Check for prime numbers
     * @param n number
     * @return true if number prime
     */
    public static Boolean isPrime(BigInteger n) {
        BigInteger lessOne = n.subtract(BigInteger.ONE);
        // get the next prime from one less than number and check with the number
        return lessOne.nextProbablePrime().compareTo(n) == 0;
    }

    /**
     * Calculate greatest common divisor
     * @param a first number
     * @param b second number
     * @return greatest common divisor of a and b
     */
    public static BigInteger gcd(BigInteger a, BigInteger b){
        return a.gcd(b);
    }


    /**
     * Advanced Euclidean algorithm. Search for coefficients x1, y1, d1 in the equation:
     * x1 * a + y1 * b = d1 (d1 = GCD(a,b))
     * @param a first number
     * @param b second number
     * @return ARRAY: [0] => x1, [1] => y1, [2] => d1
     */
    public static BigInteger[] advancedEuclideanAlgorithm(BigInteger a, BigInteger b) {
        BigInteger d0 = a;
        BigInteger d1 = b;
        BigInteger x0 = BigInteger.ONE;
        BigInteger x1 = BigInteger.ZERO;
        BigInteger y0 = BigInteger.ZERO;
        BigInteger y1 = BigInteger.ONE;


        while(d1.compareTo(BigInteger.ONE) > 0) { //  IF d1 > 1
            BigInteger q = d0.divide(d1); // q = d0/d1
            BigInteger d2 = d0.mod(d1); // d2 = d0 % d1;
            BigInteger x2 = x0.subtract(q.multiply(x1)); //x2 = x0 - q*x1;
            BigInteger y2 = y0.subtract(q.multiply(y1));  // y2 = y0 - q*y1;
            d0 = d1;
            d1 = d2;
            x0 = x1;
            x1 = x2;
            y0 = y1;
            y1 = y2;
        }
        return new BigInteger[] {x1,y1,d1};
    }

    /**
     * Find multiplicative inverse number
     * @param number The number for which you need to find multiplicatively inverse
     * @param phi Euler function from modulo
     * @return multiplicative inverse number
     */
    public static BigInteger getMultiplicativelyInverse(BigInteger number, BigInteger phi) {
        if(!gcd(number, phi).equals(BigInteger.ONE)) throw new ArithmeticException("Euler's function and number must be mutually simple.\n");
        if(number.compareTo(phi) > 0) throw  new ArithmeticException("Number >= phi");


        BigInteger[] coefs = advancedEuclideanAlgorithm(phi, number);
        BigInteger y1 = coefs[1];
        while(y1.compareTo(BigInteger.ZERO) < 0) {
            y1 = y1.add(phi);
        }

        return y1;
    }

    
}
