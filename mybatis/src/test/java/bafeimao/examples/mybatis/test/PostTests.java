package bafeimao.examples.mybatis.test;

import bafeimao.examples.mybatis.domain.Post;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by ktgu on 15/8/29.
 */
public class PostTests {
    SqlSessionFactory sessionFactory;

    @Before
    public void setup() throws FileNotFoundException {
        InputStream is = PostTests.class.getClassLoader().getResourceAsStream("mybatis-config.xml");
        sessionFactory = new SqlSessionFactoryBuilder().build(is);

        Assert.assertNotNull(sessionFactory);
    }


    /**
     * 测试one-to-one映射
     */
    @Test
    public void testSelectPost() {
        SqlSession session = sessionFactory.openSession(true);
        try {
            Post post = session.selectOne("mapping.postMapper.selectPost", 1);
            Assert.assertNotNull(post);
            Assert.assertNotNull(post.getUser());
            System.out.println(post);
        }finally {
            session.close();
        }
    }
}
