import bafeimao.examples.mybatis.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.*;

/**
 * Created by ktgu on 15/8/29.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession();

        String statement = "mapping.userMapper.getUser";//映射sql的标识字符串
        //执行查询返回一个唯一user对象的sql
        User user = session.selectOne(statement, 1);
        System.out.println(user);
    }
}
