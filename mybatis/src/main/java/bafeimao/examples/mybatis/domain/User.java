package bafeimao.examples.mybatis.domain;

import java.util.List;

/**
 * Created by ktgu on 15/8/31.
 */
public class User {
    private int id;
    private String name;
    private int age;
    private List<Post> posts;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", age=" + age + "]";
    }
}