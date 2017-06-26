package com.github.ratethrottler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.github.ratethrottler.Invocation.WindowType;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public final class ServiceRateThrottlerImpl implements ServiceRateThrottler {
  private final static Logger logger = LogManager.getLogger(ServiceRateThrottlerImpl.class);
  private static final long ONE_NANO_SEC = 1000000000L;
  private Map<String, LinkedBlockingDeque<Long>> throttlers =
      new HashMap<String, LinkedBlockingDeque<Long>>();

  @Override
  public void setupInvocationThrottler(final Invocation invocation) {
    logger.debug("Setup invocationThrottler for " + invocation);
    throttlers.put(invocation.getInvoked(), new LinkedBlockingDeque<Long>());
  }

  @Override
  public void purgeInvocationThrottler(final Invocation invocation) {
    logger.debug("Purge invocationThrottler for " + invocation);
    throttlers.get(invocation).clear();
  }

  @Override
  public void dropInvocationThrottler(final Invocation invocation) {
    logger.debug("Drop invocationThrottler for " + invocation);
    throttlers.remove(invocation);
  }

  @Override
  public int reportActiveThrottlerCount() {
    logger.debug("Report all active throttlers");
    return throttlers.size();
  }

  @Override
  public boolean existsInvocationThrottler(final Invocation invocation) {
    logger.debug("Check existence of invocationThrottler for " + invocation);
    return throttlers.containsKey(invocation.getInvoked());
  }

  // handle with care
  @Override
  public void purgeAllState() {
    logger.debug("Purge all service ratethrottler state");
    throttlers.clear();
  }

  // @PreDestroy and push to mongo
  @Override
  public synchronized String takeSnapshot() {
    logger.debug("Take active state snapshot");
    String snapshot = JSON.serialize(throttlers);
    return snapshot;
  }

  @SuppressWarnings("unchecked")
  @Override
  public synchronized void reconstructFromSnapshot(String snapshot) {
    logger.debug("Reconstruct from snapshot: " + snapshot);
    BasicDBObject deserialized = (BasicDBObject) JSON.parse(snapshot);
    if (deserialized == null) {
      return;
    }
    Map<String, BasicDBList> bsonMap = deserialized.toMap();
    throttlers = new HashMap<String, LinkedBlockingDeque<Long>>(bsonMap.size());
    for (Map.Entry<String, BasicDBList> entry : bsonMap.entrySet()) {
      BasicDBList dequeSnapshot = entry.getValue();
      LinkedBlockingDeque<Long> deque = new LinkedBlockingDeque<Long>(dequeSnapshot.size());
      for (Long tstamp : dequeSnapshot.toArray(new Long[dequeSnapshot.size()])) {
        deque.addLast(tstamp);
      }
      throttlers.put(entry.getKey(), deque);
    }
  }

  @Override
  public boolean throttle(final Invocation invocation) {
    logger.debug("Throttle " + invocation);
    LinkedBlockingDeque<Long> invocationThrottler = throttlers.get(invocation.getInvoked());
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
