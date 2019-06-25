# Traffic Shaper

## Overview
The Traffic Shaper allows for customizable time-windowed rate throttling of any invocations (service/method/resource) in a selectable time-unit (hour/minute/second). An example use would be say we want to generate an event if our subscription service receives 20000 new user subscription messages in 3 seconds. Since throttling is in-memory but at the same time, we care for data reliability, there's full state-snapshotting available on-demand or pre-shutdown; similarly state-reconstruction happens at boot-time but is also available on-demand.  


## Quick Start
Assuming you dropped the ratethrottler jar in your classpath:  
1. Create an instance <code>ServiceRateThrottler throttler = new ServiceRateThrottler();</code>  
2. Create an invocation <code>Invocation invocation = new Invocation("dispatchPayment", 50000L, 5L, WindowType.SECONDS);</code>  
3. Register invocation's interest with the throttler <code>throttler.setupInvocationThrottler(invocation);</code>  
4. Tick the event at every invocation and watch outcome <code>boolean limitReached = throttler.throttle(invocation);</code>  

## API
### 1. Setup invocation throttler
<code>void setupInvocationThrottler(final Invocation invocation)</code>

### 2. Tick invocation and signal throttling outcome
<code>boolean throttle(final Invocation invocation)</code>

### 3. Purge previous recordings for invocation
<code>void purgeInvocationThrottler(final Invocation invocation)</code>

### 4. Drop throttler
<code>void dropInvocationThrottler(final Invocation invocation)</code>

### 5. Count active throttlers
<code>int reportActiveThrottlerCount()</code>

### 6. Check existence of throttler for invocation
<code>boolean existsInvocationThrottler(final Invocation invocation)</code>

### 7. Purge all runtime throttling state
<code>void purgeAllState()</code>

### 8. Snaphsot all runtime throttling state to bson
<code>String takeSnapshot()</code>

### 9. Reconstruct throttling state from a previous snapshot
<code>void reconstructFromSnapshot(String bsonSnapshot)</code>  
 

## License
MIT License - Copyright (c) 2012 Gaurav Sharma  
