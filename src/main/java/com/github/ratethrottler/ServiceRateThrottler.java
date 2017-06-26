package com.github.ratethrottler;

public interface ServiceRateThrottler {
  public void setupInvocationThrottler(final Invocation invocation);

  public void purgeInvocationThrottler(final Invocation invocation);

  public void dropInvocationThrottler(final Invocation invocation);

  public int reportActiveThrottlerCount();

  public boolean existsInvocationThrottler(final Invocation invocation);

  // handle with care
  public void purgeAllState();

  public String takeSnapshot();

  public void reconstructFromSnapshot(String snapshot);

  public boolean throttle(final Invocation invocation);
}
