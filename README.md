# Service Rate Throttler

## Overview
The Service Rate-throttler allows for customizable time-windowed rate throttling of any invocations (service/method/resource) in a selectable time-unit (hour/minute/second). An example use would be say we want to generate an event if our subscription service receives 20000 new user subscription messages in 3 seconds. Since throttling is in-memory but at the same time, we care for data reliability, there's full state-snapshotting available on-demand or pre-shutdown; similarly state-reconstruction happens at boot-time but is also available on-demand.  

## Getting Started
Either build from source <code>mvn clean install</code> or download the latest and greatest jar (https://github.com/gsharma/service-ratethrottler/downloads)  

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

## Contributions
Fork, spoon, knive the project as you see fit (: Pull requests with bug fixes are very welcome. If you encounter an issue and do not have the time to submit a patch, please log a [Github Issue](https://github.com/gsharma/service-ratethrottler/issues) against the project.  

## License
MIT License - Copyright (c) 2012 Gaurav Sharma  
