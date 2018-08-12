package com.github.kkgy333.sword.fabric.server.service;

import com.alibaba.fastjson.JSONObject;
import com.github.kkgy333.sword.fabric.server.bean.Api;
import com.github.kkgy333.sword.fabric.server.model.Chaincode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface ChaincodeService {

    int add(Chaincode chaincode);

    JSONObject install(Chaincode chaincode, MultipartFile file, Api api, boolean init);

    JSONObject instantiate(Chaincode chaincode, List<String> strArray);

    JSONObject upgrade(Chaincode chaincode, MultipartFile file, Api api);

    int update(Chaincode chaincode);

    List<Chaincode> listAll();

    List<Chaincode> listById(int id);

    Chaincode get(int id);

    int countById(int id);

    int count();

    int delete(int id);

    int deleteAll(int channelId);

}
