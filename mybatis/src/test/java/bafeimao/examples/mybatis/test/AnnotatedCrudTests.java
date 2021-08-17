package bafeimao.examples.mybatis.test;

import bafeimao.examples.mybatis.domain.User;
import bafeimao.examples.mybatis.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by ktgu on 15/8/29.
 */
public class AnnotatedCrudTests {
    SqlSessionFactory sessionFactory;

    @Before
    public void setup() throws FileNotFoundException {
        InputStream is = AnnotatedCrudTests.class.getClassLoader().getResourceAsStream("mybatis-config.xml");
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
            UserMapper userMapper = session.getMapper(UserMapper.class);
            int i = userMapper.insert(user);

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
            user.setId(8);
            user.setName("tom888");
            user.setAge(200);
            int i = session.getMapper(UserMapper.class).update(user);

            System.out.println(i);
        } finally {
            session.close();
        }
    }

    @Test
    public void testDeleteUser() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            int i = session.getMapper(UserMapper.class).delete(8);
            System.out.println(i);
        } finally {
            session.close();
        }
    }

    @Test
    public void testSelectUserById() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            User user = session.getMapper(UserMapper.class).getById(1);
            System.out.println(user);
        } finally {
            session.close();
        }
    }

    @Test
    public void testSelectUserByName() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            User user = session.getMapper(UserMapper.class).getByName("ktgu");
            System.out.println(user);
        } finally {
            session.close();
        }
    }

    @Test
    public void testSelectAllUsers() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            List<User> users = session.getMapper(UserMapper.class).getAll();
            System.out.println(users);
        } finally {
            session.close();
        }
    }

}
