package com.github.kkgy333.sword.fabric.server.service;

import com.github.kkgy333.sword.fabric.server.model.Peer;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface PeerService {

    int add(Peer peer);

    int update(Peer peer);

    List<Peer> listAll();

    List<Peer> listById(int id);

    Peer get(int id);

    int countById(int id);

    int count();

    int delete(int id);
}
