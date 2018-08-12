package com.github.kkgy333.sword.fabric.server.service;

import com.github.kkgy333.sword.fabric.server.bean.State;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface StateService {

    String invoke(State state);

    String query(State state);

}