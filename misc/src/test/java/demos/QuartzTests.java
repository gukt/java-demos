package demos;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

public class QuartzTests {

    @Slf4j
    public static class HelloJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            // Say Hello to the World and display the date/time
            log.info("Hello World! - " + new Date());
        }
    }
}
