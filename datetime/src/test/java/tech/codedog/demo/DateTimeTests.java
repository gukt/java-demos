package tech.codedog.demo;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * @author https://github.com/gukt
 * @version 1.0
 */
public class DateTimeTests {

  @Test
  public void test111() {
    System.out.println(LocalDate.now().atStartOfDay());

    LocalDate date = LocalDate.now();
    LocalDateTime dateTime = LocalDateTime.now();
    //     dateTime.get(ChronoUnit.CENTURIES)

  }

  @Test
  public void test11() {
    LocalDate now = LocalDate.of(2020, 7, 19);
    System.out.println(now.getDayOfWeek()); // SUNDAY
    // 获取本月的第一个星期五
    // 第一个参数：the week within the month, unbounded but typically from -5 to 5
    System.out.println(
        now.with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.FRIDAY))); // 2020-07-03
    // 可以跨月本月到下一个月
    System.out.println(
        now.with(TemporalAdjusters.dayOfWeekInMonth(5, DayOfWeek.SUNDAY))); // 2020-08-02
    // 下个星期五
    System.out.println(now.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))); // 2020-07-31
    // TODO
    System.out.println(now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)));

    // 今年第一天
    System.out.println(now.with(TemporalAdjusters.firstDayOfYear()));
    // 今年最后一天
    System.out.println(now.with(TemporalAdjusters.lastDayOfYear()));
    // 明年第一天
    System.out.println(now.with(TemporalAdjusters.firstDayOfNextYear()));

    // 本月第一天
    System.out.println(now.with(TemporalAdjusters.firstDayOfMonth()));
    // 本月最后一天
    System.out.println(now.with(TemporalAdjusters.lastDayOfMonth()));
    // 下月第一天
    System.out.println(now.with(TemporalAdjusters.firstDayOfNextMonth()));

    // TODO
    // 本周四
    // 上周四
    // 本周第 2 天
    // 上周第 2 天
    // 本月第 2 个周五
    // 本月第 2 个周日
    // 下月第 2 个周五
    // 下月第 2 个周日
    // 下周 5 再加 4 天

    // 今天早上 4 点
    System.out.println(LocalDate.now().atTime(4, 0)); // 2020-07-19T04:00
    System.out.println(LocalDate.now().atTime(LocalTime.of(0, 0)));

    // 时间截取，留下指定的部分，以下是只留下小时
    System.out.println(LocalDateTime.now()); // 2020-07-19T23:01:52.857
    System.out.println(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS)); // 2020-07-19T23:00
    System.out.println(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)); // 2020-07-19T23:01

    // 自定义 TemporalAdjuster
    // https://blog.csdn.net/neweastsun/article/details/88777896

    //    TemporalAdjusters.ofDateAdjuster() // TODO
    //    TemporalAdjusters.lastInMonth() // TODO
  }

  @Test
  public void test1() {

    // 定义指定年与日的时间使用 of 方法
    //    你可以通过静态工厂方法of创建一个 LocalDate 实例,LocalDate实例提供了很多的方法来读取常用的 值,比如年份,月份,星期几,是否闰年等等.

    LocalDate date = LocalDate.of(2020, 1, 1);
    System.out.println(date); // 2020-01-01
    date = LocalDate.of(2020, Month.of(2), 1);
    System.out.println(date); // 2020-02-01
    date = LocalDate.of(2020, Month.MARCH, 1);
    System.out.println(date); // 2020-03-01

    // date = LocalDate.of(2020, 0,1);
    // Invalid value for MonthOfYear (valid values 1 - 12): 0
    // System.out.println(date);

    date = LocalDate.now();
    System.out.println(date); // 2020-07-19

    System.out.println(LocalDate.MAX); // +999999999-12-31
    System.out.println(LocalDate.MIN); // -999999999-01-01

    // TODO
    //    LocalDate.now(ZoneId)
    //    LocalDate.now(Clock)
    //    LocalDate.from(TemporalAccessor)
    //    LocalDate.ofEpochDay()
    //    LocalDate.ofYearDay()
    //    LocalDate.parse()
    //    LocalDate.parse(DateTimeFormatter)

    date = LocalDate.now();
    System.out.println(date); // 2020-07-19
    System.out.println(date.getYear()); // 2020
    System.out.println(date.getMonth()); // JULY，Month 对象
    System.out.println(date.getMonthValue()); // 7
    System.out.println(date.getDayOfWeek()); // SUNDAY TODO
    System.out.println(date.getDayOfMonth()); // 19
    System.out.println(date.getDayOfYear()); // 201
    System.out.println(date.getChronology()); // ISO TODO
    System.out.println(date.getEra()); // CE, LocalDateTime 中没有 getEra  TODO
    //    System.out.println(date.getLong(TemporalField)); // TODO
    //    date.isBefore()
    //    date.isAfter()
    //    date.isEqual()
    //    date.isSupported()

    System.out.println(date.lengthOfYear()); // 366
    System.out.println(date.lengthOfMonth()); // 31

    LocalDate dt1 = LocalDate.of(2020, 1, 2);
    LocalDate dt2 = LocalDate.of(2020, 1, 3);
    LocalDate dt3 = LocalDate.of(2020, 1, 2);

    System.out.println(dt1.isAfter(dt2)); // false
    System.out.println(dt1.isBefore(dt2)); // true
    System.out.println(dt1.isEqual(dt3)); // true

    // 是否闰年
    System.out.println(date.isLeapYear()); // true

    System.out.println(LocalDate.parse("2020-01-01")); // 2020-01-01
    System.out.println(LocalTime.parse("01:02:03")); // 01:02:03
    //    System.out.println(LocalTime.parse("1:02:03")); // Text '1:02:03' could not be parsed at
    // index 0

    LocalTime time3 = LocalTime.parse("1-02-03", DateTimeFormatter.ofPattern("H-mm-ss"));
    System.out.println(time3); // 01:02:03
    LocalDateTime dt4 =
        LocalDateTime.parse(
            "2020/01/01 1-02-03",
            DateTimeFormatter.ofPattern("yyyy/MM/dd H-mm-ss")); // 2020-01-01T01:02:03
    System.out.println(dt4);
  }

  @Test
  public void test2() {
    //    Period period = Period.of(1, 2, 3);
    //    System.out.println(period);

    // 今天零点
    LocalDateTime now = LocalDateTime.now(); // 2020-07-21T22:24:45.616
    LocalDateTime startOfToday = LocalDate.now().atStartOfDay();

    //
    LocalDateTime t1 =
        now.plus(1, ChronoUnit.YEARS)
            .plus(2, ChronoUnit.MONTHS)
            .plus(3, ChronoUnit.DAYS)
            .plus(4, ChronoUnit.HOURS)
            .plus(5, ChronoUnit.MINUTES)
            .plus(6, ChronoUnit.SECONDS);

    LocalDateTime t2 =
        now.plus(Period.ofYears(1))
            .plus(Period.ofMonths(2))
            .plus(Period.ofDays(3))
            .plus(Duration.ofHours(4))
            .plus(Duration.ofMinutes(5))
            .plus(Duration.ofSeconds(6));

    ChronoUnit.HOURS.addTo(now, 1);

    LocalDateTime t3 =
        now.plusYears(1).plusMonths(2).plusDays(3).plusHours(4).plusMinutes(5).plusSeconds(6L);

    System.out.println(t1.equals(t2));
    System.out.println(t2.equals(t3));
    System.out.println(t1.equals(t3));

    //    now.plus(Duration.ofHours(1)).plus(Duration.)

    System.out.println(now);
    System.out.println(startOfToday);
    System.out.println(ChronoUnit.MILLIS.between(startOfToday, now)); //
    System.out.println(ChronoUnit.DAYS.between(startOfToday, now));
    System.out.println(ChronoUnit.MILLIS.getDuration()); // PT0.001S
  }

  @Test
  public void test3() {
    LocalDate start = LocalDate.of(2018, 5, 15);
    LocalDate end = LocalDate.of(2020, 7, 19);

    System.out.println(start); // 2018-05-15
    System.out.println(end); // 2020-07-19

    Period period = Period.between(start, end);
    System.out.println(period); // P2Y2M4D
    System.out.println(period.getYears()); // 2
    System.out.println(period.getMonths()); // 2
    System.out.println(period.getDays()); // 4
    System.out.println(period.getUnits()); // [Years, Months, Days]
    System.out.println(period.getChronology()); // ISO
    System.out.println(period.isNegative()); // false TODO
    System.out.println(period.isZero()); // false TODO
    //    System.out.println(period.get(TemporalUnit)); // TODO
    //    System.out.println(period.addTo()); // TODO
    System.out.println(period.minusYears(1)); // P1Y2M4D
    System.out.println(period.minusMonths(1)); // P2Y1M4D
    System.out.println(period.minusDays(1)); // P2Y2M3D
    //    System.out.println(period.minus(TemporalAmount));
  }

  @Test
  public void test4() {
    //    TemporalUnit unit =
  }
}
