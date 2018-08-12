package com.github.kkgy333.sword.fabric.sdk;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 描述：BlockListener监听返回map集合
 *
 * @author : Aberic 【2018/5/22 18:49】
 */
public interface BlockListener {

    int SUCCESS = 200;
    int ERROR = 9999;

    void received(JSONObject jsonObject);
}
