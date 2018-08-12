package com.github.kkgy333.sword.fabric.server.service.impl;

import com.github.kkgy333.sword.fabric.sdk.FabricManager;
import com.github.kkgy333.sword.fabric.server.base.BaseService;
import com.github.kkgy333.sword.fabric.server.bean.State;
import com.github.kkgy333.sword.fabric.server.mapper.*;
import com.github.kkgy333.sword.fabric.server.service.StateService;
import com.github.kkgy333.sword.fabric.server.utils.FabricHelper;
import com.github.kkgy333.sword.fabric.server.utils.VerifyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;

    @Override
    public String invoke(State state) {
        return chaincodeByVerify(state, ChainCodeIntent.INVOKE);
    }

    @Override
    public String query(State state) {
        return chaincodeByVerify(state, ChainCodeIntent.QUERY);
    }


    enum ChainCodeIntent {
        INVOKE, QUERY
    }

    private String chaincodeByVerify(State state, ChainCodeIntent intent) {
        if (VerifyUtil.unRequest(state, chaincodeMapper, appMapper)) {
            return responseFail("app key is invalid");
        }
        return chaincode(state, intent);
    }

    private String chaincode(State state, ChainCodeIntent intent) {
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
        return chaincodeExec(state, intent, fcn, argArray);
    }

    private String chaincodeExec(State state, ChainCodeIntent intent, String fcn, String[] argArray) {
        Map<String, String> resultMap = null;
        try {
            FabricManager manager = FabricHelper.obtain().get(orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper,
                    state.getId());
            switch (intent) {
                case INVOKE:
                    resultMap = manager.invoke(fcn, argArray);
                    break;
                case QUERY:
                    resultMap = manager.query(fcn, argArray, state.getVersion());
                    break;
            }
            if (resultMap.get("code").equals("error")) {
                return responseFail(resultMap.get("data"));
            } else {
                return responseSuccess(resultMap.get("data"), resultMap.get("txid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("Request failed： %s", e.getMessage()));
        }
    }

}
