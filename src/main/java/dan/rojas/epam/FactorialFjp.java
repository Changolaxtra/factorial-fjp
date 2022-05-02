package dan.rojas.epam;

import dan.rojas.epam.action.Factorial;
import dan.rojas.epam.action.FactorialRecursiveAction;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

@Slf4j
public class FactorialFjp {
  
  private static final long NUMBER = 10000;
  private static final int PARALLELISM_LEVEL = 8;

  public static void main(String[] args) {
    final BigInteger forkJoinResult = forkJoinCompute();
    final BigInteger sequentialResult = sequentialCompute();

    if (Objects.nonNull(forkJoinResult) && Objects.nonNull(sequentialResult)) {
      if (forkJoinResult.equals(sequentialResult)) {
        log.info("Same results: " + forkJoinResult.toString().substring(0, 120) + "...");
      }
    }
  }

  private static BigInteger forkJoinCompute() {
    try {
      long startMillis = System.currentTimeMillis();
      final ForkJoinPool forkJoinPool = new ForkJoinPool(PARALLELISM_LEVEL);
      final Future<BigInteger> result = forkJoinPool.submit(new FactorialRecursiveAction(1, NUMBER));
      final BigInteger factorial = result.get();
      log.info("ForkJoin calculated in " + (System.currentTimeMillis() - startMillis) + "ms");
      return factorial;
    } catch (InterruptedException | ExecutionException e) {
      log.error("ForkJoin: Error getting the results:" + e.getMessage());
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
      log.info("Sequential calculated in " + (System.currentTimeMillis() - startMillis) + "ms");
      return factorial;
    } catch (Exception e) {
      log.error("Sequential: Error getting the results:" + e.getMessage());
      return null;
    }
  }
  
  private static BigInteger recursiveCompute() {
    try {
      long startMillis = System.currentTimeMillis();
      final BigInteger factorial = Factorial.compute(BigInteger.valueOf(NUMBER));
      log.info("Recursive calculated in " + (System.currentTimeMillis() - startMillis) + "ms");
      return factorial;
    } catch (Exception e) {
      log.error("Recursive: Error getting the results:" + e.getMessage());
      return null;
    }
  }

}
