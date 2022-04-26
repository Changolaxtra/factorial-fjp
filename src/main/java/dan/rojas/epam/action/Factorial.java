package dan.rojas.epam.action;

import java.math.BigInteger;

public class Factorial {

  private final static BigInteger TWO = BigInteger.valueOf(2);

  public static BigInteger compute(BigInteger number) {
    if (number.compareTo(TWO) <= 0) {
      return number;
    }
    return number.multiply(compute(number.subtract(BigInteger.ONE)));
  }
}
