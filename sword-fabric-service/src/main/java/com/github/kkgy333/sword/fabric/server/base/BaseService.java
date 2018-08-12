package com.github.kkgy333.sword.fabric.server.base;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface BaseService {

    int FAIL = 9999;

    default String responseFail(String result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", FAIL);
        jsonObject.put("error", result);
        return jsonObject.toString();
    }

    default JSONObject responseFailJson(String result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", FAIL);
        jsonObject.put("error", result);
        return jsonObject;
    }

}