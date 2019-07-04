package com.github.trafficshaper;

import com.github.trafficshaper.Invocation.State;

public interface RateThrottler {

  public void setInvocationState(final Invocation invocation, final State state);

  public int reportActiveThrottlerCount();

  public boolean existsInvocationThrottler(final Invocation invocation);

  // handle with care
  public void purgeAllState();

  public String takeSnapshot();

  public void reconstructFromSnapshot(String snapshot);

  public boolean throttle(final Invocation invocation);
}
