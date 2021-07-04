package com.shred.mapper;

import com.shred.pojo.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.mapping.FetchType;
import org.mybatis.caches.redis.RedisCache;

import java.util.List;

//开启二级缓存
//@CacheNamespace(implementation = RedisCache.class)
public interface IUserMapper {

    @Select("select * from user")
    //给@Results加上id，实现复用，复用时使用@ResultMap(value={"id"})
    @Results(id = "userOrderMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "orderList", column = "id",
                    //javaType 为list
                    javaType = List.class,
                    //many 中的selectMapper的参数由column指定，也就是取id那一列的值作为其参数
                    many = @Many(fetchType = FetchType.EAGER, select = "com.shred.mapper.IOrderMapper.findOrderByUid")
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

    @Options(useCache = true)
    @Select("select  * from user where id = #{id}")
    @ResultMap(value = {"userOrderMap"})
    User findOne(Integer id);

    @Insert("insert into user values(#{id}, #{username})")
    void add(User user);

    @Update("update user set username = #{username} where id = #{id}")
    void update(User user);

    @Delete("delete from user where id=#{id}")
    void deleteUser(Integer id);


}
