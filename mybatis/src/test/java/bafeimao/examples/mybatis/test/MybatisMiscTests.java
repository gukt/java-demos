package bafeimao.examples.mybatis.test;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ktgu on 15/8/29.
 */
public class MybatisMiscTests {
    /**
     * TODO Make it work
     * @throws IOException
     */
    @Test
    public void testBuildSqlSessionFactoryWithXml() throws IOException {
//        String resource = Main.class.getResource("mybatis-config.xml").getPath();
//        File file = new File(resource);
//        InputStream inputStream = new FileInputStream(file);
//
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//        Assert.assertNotNull(sqlSessionFactory);
    }

    /**
     * TODO to be continued
     */
    @Test
    public void testBuildSqlSessionFactoryWithJava() {
//        DataSource dataSource = BlogDataSourceFactory.getBlogDataSource();
//        TransactionFactory transactionFactory = new JdbcTransactionFactory();
//        Environment environment = new Environment("development", transactionFactory, dataSource);
//        Configuration configuration = new Configuration(environment);
//        configuration.addMapper(BlogMapper.class);
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

    }
}
