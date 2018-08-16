/*
 * Copyright (c) 2018. Aberic - aberic@qq.com - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.kkgy333.sword.fabric.server.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.kkgy333.sword.fabric.server.dao.Orderer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/9 13:53
 * 邮箱：abericyang@gmail.com
 */
//@Mapper
public interface OrdererMapper extends BaseMapper<Orderer> {

//    @Insert("insert into orderer (name,location,server_crt_path,org_id,date) values (#{o.name},#{o.location},#{o.serverCrtPath},#{o.orgId},#{o.date})")
//    int add(@Param("o") Orderer orderer);
//
//    @Update("update orderer set name=#{o.name}, location=#{o.location}, server_crt_path=#{o.serverCrtPath} where id=#{o.id}")
//    int update(@Param("o") Orderer orderer);
//
//    @Update("update orderer set name=#{o.name}, location=#{o.location} where id=#{o.id}")
//    int updateWithNoFile(@Param("o") Orderer orderer);


      boolean updateWithNoFile(@Param("o") Orderer orderer);

//
//    @Select("select count(name) from orderer where org_id=#{id}")
//    int count(@Param("id") int id);
//
//    @Select("select count(name) from orderer")
//    int countAll();
//
//    @Delete("delete from orderer where id=#{id}")
//    int delete(@Param("id") int id);
//
//    @Delete("delete from orderer where org_id=#{orgId}")
//    int deleteAll(@Param("orgId") int orgId);
//
//    @Select("select id,name,location,server_crt_path,org_id,date from orderer where id=#{id}")
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "name", column = "name"),
//            @Result(property = "location", column = "location"),
//            @Result(property = "serverCrtPath", column = "server_crt_path"),
//            @Result(property = "orgId", column = "org_id"),
//            @Result(property = "date", column = "date")
//    })
//    Orderer get(@Param("id") int id);
//
//    @Select("select id,name,location,server_crt_path,org_id,date from orderer where org_id=#{id}")
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "name", column = "name"),
//            @Result(property = "location", column = "location"),
//            @Result(property = "serverCrtPath", column = "server_crt_path"),
//            @Result(property = "orgId", column = "org_id"),
//            @Result(property = "date", column = "date")
//    })
//    List<Orderer> list(@Param("id") int id);
//
//    @Select("select id,name,location,server_crt_path,org_id,date from orderer")
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "name", column = "name"),
//            @Result(property = "location", column = "location"),
//            @Result(property = "serverCrtPath", column = "server_crt_path"),
//            @Result(property = "orgId", column = "org_id"),
//            @Result(property = "date", column = "date")
//    })
//    List<Orderer> listAll();

}
