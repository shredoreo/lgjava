package com.shred.mapper;

import com.shred.pojo.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IRoleMapper {

    @Select("select * from role r,user_role ur where r.id=ur.role_id and ur.user_id = #{uid}")
    List<Role> findRoleByUid(Integer uid);
}
