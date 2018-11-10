package men.brakh.digitalSignature;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class DigitalSignatureMathTest {

    @Test
    public void powerTest() {
        int limit = 150;
        for(int i = 1; i < limit; i++) {
            for(int j = 1; j < limit; j++) {
                for(int k = 1; k < limit; k++) {
                    BigInteger number = BigInteger.valueOf(i);
                    BigInteger exp = BigInteger.valueOf(j);
                    BigInteger modulo = BigInteger.valueOf(k);

                    assertEquals(DigitalSignatureMath.power(number, exp, modulo), number.modPow(exp, modulo));
                }
            }
        }
    }
}