package bafeimao.examples.mybatis.test;

import bafeimao.examples.mybatis.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by ktgu on 15/8/29.
 */
public class UserTests {
    SqlSessionFactory sessionFactory;

    @Before
    public void setup() throws FileNotFoundException {
        InputStream is = UserTests.class.getClassLoader().getResourceAsStream("mybatis-config.xml");
        sessionFactory = new SqlSessionFactoryBuilder().build(is);

        Assert.assertNotNull(sessionFactory);
    }


    @Test
    public void testInsertUser() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            User user = new User();
            user.setName("tom");
            user.setAge(20);
            int i = session.insert("mapping.userMapper.insertUser", user);

            System.out.println(i);
        } finally {
            session.close();
        }
    }

    @Test
    public void testUpdateUser() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            User user = new User();
            user.setId(7);
            user.setName("tom777");
            user.setAge(200);
            int i = session.insert("mapping.userMapper.updateUser", user);

            System.out.println(i);
        } finally {
            session.close();
        }
    }

    @Test
    public void testDeleteUser() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            int i = session.insert("mapping.userMapper.deleteUser", 7);
            System.out.println(i);
        } finally {
            session.close();
        }
    }

    @Test
    public void testSelectUserById() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            User user = session.selectOne("mapping.userMapper.selectUserById", 1);
            System.out.println(user);
        } finally {
            session.close();
        }
    }

    @Test
    public void testSelectUserByName() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            User user = session.selectOne("mapping.userMapper.selectUserByName", "ktgu");
            System.out.println(user);
        } finally {
            session.close();
        }
    }

    @Test
    public void testSelectAllUsers() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            List<User> users = session.selectList("mapping.userMapper.selectAllUsers");
            System.out.println(users);
        } finally {
            session.close();
        }
    }

    /**
     * 测试one-to-many映射
     */
    @Test
    public void testSelectUserAndPostsById() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            User user = session.selectOne("mapping.userMapper.selectUserAndPostsById", 2);
            Assert.assertNotNull(user);
            Assert.assertNotNull(user.getPosts());
            Assert.assertTrue(user.getPosts().size() > 0);
        } finally {
            session.close();
        }
    }

    @Test
    public void testSelectUserAsHashMap() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            Map<String, Object> result = session.selectOne("mapping.userMapper.selectUserAsHashMap", 1);
            Assert.assertNotNull(result);
            Assert.assertTrue(result.size() > 0);
        } finally {
            session.close();
        }
    }

}
