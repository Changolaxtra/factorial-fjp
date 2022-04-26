package dan.rojas.epam.action;

import dan.rojas.epam.utils.Tuple;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FactorialRecursiveAction extends RecursiveTask<BigInteger> {

  private final static Logger logger = LoggerFactory.getLogger(FactorialRecursiveAction.class);

  private static final long LIMIT = 50L;
  private final long start;
  private final long end;

  public FactorialRecursiveAction(final long start, final long end) {
    this.start = start;
    this.end = end;
  }

  @Override
  protected BigInteger compute() {
    BigInteger result = BigInteger.ONE;
    if ((end - start) <= LIMIT) {
      logger.debug("Computing...");
      for (long number = start; number <= end; number++) {
        result = result.multiply(BigInteger.valueOf(number));
      }
    } else {
      logger.debug("Creating subtasks for:" + start + "-" + end);
      final Tuple<FactorialRecursiveAction> subtasks = getSubtasks(start, end);
      subtasks.getFirst().fork();
      subtasks.getSecond().fork();
      result = subtasks.getFirst().join().multiply(subtasks.getSecond().join());
    }
    logger.debug("Returning result...");
    return result;
  }

  private Tuple<FactorialRecursiveAction> getSubtasks(final long start, final long end) {
    long pivot = (start + end) / 2;
    return new Tuple<>(new FactorialRecursiveAction(start, pivot), new FactorialRecursiveAction(pivot + 1, end));
  }
}
