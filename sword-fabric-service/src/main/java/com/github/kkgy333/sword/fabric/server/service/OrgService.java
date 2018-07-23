package com.github.kkgy333.sword.fabric.server.service;

import com.github.kkgy333.sword.fabric.server.model.Org;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface OrgService {

    int add(Org org, MultipartFile file);

    int update(Org org, MultipartFile file);

    List<Org> listAll();

    List<Org> listById(int id);

    Org get(int id);

    int countById(int id);

    int count();

    int delete(int id);
}
