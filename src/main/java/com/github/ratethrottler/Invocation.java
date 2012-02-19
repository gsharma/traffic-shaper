package com.github.ratethrottler;

public class Invocation {
    private String invoked;
    private long bound;
    private long window;
    private WindowType windowType;

    public static enum WindowType {
        SECONDS, MINUTES, HOURS
    }

    public Invocation(String invoked, long bound, long window, WindowType windowType) {
        this.invoked = invoked;
        this.bound = bound;
        this.window = window;
        this.windowType = windowType;
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
        builder.append("Invocation [invoked=").append(invoked).append(", bound=").append(bound).append(", window=")
                .append(window).append("]");
        return builder.toString();
    }
}
