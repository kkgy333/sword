package com.github.kkgy333.sword.fabric.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.kkgy333.sword.fabric.server.dao.Org;
import com.github.kkgy333.sword.fabric.server.dao.Peer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface PeerService extends IService<Peer> {
    
    boolean add(Peer peer, MultipartFile serverCrtFile);

    boolean update(Peer peer, MultipartFile serverCrtFile);

    List<Peer> listAll();

    List<Peer> listById(int id);

    Peer get(int id);

    int countById(int id);

    int count();

    boolean delete(int id);
}
