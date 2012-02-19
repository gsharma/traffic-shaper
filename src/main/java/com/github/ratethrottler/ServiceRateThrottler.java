package com.github.ratethrottler;

public interface ServiceRateThrottler {
    public void setupInvocationLimiter(final Invocation invocation);

    public void purgeInvocationLimiter(final Invocation invocation);

    public void dropInvocationLimiter(final Invocation invocation);

    public int reportActiveLimitersCount();

    public boolean existsInvocationLimiter(final Invocation invocation);

    // handle with care
    public void purgeAllState();

    public String takeSnapshot();

    public void reconstructFromSnapshot(String snapshot);

    public boolean throttle(final Invocation invocation);
}
