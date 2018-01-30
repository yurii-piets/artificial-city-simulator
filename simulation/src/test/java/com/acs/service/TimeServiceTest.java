package com.acs.service;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class TimeServiceTest {

    @Test
    public void whenTimeDiffOneHour() throws Exception {
        TimeService timeService = new TimeService();
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        ReflectionTestUtils.setField(timeService, "startDateTime", startDate, LocalDateTime.class);

        assertEquals(1, timeService.countTimeDifference(), 0.1);
    }

    @Test
    public void whenTimeDiff13Hour() throws Exception {
        TimeService timeService = new TimeService();
        LocalDateTime startDate = LocalDateTime.now().minusHours(13);
        ReflectionTestUtils.setField(timeService, "startDateTime", startDate, LocalDateTime.class);

        assertEquals(13, timeService.countTimeDifference(), 0.1);
    }

    @Test
    public void whenTimeDiff24Hour() throws Exception {
        TimeService timeService = new TimeService();
        LocalDateTime startDate = LocalDateTime.now().minusHours(24);
        ReflectionTestUtils.setField(timeService, "startDateTime", startDate, LocalDateTime.class);

        assertEquals(24, timeService.countTimeDifference(), 0.1);
    }

    @Test
    public void whenTimeDiffDay25Hour() throws Exception {
        TimeService timeService = new TimeService();
        LocalDateTime startDate = LocalDateTime.now().minusHours(25);
        ReflectionTestUtils.setField(timeService, "startDateTime", startDate, LocalDateTime.class);

        assertEquals(25, timeService.countTimeDifference(), 0.1);
    }

    @Test
    public void whenTimeDiffDay49Hour() throws Exception {
        TimeService timeService = new TimeService();
        LocalDateTime startDate = LocalDateTime.now().minusHours(49);
        ReflectionTestUtils.setField(timeService, "startDateTime", startDate, LocalDateTime.class);

        assertEquals(49, timeService.countTimeDifference(), 0.1);
    }

    @Test
    public void whenTimeDiffDay100Hour() throws Exception {
        TimeService timeService = new TimeService();
        LocalDateTime startDate = LocalDateTime.now().minusHours(100);
        ReflectionTestUtils.setField(timeService, "startDateTime", startDate, LocalDateTime.class);

        assertEquals(100, timeService.countTimeDifference(), 0.1);
    }

    @Test
    public void whenTimeDiffDay2_5Hour() throws Exception {
        TimeService timeService = new TimeService();
        LocalDateTime startDate = LocalDateTime.now().minusHours(2).minusMinutes(30);
        ReflectionTestUtils.setField(timeService, "startDateTime", startDate, LocalDateTime.class);

        assertEquals(2.5, timeService.countTimeDifference(), 0.1);
    }

    @Test
    public void whenTimeDiffDay33_3Hour() throws Exception {
        TimeService timeService = new TimeService();
        LocalDateTime startDate = LocalDateTime.now().minusHours(33).minusMinutes(20);
        ReflectionTestUtils.setField(timeService, "startDateTime", startDate, LocalDateTime.class);

        assertEquals(33.3, timeService.countTimeDifference(), 0.1);
    }
}
