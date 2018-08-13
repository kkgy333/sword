package com.github.kkgy333.sword.fabric.server.utils;

import com.github.kkgy333.sword.fabric.sdk.FabricManager;
import com.github.kkgy333.sword.fabric.server.bean.App;
import com.github.kkgy333.sword.fabric.server.bean.Home;
import com.github.kkgy333.sword.fabric.server.dao.CA;
import com.github.kkgy333.sword.fabric.server.dao.Peer;
import com.github.kkgy333.sword.fabric.server.dao.mapper.AppMapper;
import com.github.kkgy333.sword.fabric.server.dao.mapper.CAMapper;
import com.github.kkgy333.sword.fabric.server.dao.mapper.PeerMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public class CacheUtil {

    private static Cache<String, String> cacheString = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterAccess(12, TimeUnit.HOURS).build();

    /** 存储 flag，ca */
    private static Cache<String, CA> cacheFlagCA = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterAccess(12, TimeUnit.HOURS).build();

    /** 存储 app，bool */
    private static Cache<String, Boolean> cacheAppBool = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterAccess(12, TimeUnit.HOURS).build();

    /** 存储 cc，fabric-manager*/
    private static Cache<String, FabricManager> cacheStringFabric = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterAccess(12, TimeUnit.HOURS).build();

    /** 存储 channelId，fabric-manager*/
    private static Cache<Integer, FabricManager> cacheIntegerFabric = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterAccess(12, TimeUnit.HOURS).build();

    /** 存储 channelId，fabric-manager*/
    private static Cache<String, Home> cacheHome = CacheBuilder.newBuilder().maximumSize(1)
            .expireAfterAccess(5, TimeUnit.MINUTES).build();

    public static void putString(String key, String value) {
        cacheString.put(key, value);
    }

    public static String getString(String key) {
        try {
            return cacheString.getIfPresent(key);
        } catch (Exception e) {
            return "";
        }
    }

    public static void removeString(String key) {
        cacheString.invalidate(key);
    }

    private static void putFlagCA(String flag, CA ca) {
        cacheFlagCA.put(flag, ca);
    }

    public static CA getFlagCA(String flag, CAMapper caMapper) {
        CA ca = cacheFlagCA.getIfPresent(flag);
        if (null == ca) {
            ca = caMapper.getByFlag(flag);
            if (null == ca) {
                return null;
            } else {
                putFlagCA(flag, ca);
            }
        }
        return ca;
    }

    public static void removeFlagCA(int leagueId, PeerMapper peerMapper, CAMapper caMapper) {
        List<Peer> peers = peerMapper.list(leagueId);
        for (Peer peer : peers) {
            removeFlagCA(peer.getId(), caMapper);
        }
    }

    public static void removeFlagCA(int peerId, CAMapper caMapper) {
        List<CA> cas = caMapper.list(peerId);
        for (CA ca : cas) {
            removeFlagCA(ca.getFlag());
        }
    }

    public static void removeFlagCA(String flag) {
        cacheFlagCA.invalidate(flag);
    }

    private static void putAppBool(String key, boolean value) {
        cacheAppBool.put(key, value);
    }

    static boolean getAppBool(String key, AppMapper appMapper) {
        try {
            return cacheAppBool.getIfPresent(key);
        } catch (Exception e) {
            App app = appMapper.getByKey(key);
            if (null == app) {
                return false;
            }
            boolean flag = app.isActive();
            if (flag) {
                putAppBool(key, true);
            } else {
                putAppBool(key, false);
            }
            return flag;
        }
    }

    public static void removeAppBool(String key) {
        cacheAppBool.invalidate(key);
    }


    static void putStringFabric(String key, FabricManager value) {
        cacheStringFabric.put(key, value);
    }

    static FabricManager getStringFabric(String key) {
        try {
            return cacheStringFabric.getIfPresent(key);
        } catch (Exception e) {
            return null;
        }
    }

    static void removeStringFabric(String key) {
        cacheStringFabric.invalidate(key);
    }

    static void putIntegerFabric(int key, FabricManager value) {
        cacheIntegerFabric.put(key, value);
    }

    static FabricManager getIntegerFabric(int key) {
        try {
            return cacheIntegerFabric.getIfPresent(key);
        } catch (Exception e) {
            return null;
        }
    }

    static void removeIntegerFabric(int key) {
        cacheIntegerFabric.invalidate(key);
    }

    public static void putHome(Home value) {
        cacheHome.put("do-home-cache", value);
    }

    public static Home getHome() {
        try {
            return cacheHome.getIfPresent("do-home-cache");
        } catch (Exception e) {
            return null;
        }
    }

}