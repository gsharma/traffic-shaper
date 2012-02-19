# Service Rate Throttler


## Overview
The Service Rate-throttler allows for customizable time-windowed rate throttling of any invocations (service/method/resource) in a selectable time-unit (hour/minute/second). An example use would be say we want to generate an event if our subscription service receives 20000 new user subscription messages in 3 seconds. Since throttling is in-memory, there's full state-snapshotting available on-demand or pre-shutdown; similarly state-reconstruction happens at boot-time and is also available on-demand.  


## Contributions
Fork, spoon, knive the project as you see fit (: Pull requests with bug fixes are very welcome. If you encounter an issue and do not have the time to submit a patch, please log a [Github Issue](https://github.com/gsharma/service-ratethrottler/issues) against the project.  


## License
MIT License - Copyright (c) 2012 Gaurav Sharma  
