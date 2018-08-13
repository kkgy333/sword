package com.github.kkgy333.sword.fabric.server.service.impl;

import com.github.kkgy333.sword.fabric.sdk.FabricManager;
import com.github.kkgy333.sword.fabric.server.base.BaseService;
import com.github.kkgy333.sword.fabric.server.bean.Trace;
import com.github.kkgy333.sword.fabric.server.dao.CA;
import com.github.kkgy333.sword.fabric.server.dao.mapper.*;
import com.github.kkgy333.sword.fabric.server.service.TraceService;
import com.github.kkgy333.sword.fabric.server.utils.FabricHelper;
import com.github.kkgy333.sword.fabric.server.utils.VerifyUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

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
    private CAMapper caMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;

    @Override
    public String queryBlockByTransactionID(Trace trace) {
        return traceCC(trace, TraceIntent.TRANSACTION, caMapper.getByFlag(trace.getFlag()));
    }

    @Override
    public String queryBlockByHash(Trace trace) {
        return traceCC(trace, TraceIntent.HASH, caMapper.getByFlag(trace.getFlag()));
    }

    @Override
    public String queryBlockByNumber(Trace trace) {
        return traceCC(trace, TraceIntent.NUMBER, caMapper.getByFlag(trace.getFlag()));
    }

    @Override
    public String queryBlockChainInfo(String cc, String key, CA ca) {
        Trace trace = new Trace();
        trace.setChannelId(chaincodeMapper.getByCC(cc).getChannelId());
        trace.setKey(key);
        return traceCC(trace, TraceIntent.INFO, ca);
    }

    @Override
    public String queryBlockByNumberForIndex(Trace trace) {
        return trace(trace, TraceIntent.NUMBER, caMapper.list(channelMapper.get(trace.getChannelId()).getPeerId()).get(0));
    }

    @Override
    public String queryBlockChainInfoForIndex(int channelId) {
        Trace trace = new Trace();
        trace.setChannelId(channelId);
        return trace(trace, TraceIntent.INFO, caMapper.list(channelMapper.get(channelId).getPeerId()).get(0));
    }

    enum TraceIntent {
        TRANSACTION, HASH, NUMBER, INFO
    }

    private String traceCC(Trace trace, TraceIntent intent, CA ca) {
        String cc = VerifyUtil.getCc(trace, chaincodeMapper, appMapper);
        if (StringUtils.isEmpty(cc)) {
            return responseFail("Request failed：app key is invalid");
        }
        try {
            FabricManager manager = FabricHelper.obtain().get(orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper,
                    ca, cc);
            return trace(manager, trace, intent);
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("Request failed： %s", e.getMessage()));
        }
    }

    private String trace(Trace trace, TraceIntent intent, CA ca) {
        try {
            FabricManager manager = FabricHelper.obtain().get(orgMapper, channelMapper, ordererMapper, peerMapper,
                    ca, trace.getChannelId());
            return trace(manager, trace, intent);
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("Request failed： %s", e.getMessage()));
        }
    }

    private String trace(FabricManager manager, Trace trace, TraceIntent intent) throws ProposalException, IOException, InvalidArgumentException, DecoderException {
        JSONObject jsonObject = null;
        switch (intent) {
            case TRANSACTION:
                jsonObject = manager.queryBlockByTransactionID(trace.getTrace());
                break;
            case HASH:
                jsonObject = manager.queryBlockByHash(Hex.decodeHex(trace.getTrace().toCharArray()));
                break;
            case NUMBER:
                jsonObject = manager.queryBlockByNumber(Long.valueOf(trace.getTrace()));
                break;
            case INFO:
                jsonObject = manager.getBlockchainInfo();
                break;
        }
        return jsonObject.toJSONString();
    }

}