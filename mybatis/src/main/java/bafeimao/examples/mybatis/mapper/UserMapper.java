package bafeimao.examples.mybatis.mapper;

import bafeimao.examples.mybatis.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by ktgu on 15/8/31.
 */
public interface UserMapper {

    @Select("select * from ccn_users where id = #{id}")
    User getById(int id);

    @Select("select * from ccn_users where name= #{name}")
    User getByName(String name);

    @Select("select * from ccn_users")
    List<User> getAll();

    @Insert("insert into ccn_users(name, age) values (#{name}, #{age})")
    int insert(User user);

    @Update("update ccn_users set name=#{name}, age=#{age} where id=#{id}")
    int update(User user);

    @Delete("delete from ccn_users where id = #{id}")
    int delete(int id);
}
