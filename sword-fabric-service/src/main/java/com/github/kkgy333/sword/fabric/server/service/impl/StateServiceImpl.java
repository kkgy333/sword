package com.github.kkgy333.sword.fabric.server.service.impl;

import com.github.kkgy333.sword.fabric.sdk.FabricManager;
import com.github.kkgy333.sword.fabric.server.base.BaseService;
import com.github.kkgy333.sword.fabric.server.bean.State;
import com.github.kkgy333.sword.fabric.server.dao.CA;
import com.github.kkgy333.sword.fabric.server.dao.mapper.*;
import com.github.kkgy333.sword.fabric.server.service.StateService;
import com.github.kkgy333.sword.fabric.server.utils.CacheUtil;
import com.github.kkgy333.sword.fabric.server.utils.FabricHelper;
import com.github.kkgy333.sword.fabric.server.utils.VerifyUtil;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Service("stateService")
public class StateServiceImpl implements StateService, BaseService {

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
    public String invoke(State state) {
        return chaincode(state, ChainCodeIntent.INVOKE, CacheUtil.getFlagCA(state.getFlag(), caMapper));
    }

    @Override
    public String query(State state) {
        return chaincode(state, ChainCodeIntent.QUERY, CacheUtil.getFlagCA(state.getFlag(), caMapper));
    }


    enum ChainCodeIntent {
        INVOKE, QUERY
    }

    private String chaincode(State state, ChainCodeIntent intent, CA ca) {
        String cc = VerifyUtil.getCc(state, chaincodeMapper, appMapper);
        if (StringUtils.isEmpty(cc)) {
            return responseFail("Request failed：app key is invalid");
        }
        List<String> array = state.getStrArray();
        int length = array.size();
        String fcn = null;
        String[] argArray = new String[length - 1];
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                fcn = array.get(i);
            } else {
                argArray[i - 1] = array.get(i);
            }
        }
        return chaincodeExec(intent, ca, cc, fcn, argArray);
    }

    private String chaincodeExec(ChainCodeIntent intent, CA ca, String cc, String fcn, String[] argArray) {
        JSONObject jsonObject = null;
        try {
            FabricManager manager = FabricHelper.obtain().get(orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper,
                    ca, cc);
            switch (intent) {
                case INVOKE:
                    jsonObject = manager.invoke(fcn, argArray);
                    break;
                case QUERY:
                    jsonObject = manager.query(fcn, argArray);
                    break;
            }
            return jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("Request failed： %s", e.getMessage()));
        }
    }

}
