package com.github.ratethrottler;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ratethrottler.Invocation.WindowType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/wiring.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ServiceRateThrottlerTest {
    @Autowired
    private ServiceRateThrottler rateThrottler;

    @Before
    public void init() {
        BasicConfigurator.configure();
    }

    @Test
    public void testThrottleSeconds() {
        long bound = 50L;
        Invocation i = new Invocation("test", bound, 5L, WindowType.SECONDS);
        rateThrottler.setupInvocationThrottler(i);
        for (int iter = 1; iter <= 100; iter++) {
            if (rateThrottler.throttle(i)) {
                assertTrue(iter >= 50);
            }
        }
    }

    @Test
    public void testThrottleMinutes() {
        long bound = 1000L;
        Invocation i = new Invocation("test", bound, 1L, WindowType.MINUTES);
        rateThrottler.setupInvocationThrottler(i);
        for (int iter = 1; iter <= 2000; iter++) {
            if (rateThrottler.throttle(i)) {
                assertTrue(iter >= 1000);
            }
        }
    }

    @Test
    public void testStateSnapshotting() {
        long bound = 1000L;
        Invocation i = new Invocation("test", bound, 1L, WindowType.MINUTES);
        rateThrottler.setupInvocationThrottler(i);
        for (int iter = 1; iter <= 2000; iter++) {
            if (rateThrottler.throttle(i)) {
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
