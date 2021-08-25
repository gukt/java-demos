package demos;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

@Slf4j
public class QuartzTests {

    @Test
    void testHelloJob() {
        HelloJob job = new HelloJob();
        // TODO: 2021/8/25
    }


    public static class HelloJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            // Say Hello to the World and display the date/time
            log.info("Hello World! - " + new Date());
        }
    }
}
