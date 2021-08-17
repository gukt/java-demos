package tech.codedog.guides;

import java.util.Date;

/**
 * ThreadLocalExample class
 *
 * @author https://github.com/gukt
 */
public class ThreadLocalExample {

  private ThreadLocal<Date> startTime1 = new ThreadLocal<>();
  private Date startTime2;

  public void setStartTime(boolean threadLocal) {
    Date now = new Date();
    if (threadLocal) {
      startTime1.set(now);
    } else {
      startTime2 = now;
    }
  }

  public Date getStartTime(boolean threadLocal) {
    return threadLocal ? startTime1.get() : startTime2;
  }

  public static void main(String[] args) throws InterruptedException {
    ThreadLocalExample example = new ThreadLocalExample();
    example.setStartTime(true);

    // Main thread sleeps 1000 millis
    Thread.sleep(1000);

    // Thread t1 starts
    Thread t1 = new Thread(() -> example.setStartTime(true));
    t1.start();

    System.out.println(example.startTime1);
  }
}
