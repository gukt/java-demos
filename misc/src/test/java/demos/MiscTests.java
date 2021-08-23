package demos;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MiscTests {

    @Test
    public void testHashMapBenchmark() {
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        int count = 10000;
        long startTime = System.nanoTime();

        for (int i = 0; i < count; i++) {
            map.put(i, "String_" + i);
        }
        log.info("Put {} items into map in {} nano-seconds", count, System.nanoTime() - startTime);

        System.out.println(map.get(50000));
    }

    @Test
    public void test1() {
        log.trace("xxxx:{}", concatStrings());
    }

    private String concatStrings(String... params) {
        String result = "";
        for (String s : params) {
            result += "," + s;
        }
        return result;
    }

}
