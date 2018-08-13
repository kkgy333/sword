package com.github.kkgy333.sword.fabric.server.service;

import com.github.kkgy333.sword.fabric.server.dao.League;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface LeagueService {

    int add(League league);

    int update(League league);

    List<League> listAll();

    League get(int id);

    int delete(int id);
}
