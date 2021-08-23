package demos;

import lombok.Data;

/**
 * Person class
 *
 * @author https://github.com/gukt
 */
@Data
public class Person {

    private String name;
    private int age;

    public Person(int age) {
        this.age = age;
    }

    public Person(int age, String name) {
        this.name = name;
        this.age = age;
    }
}
