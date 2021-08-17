package net.bafeimao.examples.test.java;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2015/10/19.
 */
public class ResourceTests {
private static Logger LOGGER = LoggerFactory.getLogger(ResourceTests.class);

    @Test
    public void test1() {
        this.getClass().getResource(".");
    }
}