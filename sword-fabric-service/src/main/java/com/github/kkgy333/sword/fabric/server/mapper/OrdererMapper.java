package com.github.kkgy333.sword.fabric.server.mapper;

import com.github.kkgy333.sword.fabric.server.model.Orderer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Mapper
public interface OrdererMapper {

    @Insert("insert into orderer (name,location,org_id,date) values (#{o.name},#{o.location},#{o.orgId},#{o.date})")
    int add(@Param("o") Orderer orderer);

    @Update("update orderer set name=#{o.name}, location=#{o.location} where id=#{o.id}")
    int update(@Param("o") Orderer orderer);

    @Select("select count(name) from orderer where org_id=#{id}")
    int count(@Param("id") int id);

    @Select("select count(name) from orderer")
    int countAll();

    @Delete("delete from orderer where id=#{id}")
    int delete(@Param("id") int id);

    @Delete("delete from orderer where org_id=#{orgId}")
    int deleteAll(@Param("orgId") int orgId);

    @Select("select id,name,location,org_id,date from orderer where id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location"),
            @Result(property = "orgId", column = "org_id"),
            @Result(property = "date", column = "date")
    })
    Orderer get(@Param("id") int id);

    @Select("select id,name,location,org_id,date from orderer where org_id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location"),
            @Result(property = "orgId", column = "org_id"),
            @Result(property = "date", column = "date")
    })
    List<Orderer> list(@Param("id") int id);

    @Select("select id,name,location,org_id,date from orderer")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "location", column = "location"),
            @Result(property = "orgId", column = "org_id"),
            @Result(property = "date", column = "date")
    })
    List<Orderer> listAll();

}