package com.github.kkgy333.sword.fabric.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.kkgy333.sword.fabric.server.dao.League;
import com.github.kkgy333.sword.fabric.server.dao.User;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface LeagueService extends IService<League> {

    boolean add(League league);

    boolean update(League league);

    List<League> listAll();

    League get(int id);

    boolean delete(int id);
}
