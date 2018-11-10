package men.brakh.digitalSignature;


/**
 * Math package for digital signature
 * @author Pankratiew Alexandr
 */

public class DigitalSignatureMath {

    /**
     * Modular exponentiation
     *
     * @param a number
     * @param z power
     * @param m module
     * @return a^z mod m
     */
    public static long power(long a, long z, long m) {
        long a1 = a;
        long z1 = z;
        long x = 1;
        while (z1 != 0) {
            while (z1 % 2 == 0) {
                z1 /= 2;
                a1 = (a1 * a1) % m;
            }
            z1 = z1 - 1;
            x = (x * a1) % m;
        }
        return x;
    }

    /**
     * Check for prime numbers
     * @param x number
     * @return true if number prime
     */
    public static Boolean isPrime(long x) {
        for(long i=2;i<=Math.sqrt(x);i++)
            if(x%i==0)
                return false;
        return true;
    }

    /**
     * Calculate greatest common divisor
     * @param a first number
     * @param b second number
     * @return greatest common divisor of a and b
     */
    public static long gcd(long a, long b){
        if(b==0)
            return a;
        return gcd(b, a%b);
    }


    /**
     * Advanced Euclidean algorithm. Search for coefficients x1, y1, d1 in the equation:
     * x1 * a + y1 * b = d1 (d1 = GCD(a,b))
     * @param a first number
     * @param b second number
     * @return ARRAY: [0] => x1, [1] => y1, [2] => d1
     */
    public static int[] advancedEuclideanAlgorithm(int a, int b) {
        int d0 = a;
        int d1 = b;
        int x0 = 1;
        int x1 = 0;
        int y0 = 0;
        int y1 = 1;


        while(d1 > 1) {
            int q = d0 / d1;
            int d2 = d0 % d1;
            int x2 = x0 - q*x1;
            int y2 = y0 - q*y1;
            d0 = d1;
            d1 = d2;
            x0 = x1;
            x1 = x2;
            y0 = y1;
            y1 = y2;
        }
        return new int[] {x1,y1,d1};
    }

    /**
     * Find multiplicative inverse number
     * @param number The number for which you need to find multiplicatively inverse
     * @param phi Euler function from modulo
     * @return multiplicative inverse number
     */
    public static int getMultiplicativelyInverse(int number, int phi) {
        if(gcd(number, phi) != 0) throw new ArithmeticException("Euler's function and number must be mutually simple.\n");
        if(number > phi) throw  new ArithmeticException("Number >= phi");


        int[] coefs = advancedEuclideanAlgorithm(phi, number);
        int y1 = coefs[1];
        while(y1 < 0) {
            y1 += phi;
        }

        return y1;
    }



}
