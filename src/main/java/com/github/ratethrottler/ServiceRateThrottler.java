package com.github.ratethrottler;

import com.github.ratethrottler.Invocation.State;

public interface ServiceRateThrottler {

  public void setInvocationState(final Invocation invocation, final State state);

  public int reportActiveThrottlerCount();

  public boolean existsInvocationThrottler(final Invocation invocation);

  // handle with care
  public void purgeAllState();

  public String takeSnapshot();

  public void reconstructFromSnapshot(String snapshot);

  public boolean throttle(final Invocation invocation);
}
