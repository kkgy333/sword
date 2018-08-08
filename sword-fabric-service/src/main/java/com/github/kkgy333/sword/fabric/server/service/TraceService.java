package com.github.kkgy333.sword.fabric.server.service;

import com.github.kkgy333.sword.fabric.server.bean.Trace;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public interface TraceService {

    String queryBlockByTransactionID(Trace trace);

    String queryBlockByHash(Trace trace);

    String queryBlockByNumber(Trace trace);

    String queryBlockChainInfo(int id, String key);

    String queryBlockByNumberForIndex(Trace trace);

    String queryBlockChainInfoForIndex(int id);

    String test();
}
