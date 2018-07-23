package com.github.kkgy333.sword.fabric.server.mapper;

import com.github.kkgy333.sword.fabric.server.bean.App;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Mapper
public interface AppMapper {

    @Insert("insert into app (name, key, chaincode_id, create_date, modify_date, private_key, public_key, active)" +
            " values (#{a.name},#{a.key},#{a.chaincodeId},#{a.createDate},#{a.modifyDate},#{a.privateKey},#{a.publicKey},#{a.active})")
    int add(@Param("a") App app);

    @Update("update app set name=#{a.name}, modify_date=#{a.modifyDate}, active=#{a.active} where rowid=#{a.id}")
    int update(@Param("a") App app);

    @Update("update app set key=#{a.key}, private_key=#{a.privateKey}, public_key=#{a.publicKey} where rowid=#{a.id}")
    int updateKey(@Param("a") App app);

    @Select("select count(name) from app where chaincode_id=#{id}")
    int count(@Param("id") int id);

    @Select("select key from app where name=#{a.name} and chaincode_id=#{a.chaincodeId}")
    @Results({
            @Result(property = "name", column = "name"),
            @Result(property = "key", column = "key"),
            @Result(property = "chaincode_id", column = "chaincodeId")
    })
    App check(@Param("a") App app);

    @Select("select rowid, name, key, chaincode_id, create_date, modify_date, public_key, active from app where chaincode_id=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "key", column = "key"),
            @Result(property = "chaincodeId", column = "chaincode_id"),
            @Result(property = "createDate", column = "create_date"),
            @Result(property = "modifyDate", column = "modify_date"),
            @Result(property = "publicKey", column = "public_key"),
            @Result(property = "active", column = "active")
    })
    List<App> list(@Param("id") int id);

    @Select("select rowid, name, key, chaincode_id, create_date, modify_date, public_key, active from app where rowid=#{id}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "key", column = "key"),
            @Result(property = "chaincodeId", column = "chaincode_id"),
            @Result(property = "createDate", column = "create_date"),
            @Result(property = "modifyDate", column = "modify_date"),
            @Result(property = "publicKey", column = "public_key"),
            @Result(property = "active", column = "active")
    })
    App get(@Param("id") int id);

    @Select("select rowid, name, key, chaincode_id, create_date, modify_date, public_key, active from app where key=#{key}")
    @Results({
            @Result(property = "id", column = "rowid"),
            @Result(property = "name", column = "name"),
            @Result(property = "key", column = "key"),
            @Result(property = "chaincodeId", column = "chaincode_id"),
            @Result(property = "createDate", column = "create_date"),
            @Result(property = "modifyDate", column = "modify_date"),
            @Result(property = "publicKey", column = "public_key"),
            @Result(property = "active", column = "active")
    })
    App getByKey(@Param("key") String key);

    @Delete("delete from app where rowid=#{id}")
    int delete(@Param("id") int id);

    @Delete("delete from app where chaincode_id=#{id}")
    int deleteAll(@Param("id") int id);

}
