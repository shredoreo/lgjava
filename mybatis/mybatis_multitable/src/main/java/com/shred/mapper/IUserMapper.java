package com.shred.mapper;

import com.shred.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IUserMapper {

    @Select("select * from user")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "orderList", column = "id",
                    //javaType 为list
                    javaType = List.class,
                    //many 中的selectMapper的参数由column指定，也就是取id那一列的值作为其参数
                    many = @Many(select = "com.shred.mapper.IOrderMapper.findOrderByUid")
            ),
    })
    List<User> findAll();


    @Select("select * from user")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "roleList", column = "id",
                    javaType = List.class,
                    many = @Many(select = "com.shred.mapper.IRoleMapper.findRoleByUid")
            ),
    })
    List<User> findAllUserAndRole();

    @Select("select  * from user where id = #{id}")
    User findOne(Integer id);

    @Insert("insert into user values(#{id}, #{username})")
    void add(User user);

    @Update("update user set username = #{username} where id = #{id}")
    void update(User user);

    @Delete("delete from user where id=#{id}")
    void deleteUser(Integer id);


}
