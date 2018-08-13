package com.github.kkgy333.sword.fabric.server.service;

import com.github.kkgy333.sword.fabric.server.bean.App;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface AppService {

    int add(App app);

    int update(App app);

    int updateKey(int id);

    List<App> list(int id);

    App get(int id);

    int delete(int id);

    int deleteAll(int id);

    int count();

    int countById(int id);
}