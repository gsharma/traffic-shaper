package com.github.ratethrottler;

import static org.junit.Assert.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.github.ratethrottler.Invocation.State;
import com.github.ratethrottler.Invocation.WindowType;

public class ServiceRateThrottlerTest {
  private ServiceRateThrottler rateThrottler;

  @Before
  public void init() {
    LogManager.getLogger(SimpleLogger.class);
    rateThrottler = new ServiceRateThrottlerImpl();
  }

  @Test
  public void testThrottleSeconds() {
    long bound = 50L;
    Invocation invocation = new Invocation("test", bound, 5L, WindowType.SECONDS);
    rateThrottler.setInvocationState(invocation, State.SETUP);
    for (int iter = 1; iter <= 100; iter++) {
      if (rateThrottler.throttle(invocation)) {
        assertTrue(iter >= 50);
      }
    }
  }

  @Test
  public void testThrottleMinutes() {
    long bound = 1000L;
    Invocation invocation = new Invocation("test", bound, 1L, WindowType.MINUTES);
    rateThrottler.setInvocationState(invocation, State.SETUP);
    for (int iter = 1; iter <= 2000; iter++) {
      if (rateThrottler.throttle(invocation)) {
        assertTrue(iter >= 1000);
      }
    }
  }

  @Ignore
  @Test
  public void testStateSnapshotting() {
    long bound = 1000L;
    Invocation invocation = new Invocation("test", bound, 1L, WindowType.MINUTES);
    rateThrottler.setInvocationState(invocation, State.SETUP);
    for (int iter = 1; iter <= 2000; iter++) {
      if (rateThrottler.throttle(invocation)) {
        assertTrue(iter >= 1000);
      }
    }

    int limitersPresnapshot = rateThrottler.reportActiveThrottlerCount();
    String prePurgeSnapshot = rateThrottler.takeSnapshot();
    rateThrottler.purgeAllState();
    rateThrottler.reconstructFromSnapshot(prePurgeSnapshot);
    assertEquals(limitersPresnapshot, rateThrottler.reportActiveThrottlerCount());
    assertEquals(prePurgeSnapshot, rateThrottler.takeSnapshot());
  }
}
