package com.github.kkgy333.sword.fabric.server.service;

import com.github.kkgy333.sword.fabric.server.model.Orderer;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface OrdererService {

    int add(Orderer orderer);

    int update(Orderer orderer);

    List<Orderer> listAll();

    List<Orderer> listById(int id);

    Orderer get(int id);

    int countById(int id);

    int count();

    int delete(int id);
}
