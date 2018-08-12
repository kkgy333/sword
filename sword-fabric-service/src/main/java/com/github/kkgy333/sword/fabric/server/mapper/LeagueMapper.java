package com.github.kkgy333.sword.fabric.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.kkgy333.sword.fabric.server.model.League;
import com.github.kkgy333.sword.fabric.server.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/

public interface LeagueMapper extends BaseMapper<League> {

//    @Insert("insert into league  (name,date,version) values (#{l.name},#{l.date},#{l.version})")
//    int add(@Param("l") League league);
//
//    @Update("update league set name=#{l.name},version=#{l.version} where id=#{l.id}")
//    int update(@Param("l") League league);
//
//    @Delete("delete from league where id=#{id}")
//    int delete(@Param("id") int id);
//
//    @Select("select id,name,date,version from league where id=#{id}")
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "name", column = "name"),
//            @Result(property = "date", column = "date"),
//            @Result(property = "version", column = "version")
//    })
//    League get(@Param("id") int id);
//
//    @Select("select id,name,date,version from league")
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "name", column = "name"),
//            @Result(property = "date", column = "date"),
//            @Result(property = "version", column = "version")
//    })
//    List<League> listAll();

}
