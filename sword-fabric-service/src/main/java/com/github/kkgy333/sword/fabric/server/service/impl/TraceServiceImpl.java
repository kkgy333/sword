package com.github.kkgy333.sword.fabric.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.kkgy333.sword.fabric.sdk.FabricManager;
import com.github.kkgy333.sword.fabric.server.base.BaseService;
import com.github.kkgy333.sword.fabric.server.bean.Trace;
import com.github.kkgy333.sword.fabric.server.mapper.*;
import com.github.kkgy333.sword.fabric.server.service.TraceService;
import com.github.kkgy333.sword.fabric.server.utils.FabricHelper;
import com.github.kkgy333.sword.fabric.server.utils.VerifyUtil;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Service("traceService")
public class TraceServiceImpl implements TraceService, BaseService {

    @Resource
    private AppMapper appMapper;
    @Resource
    private OrgMapper orgMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;

    @Override
    public String queryBlockByTransactionID(Trace trace) {
        return traceByVerify(trace, TraceIntent.TRANSACTION);
    }

    @Override
    public String queryBlockByHash(Trace trace) {
        return traceByVerify(trace, TraceIntent.HASH);
    }

    @Override
    public String queryBlockByNumber(Trace trace) {
        return traceByVerify(trace, TraceIntent.NUMBER);
    }

    @Override
    public String queryBlockChainInfo(int id, String key) {
        Trace trace = new Trace();
        trace.setId(id);
        trace.setKey(key);
        return traceByVerify(trace, TraceIntent.INFO);
    }

    @Override
    public String queryBlockByNumberForIndex(Trace trace) {
        return trace(trace, TraceIntent.NUMBER);
    }

    @Override
    public String queryBlockChainInfoForIndex(int id) {
        Trace trace = new Trace();
        trace.setId(id);
        return trace(trace, TraceIntent.INFO);
    }

    enum TraceIntent {
        TRANSACTION, HASH, NUMBER, INFO
    }

    private String traceByVerify(Trace trace, TraceIntent intent) {
        if (VerifyUtil.unRequest(trace, chaincodeMapper, appMapper)) {
            return responseFail("app key is invalid");
        }
        return trace(trace, intent);
    }

    private String trace(Trace trace, TraceIntent intent) {
        Map<String, String> resultMap = null;
        try {
            FabricManager manager = FabricHelper.obtain().get(orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper,
                    trace.getId());
            switch (intent) {
                case TRANSACTION:
                    resultMap = manager.queryBlockByTransactionID(trace.getTrace());
                    break;
                case HASH:
                    resultMap = manager.queryBlockByHash(Hex.decodeHex(trace.getTrace().toCharArray()));
                    break;
                case NUMBER:
                    resultMap = manager.queryBlockByNumber(Long.valueOf(trace.getTrace()));
                    break;
                case INFO:
                    resultMap = manager.getBlockchainInfo();
                    break;
            }
            return responseSuccess(JSONObject.parseObject(resultMap.get("data")));
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("Request failed： %s", e.getMessage()));
        }
    }
}