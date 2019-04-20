package ltu;

import org.junit.Test;

import static org.junit.Assert.*;

public class CalendarFactoryTest {

    @Test
    public void getCalendarDefault() {
        assertEquals(CalendarFactory.getCalendar().getClass(), ICalendar.class);
    }


    @Test
    public void getCalendar1() {
    }
}