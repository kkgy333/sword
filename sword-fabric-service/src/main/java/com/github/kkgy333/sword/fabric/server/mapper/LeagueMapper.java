package com.github.kkgy333.sword.fabric.server.mapper;

import com.github.kkgy333.sword.fabric.server.model.League;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Mapper
public interface LeagueMapper {

    @Insert("insert into league  (name,date,version) values (#{l.name},#{l.date},#{l.version})")
    int add(@Param("l") League league);

    @Update("update league set name=#{l.name},version=#{l.version} where rowid=#{l.id}")
    int update(@Param("l") League league);

    @Delete("delete from league where rowid=#{id}")
    int delete(@Param("id") int id);

    @Select("select rowid,name,date,version from league where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "date", column = "date"),
            @Result(property = "version", column = "version")
    })
    League get(@Param("id") int id);

    @Select("select rowid,name,date,version from league")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "date", column = "date"),
            @Result(property = "version", column = "version")
    })
    List<League> listAll();

}