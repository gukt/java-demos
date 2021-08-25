package demos;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Slf4j
public class MiscTests {

    @Test
    public void testNullInstanceOfObject() {
        Assertions.assertFalse(null instanceof Object);
    }

    @Test
    void testJoiner() {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add("hello");
        joiner.add("world");
        System.out.println(joiner); // hello,world
    }

    @Test
    void testCharOrStringRepeating() {
        System.out.println(StringUtils.repeat("hello,", 10));
    }

    @Test
    public void testHashMapPerformance() {
        Map<Integer, Object> map = new HashMap<>();
        int count = 10000;
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            map.put(i, "String_" + i);
        }
        log.info("Puts {} elements into the map: elapsed: {}", count, System.nanoTime() - startTime);
        System.out.println(map.get(50000));
    }
}
