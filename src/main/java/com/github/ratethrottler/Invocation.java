package com.github.ratethrottler;

import java.util.UUID;

public final class Invocation {
  private final String invoked;
  private final long bound;
  private final long window;
  private final WindowType windowType;
  private final String id;

  public static enum WindowType {
    SECONDS, MINUTES, HOURS
  }

  public static enum State {
    SETUP, PURGE, DROP
  }

  public Invocation(String invoked, long bound, long window, WindowType windowType) {
    this.id = UUID.randomUUID().toString();
    this.invoked = invoked;
    this.bound = bound;
    this.window = window;
    this.windowType = windowType;
  }

  public String getId() {
    return id;
  }

  public String getInvoked() {
    return invoked;
  }

  public long getBound() {
    return bound;
  }

  public long getWindow() {
    return window;
  }

  public WindowType getWindowType() {
    return windowType;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Invocation [id=").append(id).append(", invoked=").append(invoked)
        .append(", bound=").append(bound).append(", window=").append(window).append("]");
    return builder.toString();
  }
}
