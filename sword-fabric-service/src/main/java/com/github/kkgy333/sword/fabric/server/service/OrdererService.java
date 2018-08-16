package com.github.kkgy333.sword.fabric.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.kkgy333.sword.fabric.server.dao.League;
import com.github.kkgy333.sword.fabric.server.dao.Orderer;
import org.springframework.core.annotation.Order;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface OrdererService extends IService<Orderer> {

     boolean add(Orderer orderer, MultipartFile serverCrtFile);

     boolean update(Orderer orderer, MultipartFile serverCrtFile);

     List<Orderer> listAll();

     List<Orderer> listById(int id);

     Orderer get(int id);

     int countById(int id);

     int count();

     boolean delete(int id);
}
