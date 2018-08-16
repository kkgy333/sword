package com.github.kkgy333.sword.fabric.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.kkgy333.sword.fabric.server.dao.Channel;
import com.github.kkgy333.sword.fabric.server.dao.League;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface ChannelService extends IService<Channel> {

    boolean add(Channel channel);

    boolean update(Channel channel);

    List<Channel> listAll();

    List<Channel> listById(int id);

    Channel get(int id);

    int countById(int id);

    int count();

    boolean delete(int id);
}
