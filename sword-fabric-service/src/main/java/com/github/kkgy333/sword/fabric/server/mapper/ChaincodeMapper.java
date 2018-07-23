package com.github.kkgy333.sword.fabric.server.mapper;

import com.github.kkgy333.sword.fabric.server.model.Chaincode;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Mapper
public interface ChaincodeMapper {

    @Insert("insert into chaincode (name,path,version,proposal_wait_time,channel_id,date,source,policy,open) values " +
            "(#{c.name},#{c.path},#{c.version},#{c.proposalWaitTime},#{c.channelId},#{c.date},#{c.source},#{c.policy},#{c.open})")
    int add(@Param("c") Chaincode chaincode);

    @Update("update chaincode set name=#{c.name}, path=#{c.path}, version=#{c.version}, " +
            "proposal_wait_time=#{c.proposalWaitTime}, open=#{c.open} where rowid=#{c.id}")
    int update(@Param("c") Chaincode chaincode);

    @Update("update chaincode set name=#{c.name}, path=#{c.path}, version=#{c.version}, " +
            "proposal_wait_time=#{c.proposalWaitTime}, source=#{c.source}, policy=#{c.policy}, " +
            "open=#{c.open} where rowid=#{c.id}")
    int updateForUpgrade(@Param("c") Chaincode chaincode);

    @Select("select count(name) from chaincode where channel_id=#{id}")
    int count(@Param("id") int id);

    @Select("select count(name) from chaincode")
    int countAll();

    @Delete("delete from chaincode where rowid=#{id}")
    int delete(@Param("id") int id);

    @Delete("delete from chaincode where channel_id=#{channelId}")
    int deleteAll(@Param("channelId") int channelId);

    @Select("select rowid,name,path,version,proposal_wait_time,channel_id,date,source,policy,open from chaincode " +
            "where name=#{c.name} and path=#{c.path} and version=#{c.version} and channel_id=#{c.channelId}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy"),
            @Result(property = "open", column = "open")
    })
    Chaincode check(@Param("c") Chaincode chaincode);

    @Select("select rowid,name,path,version,proposal_wait_time,channel_id,date,source,policy,open from chaincode where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy"),
            @Result(property = "open", column = "open")
    })
    Chaincode get(@Param("id") int id);

    @Select("select rowid,name,path,version,proposal_wait_time,channel_id,date,source,policy,open from chaincode where channel_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy"),
            @Result(property = "open", column = "open")
    })
    List<Chaincode> list(@Param("id") int id);

    @Select("select rowid,name,path,version,proposal_wait_time,channel_id,date,source,policy,open from chaincode")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "version", column = "version"),
            @Result(property = "proposalWaitTime", column = "proposal_wait_time"),
            @Result(property = "channelId", column = "channel_id"),
            @Result(property = "date", column = "date"),
            @Result(property = "source", column = "source"),
            @Result(property = "policy", column = "policy"),
            @Result(property = "open", column = "open")
    })
    List<Chaincode> listAll();

}