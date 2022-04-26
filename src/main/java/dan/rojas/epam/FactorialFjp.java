package dan.rojas.epam;

import dan.rojas.epam.action.Factorial;
import dan.rojas.epam.action.FactorialRecursiveAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class FactorialFjp {

  private final static Logger logger = LoggerFactory.getLogger(FactorialFjp.class);
  public static final long NUMBER = 10000;

  public static void main(String[] args) {
    final BigInteger forkJoinResult = forkJoinCompute();
    final BigInteger sequentialResult = sequentialCompute();

    if (Objects.nonNull(forkJoinResult) && Objects.nonNull(sequentialResult)) {
      if (forkJoinResult.equals(sequentialResult)) {
        logger.info("Same results: " + forkJoinResult.toString().substring(0, 120) + "...");
      }
    }
  }

  private static BigInteger forkJoinCompute() {
    try {
      long startMillis = System.currentTimeMillis();
      final ForkJoinPool forkJoinPool = new ForkJoinPool(8);
      final Future<BigInteger> result = forkJoinPool.submit(new FactorialRecursiveAction(1, NUMBER));
      final BigInteger factorial = result.get();
      logger.info("ForkJoin calculated in " + (System.currentTimeMillis() - startMillis) + "ms");
      return factorial;
    } catch (InterruptedException | ExecutionException e) {
      logger.error("ForkJoin: Error getting the results:" + e.getMessage());
      return null;
    }
  }

  private static BigInteger sequentialCompute() {
    try {
      long startMillis = System.currentTimeMillis();
      BigInteger factorial = BigInteger.ONE;
      for (long number = 1; number <= NUMBER; number++) {
        factorial = factorial.multiply(BigInteger.valueOf(number));
      }
      logger.info("Sequential calculated in " + (System.currentTimeMillis() - startMillis) + "ms");
      return factorial;
    } catch (Exception e) {
      logger.error("Sequential: Error getting the results:" + e.getMessage());
      return null;
    }
  }

  private static BigInteger recursiveCompute() {
    try {
      long startMillis = System.currentTimeMillis();
      final BigInteger factorial = Factorial.compute(BigInteger.valueOf(NUMBER));
      logger.info("Recursive calculated in " + (System.currentTimeMillis() - startMillis) + "ms");
      return factorial;
    } catch (Exception e) {
      logger.error("Recursive: Error getting the results:" + e.getMessage());
      return null;
    }
  }

}
