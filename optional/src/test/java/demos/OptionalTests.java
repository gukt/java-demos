package demos;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author https://github.com/gukt
 * @version 1.0
 */
public class OptionalTests {

  @Getter
  @RequiredArgsConstructor(staticName = "of")
  private static class User {
    @NonNull private String name;
    private String phone;
    private String email;
    private Address address;
  }

  private static class Address {
    @Getter private City city;
  }

  static class City {
    @Getter String name;
  }

  @Test
  public void testMappingAndChaining() {
    User user = User.of("Tom");
    Optional.of(user).map(u -> u.getName().toLowerCase());
    String name =
        Optional.ofNullable(user.getAddress())
            .map(Address::getCity)
            .map(City::getName)
            .map(String::toLowerCase)
            .orElse("unknown");
    System.out.println(name);
  }

  @Test
  public void test1() {
    User user = new User("tom");
    String phone = Optional.of(user).map(User::getPhone).orElse("unknown");
    System.out.println(phone);

    if (user != null) {
      phone = user.getPhone();
      if (phone == null) {
        phone = "unknown";
      }
      System.out.println(phone);
    }
  }

  @Test
  public void test211() {
    //    Optional<String> optional = Optional.empty();
    //
    //    String s = optional.orElse("aaa");
    //    s = optional.orElseGet(() -> "hello");
    //    Optional<String> result = optional.or(() -> Optional.of("hello"));
    //
    //    Stream<String> stream = Optional.of("hello").stream();
    //    //    stream.findAny()
    //    //    stream.findFirst()
    //    // findAny、findFirst、max、min、reduce、
    //    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    Stream<List<Integer>> stream = Optional.of(Arrays.asList(1, 2, 3)).stream();
  }

  /**
   * Optional.empty value = null Method threw 'java.util.NoSuchElementException' exception.
   * result.isPresent() = false result.orElse(1) // 1
   */
  @Test
  public void test2() throws Throwable {
    // Optional 提供三个静态方法 empty、of、ofNullable
    Optional<Integer> optional = Optional.empty();
    System.out.println(optional); // Optional.empty
    Assert.assertFalse(optional.isPresent());
    int n = optional.orElseGet(() -> Math.min(1, 2));
    Assert.assertEquals(1, n);

    Optional<String> result = Optional.of("hello");
    result.ifPresent(System.out::println);

    optional.orElseThrow((Supplier<Throwable>) () -> new RuntimeException("No result."));
  }

  @Test(expected = RuntimeException.class)
  public void testThrowingExceptionIfAbsent() throws Throwable {
    Optional optional = Optional.empty();
    optional.orElseThrow((Supplier<Throwable>) () -> new RuntimeException("No result."));
  }

  @Test
  public void test22() {
    //        Optional<Integer> result1 = Optional.of(100);
    //        Optional<Integer> result2 = result1.filter(value -> value > 40);
    //        System.out.println(result1 == result2); // true
    //
    //        if (user.getAddress() && user.getAddress().getCity() != null &&
    // user.getAddress().getCity().getName() != null) {
    //            String name = user.getAddress().getCity().getName().toLowerCase();
    //        }
    //
    //        if (user.getAddress() != null)

  }

  @Test
  public void test23() {
    Optional<String> result1 = Optional.of("1");
    Optional<Integer> result2 =
        result1.flatMap(
            value -> {
              return Optional.of(Integer.parseInt(value) * 100);
            });
    result2.ifPresent(System.out::println);
  }

  /** 如果在一个没有值的 optional 对象上调用 get，会抛出 NoSuchElementException */
  //    @Test(expected = NoSuchElementException.class)
  @Test
  public void testNoSuchElementExceptionIfAbsent() {
    //        Optional result = Optional.ofNullable(null);
    //        System.out.println(result.isPresent());
    //
    //        Optional<String> result1 = Optional.ofNullable(null);
    //        Optional<String> result2 = Optional.empty();

    Integer n1 = 1;
    Integer n2 = 1;
    Optional<Integer> result1 = Optional.of(n1);
    Optional<Integer> result2 = Optional.of(n2);
    System.out.println(result1.equals(result2));
    System.out.println(result1 == result2);

    //        if (optional.isPresent()) {
    //            System.out.println(optional.get());
    //        } else {
    //            System.out.println("no value present.");
    //        }
  }
}
