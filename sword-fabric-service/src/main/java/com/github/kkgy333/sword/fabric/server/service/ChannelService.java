package com.github.kkgy333.sword.fabric.server.service;

import com.github.kkgy333.sword.fabric.server.model.Channel;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface ChannelService {

    int add(Channel channel);

    int update(Channel channel);

    List<Channel> listAll();

    List<Channel> listById(int id);

    Channel get(int id);

    int countById(int id);

    int count();

    int delete(int id);
}
