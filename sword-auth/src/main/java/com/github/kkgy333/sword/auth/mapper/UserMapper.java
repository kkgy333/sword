package com.github.kkgy333.sword.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.kkgy333.sword.auth.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/5
 **/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 修改用户状态
     */
//    int setStatus(@Param("userId") Integer userId, @Param("status") int status);

    /**
     * 修改密码
     */
//    int changePwd(@Param("userId") Integer userId, @Param("pwd") String pwd);

    /**
     * 根据条件查询用户列表
     */
//    List<Map<String, Object>> selectUsers(@Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptid") Integer deptid);

    /**
     * 设置用户的角色
     */
//    int setRoles(@Param("userId") Integer userId, @Param("roleIds") String roleIds);

    /**
     * 通过账号获取用户
     */
    User getByAccount(@Param("account") String account);

    @Select("select test_id as id, name, age, test_type from user")
    List<User> selectListBySQL();
}