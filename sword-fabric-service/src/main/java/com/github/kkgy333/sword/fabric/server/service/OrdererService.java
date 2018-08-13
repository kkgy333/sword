package com.github.kkgy333.sword.fabric.server.service;

import com.github.kkgy333.sword.fabric.server.dao.Orderer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface OrdererService {

     int add(Orderer orderer, MultipartFile serverCrtFile);

     int update(Orderer orderer, MultipartFile serverCrtFile);

     List<Orderer> listAll();

     List<Orderer> listById(int id);

     Orderer get(int id);

     int countById(int id);

     int count();

     int delete(int id);
}
