package com.shred.mapper;

import com.shred.pojo.Order;
import com.shred.pojo.User;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IOrderMapper {

    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "orderTime", column = "orderTime"),
            @Result(property = "total", column = "total"),
            @Result(property = "user", column = "uid", javaType = User.class,
                //one中select的参数由column列指定
                one = @One(select = "com.shred.mapper.IUserMapper.findOne")
            )
    })
    @Select("select * from orders")
    List<Order> findOrderAndUser();


    @Select("select * from orders where uid=#{uid}")
    List<Order> findOrderByUid(Integer uid);
}
