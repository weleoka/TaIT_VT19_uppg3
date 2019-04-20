package ltu;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class CalendarFactoryTest {

    @Test
    public void getCalendarDefault() {
        assertThat(CalendarFactory.getCalendar(), instanceOf(ICalendar.class));
    }

    @Test
    public void getCalendarGoodCalendar() {
        assertThat(CalendarFactory.getCalendar("ltu.CalendarImpl"), instanceOf(ICalendar.class));
    }

    @Test
    public void getCalendarBadCalendar() {
        assertNull(CalendarFactory.getCalendar("badClassName"));
    }
}