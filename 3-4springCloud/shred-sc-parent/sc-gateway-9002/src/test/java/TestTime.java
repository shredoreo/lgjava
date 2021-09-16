import org.junit.Test;

import java.time.ZonedDateTime;

public class TestTime {

    @Test
    public void testTime(){
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println(now);
    }
}
