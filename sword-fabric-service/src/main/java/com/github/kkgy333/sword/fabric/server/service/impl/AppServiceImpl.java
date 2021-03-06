package com.github.kkgy333.sword.fabric.server.service.impl;


import com.github.kkgy333.sword.fabric.server.bean.App;
import com.github.kkgy333.sword.fabric.server.bean.Key;
import com.github.kkgy333.sword.fabric.server.mapper.AppMapper;
import com.github.kkgy333.sword.fabric.server.service.AppService;
import com.github.kkgy333.sword.fabric.server.utils.CacheUtil;
import com.github.kkgy333.sword.fabric.server.utils.DateUtil;
import com.github.kkgy333.sword.fabric.server.utils.MathUtil;
import com.github.kkgy333.sword.fabric.server.utils.encryption.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Service("appService")
public class AppServiceImpl implements AppService {

    @Resource
    private AppMapper appMapper;

    @Override
    public int add(App app, int chaincodeId) {
        if (null != appMapper.check(app)) {
            return 0;
        }
        Key key = Utils.obtain().createECCDSAKeyPair();
        if (null == key) {
            return 0;
        }
        app.setPublicKey(key.getPublicKey());
        app.setPrivateKey(key.getPrivateKey());
        app.setChaincodeId(chaincodeId);
        app.setKey(MathUtil.getRandom8());
        app.setCreateDate(DateUtil.getCurrent("yyyy-MM-dd HH:mm:ss"));
        app.setModifyDate(DateUtil.getCurrent("yyyy-MM-dd HH:mm:ss"));
        if (app.isActive()) {
            CacheUtil.putKeyChaincodeId(app.getKey(), app.getChaincodeId());
        }
        return appMapper.add(app);
    }

    @Override
    public int update(App app) {
        app.setModifyDate(DateUtil.getCurrent("yyyy-MM-dd HH:mm:ss"));
        if (app.isActive()) {
            CacheUtil.putKeyChaincodeId(app.getKey(), app.getChaincodeId());
        } else {
            CacheUtil.removeString(app.getKey());
        }
        return appMapper.update(app);
    }

    @Override
    public int updateKey(int id) {
        App app = new App();
        app.setId(id);
        CacheUtil.removeKeyChaincodeId(appMapper.get(id).getKey());
        app.setKey(MathUtil.getRandom8());
        if (app.isActive()) {
            CacheUtil.putKeyChaincodeId(app.getKey(), app.getChaincodeId());
        } else {
            CacheUtil.removeString(app.getKey());
        }
        Key key = Utils.obtain().createECCDSAKeyPair();
        if (null == key) {
            return 0;
        }
        app.setPublicKey(key.getPublicKey());
        app.setPrivateKey(key.getPrivateKey());
        return appMapper.updateKey(app);
    }

    @Override
    public List<App> list(int id) {
        return appMapper.list(id);
    }

    @Override
    public App get(int id) {
        return appMapper.get(id);
    }

    @Override
    public int delete(int id) {
        return appMapper.delete(id);
    }

    @Override
    public int deleteAll(int id) {
        return appMapper.deleteAll(id);
    }

    @Override
    public int count(int id) {
        return appMapper.count(id);
    }
}