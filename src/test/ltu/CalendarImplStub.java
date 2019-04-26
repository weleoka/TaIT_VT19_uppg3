package ltu;

import java.util.Calendar;
import java.util.Date;

/**
 * A stub for providing different months of the year.
 *
 * Takes an integer that sets the current month.
 *
 * todo Implement for multiple years, not only 2016.
 */
public class CalendarImplStub  extends CalendarImpl implements ICalendar {
    Calendar.Builder calBuilder = new Calendar.Builder();
    Calendar cal;

    public CalendarImplStub(int month) {
        calBuilder.setTimeOfDay(01, 01, 01);
        calBuilder.setDate(2016, month, 1);
        cal = calBuilder.build();
    }

    @Override
    public Date getDate() {

        return cal.getTime();
    }

}
