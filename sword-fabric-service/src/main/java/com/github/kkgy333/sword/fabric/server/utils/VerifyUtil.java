package com.github.kkgy333.sword.fabric.server.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.kkgy333.sword.fabric.server.base.BaseChain;
import com.github.kkgy333.sword.fabric.server.mapper.AppMapper;
import com.github.kkgy333.sword.fabric.server.mapper.ChaincodeMapper;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public class VerifyUtil {

    /**
     * 判断字符串类型
     *
     * @param str 字符串
     *
     * @return 0-string；1-JsonObject；2、JsonArray
     */
    public static int isJSONValid(String str) {
        try {
            JSONObject.parseObject(str);
            return 1;
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(str);
                return 2;
            } catch (JSONException ex1) {
                return 0;
            }
        }
    }

    /** 判断key有效性 */
    public static boolean unRequest(BaseChain baseChain, ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        if (!CacheUtil.getChaincodeId(baseChain.getId(), chaincodeMapper)) {
            if (CacheUtil.getKeyChaincodeId(baseChain.getKey()) == -1 && null != appMapper.getByKey(baseChain.getKey())) {
                CacheUtil.putKeyChaincodeId(baseChain.getKey(), baseChain.getId());
            } else {
                return CacheUtil.getKeyChaincodeId(baseChain.getKey()) != baseChain.getId();
            }
        }
        return false;
    }

    public static List<String> versions() {
        List<String> versions = new LinkedList<>();
        versions.add("1.0");
        versions.add("1.1");
        versions.add("1.2");
        return versions;
    }

}
