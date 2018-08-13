package com.github.kkgy333.sword.fabric.server.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.kkgy333.sword.fabric.server.base.BaseChain;
import com.github.kkgy333.sword.fabric.server.dao.mapper.*;
import org.apache.commons.lang3.StringUtils;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public class VerifyUtil {

    /** 判断key有效性 */
    public static String getCc(BaseChain baseChain, ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        String cc = null;
        if (CacheUtil.getAppBool(baseChain.getKey(), appMapper)) {
            cc = CacheUtil.getString(baseChain.getKey());
            if (StringUtils.isEmpty(cc)) {
                try {
                    cc = chaincodeMapper.get(appMapper.getByKey(baseChain.getKey()).getChaincodeId()).getCc();
                    CacheUtil.putString(baseChain.getKey(), cc);
                } catch (Exception e) {
                    cc = null;
                }
            }
        }
        return cc;
    }

}
