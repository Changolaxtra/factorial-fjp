package dan.rojas.epam.action;

import dan.rojas.epam.utils.Tuple;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;


@Slf4j
public class FactorialRecursiveAction extends RecursiveTask<BigInteger> {

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
      log.debug("Computing...");
      for (long number = start; number <= end; number++) {
        result = result.multiply(BigInteger.valueOf(number));
      }
    } else {
      log.debug("Creating subtasks for:" + start + "-" + end);
      final Tuple<FactorialRecursiveAction> subtasks = getSubtasks(start, end);
      subtasks.getFirst().fork();
      subtasks.getSecond().fork();
      result = subtasks.getFirst().join().multiply(subtasks.getSecond().join());
    }
    log.debug("Returning result...");
    return result;
  }

  private Tuple<FactorialRecursiveAction> getSubtasks(final long start, final long end) {
    long pivot = (start + end) / 2;
    return new Tuple<>(new FactorialRecursiveAction(start, pivot), new FactorialRecursiveAction(pivot + 1, end));
  }
}
