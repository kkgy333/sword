package com.github.kkgy333.sword.fabric.server.mapper;

import com.github.kkgy333.sword.fabric.server.model.Channel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Mapper
public interface ChannelMapper {

    @Insert("insert into channel (name,peer_id,date) values (#{c.name},#{c.peerId},#{c.date})")
    int add(@Param("c") Channel channel);

    @Update("update channel set name=#{c.name} where rowid=#{c.id}")
    int update(@Param("c") Channel channel);

    @Select("select count(name) from channel where peer_id=#{id}")
    int count(@Param("id") int id);

    @Select("select count(name) from channel")
    int countAll();

    @Delete("delete from channel where rowid=#{id}")
    int delete(@Param("id") int id);

    @Delete("delete from channel where peer_id=#{peerId}")
    int deleteAll(@Param("peerId") int peerId);

    @Select("select rowid,name,peer_id,date from channel where name=#{c.name} and peer_id=#{c.peerId}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    Channel check(@Param("c") Channel channel);

    @Select("select rowid,name,peer_id,date from channel where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    Channel get(@Param("id") int id);

    @Select("select rowid,name,peer_id,date from channel where peer_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    List<Channel> list(@Param("id") int id);

    @Select("select rowid,name,peer_id,date from channel")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "peerId", column = "peer_id"),
            @Result(property = "date", column = "date")
    })
    List<Channel> listAll();

}