package com.github.kkgy333.sword.fabric.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.kkgy333.sword.fabric.server.model.Orderer;
import com.github.kkgy333.sword.fabric.server.model.Peer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/

public interface PeerMapper extends BaseMapper<Peer> {

//    @Insert("insert into peer (name,event_hub_name,location,event_hub_location,event_listener,org_id,date) " +
//            "values (#{p.name},#{p.eventHubName},#{p.location},#{p.eventHubLocation},#{p.eventListener},#{p.orgId},#{p.date})")
//    int add(@Param("p") Peer peer);
//
//    @Update("update peer set name=#{p.name}, event_hub_name=#{p.eventHubName}, location=#{p.location}" +
//            ", event_hub_location=#{p.eventHubLocation}, event_listener=#{p.eventListener} where id=#{p.id}")
//    int update(@Param("p") Peer peer);
//
//    @Select("select count(name) from peer where org_id=#{id}")
//    int count(@Param("id") int id);
//
//    @Select("select count(name) from peer")
//    int countAll();
//
//    @Delete("delete from peer where id=#{id}")
//    int delete(@Param("id") int id);
//
//    @Delete("delete from peer where org_id=#{orgId}")
//    int deleteAll(@Param("orgId") int orgId);
//
//    @Select("select id,name,event_hub_name,location,event_hub_location,event_listener,org_id,date from peer where id=#{id}")
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "name", column = "name"),
//            @Result(property = "eventHubName", column = "event_hub_name"),
//            @Result(property = "location", column = "location"),
//            @Result(property = "eventHubLocation", column = "event_hub_location"),
//            @Result(property = "eventListener", column = "event_listener"),
//            @Result(property = "orgId", column = "org_id"),
//            @Result(property = "date", column = "date")
//    })
//    Peer get(@Param("id") int id);
//
//    @Select("select id,name,event_hub_name,location,event_hub_location,event_listener,org_id,date from peer where org_id=#{id}")
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "name", column = "name"),
//            @Result(property = "eventHubName", column = "event_hub_name"),
//            @Result(property = "location", column = "location"),
//            @Result(property = "eventHubLocation", column = "event_hub_location"),
//            @Result(property = "eventListener", column = "event_listener"),
//            @Result(property = "orgId", column = "org_id"),
//            @Result(property = "date", column = "date")
//    })
//    List<Peer> list(@Param("id") int id);
//
//    @Select("select id,name,event_hub_name,location,event_hub_location,event_listener,org_id,date from peer")
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "name", column = "name"),
//            @Result(property = "eventHubName", column = "event_hub_name"),
//            @Result(property = "location", column = "location"),
//            @Result(property = "eventHubLocation", column = "event_hub_location"),
//            @Result(property = "eventListener", column = "event_listener"),
//            @Result(property = "orgId", column = "org_id"),
//            @Result(property = "date", column = "date")
//    })
//    List<Peer> listAll();

}