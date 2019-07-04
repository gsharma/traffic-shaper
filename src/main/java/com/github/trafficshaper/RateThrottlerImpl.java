package com.github.trafficshaper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.logging.log4j.Logger;

import com.github.trafficshaper.Invocation.State;
import com.github.trafficshaper.Invocation.WindowType;

import org.apache.logging.log4j.LogManager;

public final class RateThrottlerImpl implements RateThrottler {
  private final static Logger logger = LogManager.getLogger(RateThrottlerImpl.class);
  private static final long ONE_NANO_SEC = 1000000000L;
  private final Map<String, LinkedBlockingDeque<Long>> throttlers =
      new HashMap<String, LinkedBlockingDeque<Long>>();

  @Override
  public synchronized void setInvocationState(final Invocation invocation, final State state) {
    logger.info(String.format("Switching invocationThrottler %s to %s state",
        invocation.getInvoked(), state));
    switch (state) {
      case SETUP:
        throttlers.put(invocation.getId(), new LinkedBlockingDeque<Long>());
        break;
      case PURGE:
        throttlers.get(invocation.getId()).clear();
        break;
      case DROP:
        throttlers.remove(invocation.getId());
        break;
    }
  }

  @Override
  public int reportActiveThrottlerCount() {
    if (logger.isDebugEnabled()) {
      logger.debug("Report all active throttlers");
    }
    return throttlers.size();
  }

  @Override
  public boolean existsInvocationThrottler(final Invocation invocation) {
    if (logger.isDebugEnabled()) {
      logger.debug("Check existence of invocationThrottler for " + invocation);
    }
    return throttlers.containsKey(invocation.getId());
  }

  @Override
  public void purgeAllState() {
    if (logger.isDebugEnabled()) {
      logger.debug("Purge all ratethrottler state");
    }
    throttlers.clear();
  }

  @Override
  public synchronized String takeSnapshot() {
    // TODO
    return null;
  }

  @Override
  public synchronized void reconstructFromSnapshot(String snapshot) {
    // TODO
    logger.debug("Reconstruct from snapshot: " + snapshot);
    return;
  }

  @Override
  public boolean throttle(final Invocation invocation) {
    if (logger.isDebugEnabled()) {
      logger.debug("Throttle " + invocation);
    }
    LinkedBlockingDeque<Long> invocationThrottler = throttlers.get(invocation.getId());
    if (invocationThrottler == null) {
      throw new IllegalArgumentException("First configure the invocation before using it");
    }
    boolean limitReached = false;
    long current = System.nanoTime();
    if (invocationThrottler.size() < invocation.getBound()) {
      invocationThrottler.addLast(current);
    } else {
      long eldest = invocationThrottler.getFirst();
      long window = nanosWindow(invocation);
      if ((current - eldest) < window) {
        limitReached = true;
      } else {
        invocationThrottler.removeFirst();
        invocationThrottler.addLast(current);
      }
    }
    return limitReached;
  }

  private long nanosWindow(final Invocation invocation) {
    long windowNanos = 0L;
    WindowType windowType = invocation.getWindowType();
    if (windowType == WindowType.HOURS) {
      windowNanos = invocation.getWindow() * 60 * 60 * ONE_NANO_SEC;
    } else if (windowType == WindowType.MINUTES) {
      windowNanos = invocation.getWindow() * 60 * ONE_NANO_SEC;
    } else if (windowType == WindowType.SECONDS) {
      windowNanos = invocation.getWindow() * ONE_NANO_SEC;
    }
    return windowNanos;
  }

}
