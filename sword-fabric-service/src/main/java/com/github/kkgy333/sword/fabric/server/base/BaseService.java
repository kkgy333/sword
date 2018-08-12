package com.github.kkgy333.sword.fabric.server.base;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.kkgy333.sword.fabric.server.utils.VerifyUtil;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface BaseService {

    int SUCCESS = 200;
    int FAIL = 9999;

    default String responseSuccess(String result) {
        JSONObject jsonObject = parseResult(result);
        jsonObject.put("code", SUCCESS);
        return jsonObject.toString();
    }

    default JSONObject responseSuccessJson(String result) {
        JSONObject jsonObject = parseResult(result);
        jsonObject.put("code", SUCCESS);
        return jsonObject;
    }

    default String responseSuccess(String result, String txid) {
        JSONObject jsonObject = parseResult(result);
        jsonObject.put("code", SUCCESS);
        jsonObject.put("txid", txid);
        return jsonObject.toString();
    }

    default JSONObject responseSuccessJson(String result, String txid) {
        JSONObject jsonObject = parseResult(result);
        jsonObject.put("code", SUCCESS);
        jsonObject.put("txid", txid);
        return jsonObject;
    }

    default String responseSuccess(JSONObject json) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SUCCESS);
        jsonObject.put("data", json);
        return jsonObject.toString();
    }

    default String responseSuccess(JSONArray array) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SUCCESS);
        jsonObject.put("data", array);
        return jsonObject.toString();
    }

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

    default JSONObject parseResult(String result) {
        JSONObject jsonObject = new JSONObject();
        int jsonVerify = VerifyUtil.isJSONValid(result);
        switch (jsonVerify) {
            case 0:
                jsonObject.put("data", result);
                break;
            case 1:
                jsonObject.put("data", JSONObject.parseObject(result));
                break;
            case 2:
                jsonObject.put("data", JSONObject.parseArray(result));
                break;
        }
        return jsonObject;
    }

}