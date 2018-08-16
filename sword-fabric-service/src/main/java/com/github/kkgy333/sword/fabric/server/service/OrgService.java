package com.github.kkgy333.sword.fabric.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.kkgy333.sword.fabric.server.dao.Org;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface OrgService extends IService<Org> {

    boolean add(Org org);

    boolean update(Org org);

    List<Org> listAll();

    List<Org> listById(int id);

    Org get(int id);

    int countById(int id);

    int count();

    boolean delete(int id);
}
