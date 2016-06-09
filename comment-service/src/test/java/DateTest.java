import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by HSH on 16. 6. 9..
 */
public class DateTest {

    private final static Logger logger = LoggerFactory.getLogger(DateTest.class);

    @Test
    public void date() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        logger.info("date.getTime(): " + date.getTime() / 1000);
        logger.info("date.getHours(): " + date.getHours());
        logger.info("c.getTime(): " + c.getTime());
        logger.info("c.getWeeksInWeekYear(): " + c.getWeeksInWeekYear());
        c.setTime(date);
        Date date2 = new Date();
        date2.setTime(date.getTime() - 3600000L);
        logger.info("date.getTime(): " + date2.getTime());
        logger.info("date.getHours(): " + date2.getHours());
        logger.info("c.getTime(): " + c.getTime());
        logger.info("c.getWeeksInWeekYear(): " + c.getWeeksInWeekYear());
    }
}
