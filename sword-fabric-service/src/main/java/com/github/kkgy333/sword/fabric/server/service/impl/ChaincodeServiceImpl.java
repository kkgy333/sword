package com.github.kkgy333.sword.fabric.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.kkgy333.sword.fabric.sdk.FabricManager;
import com.github.kkgy333.sword.fabric.server.base.BaseService;
import com.github.kkgy333.sword.fabric.server.bean.Api;
import com.github.kkgy333.sword.fabric.server.mapper.*;
import com.github.kkgy333.sword.fabric.server.model.Chaincode;
import com.github.kkgy333.sword.fabric.server.service.ChaincodeService;
import com.github.kkgy333.sword.fabric.server.utils.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Service("chaincodeService")
public class ChaincodeServiceImpl implements ChaincodeService, BaseService {

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
    @Resource
    private AppMapper appMapper;
    @Resource
    private Environment env;

    @Override
    public int add(Chaincode chaincode) {
        if (StringUtils.isEmpty(chaincode.getName()) ||
                StringUtils.isEmpty(chaincode.getPath()) ||
                StringUtils.isEmpty(chaincode.getVersion()) ||
                chaincode.getProposalWaitTime() == 0 ||
                null != chaincodeMapper.check(chaincode)) {
            return 0;
        }
        chaincode.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return chaincodeMapper.add(chaincode);
    }

    @Override
    public JSONObject install(Chaincode chaincode, MultipartFile file, Api api, boolean init) {
        if (verify(chaincode) || null == file || null != chaincodeMapper.check(chaincode)) {
            return responseFailJson("install error, param has none value and source mush be uploaded or had the same chaincode");
        }
        if (!upload(chaincode, file)){
            return responseFailJson("source unzip fail");
        }
        if (chaincodeMapper.add(chaincode) <= 0) {
            return responseFailJson("chaincode add fail");
        }
        chaincode.setId(chaincodeMapper.check(chaincode).getId());
        JSONObject jsonResult = chainCode(chaincode.getId(), orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.INSTALL, new String[]{});
        if (jsonResult.getInteger("code") == BaseService.FAIL) {
            delete(chaincode.getId());
            return jsonResult;
        }
        return instantiate(chaincode, Arrays.asList(api.getExec().split(",")));
    }

    @Override
    public JSONObject upgrade(Chaincode chaincode, MultipartFile file, Api api) {
        if (verify(chaincode) || null == file || null == chaincodeMapper.get(chaincode.getId())) {
            return responseFailJson("install error, param has none value and source mush be uploaded or had no chaincode to upgrade");
        }
        if (!upload(chaincode, file)){
            return responseFailJson("source unzip fail");
        }
        FabricHelper.obtain().removeManager(chaincode.getId());
        if (chaincodeMapper.updateForUpgrade(chaincode) <= 0) {
            return responseFailJson("chaincode updateForUpgrade fail");
        }
        JSONObject jsonResult = chainCode(chaincode.getId(), orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.INSTALL, new String[]{});
        if (jsonResult.getInteger("code") == BaseService.FAIL) {
            delete(chaincode.getId());
            return jsonResult;
        }
        List<String> strArray = Arrays.asList(api.getExec().split(","));
        int size = strArray.size();
        String[] args = new String[size];
        for (int i = 0; i < size; i++) {
            args[i] = strArray.get(i);
        }
        return chainCode(chaincode.getId(), orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.UPGRADE, args);
    }

    @Override
    public JSONObject instantiate(Chaincode chaincode, List<String> strArray) {
        int size = strArray.size();
        String[] args = new String[size];
        for (int i = 0; i < size; i++) {
            args[i] = strArray.get(i);
        }
        return chainCode(chaincode.getId(), orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.INSTANTIATE, args);
    }

    @Override
    public int update(Chaincode chaincode) {
        FabricHelper.obtain().removeManager(chaincode.getId());
        if (chaincode.isOpen()) {
            CacheUtil.putChaincodeId(chaincode.getChannelId(), true);
        } else {
            CacheUtil.removeChaincodeId(chaincode.getChannelId());
        }
        return chaincodeMapper.update(chaincode);
    }

    @Override
    public List<Chaincode> listAll() {
        return chaincodeMapper.listAll();
    }

    @Override
    public List<Chaincode> listById(int id) {
        return chaincodeMapper.list(id);
    }

    @Override
    public Chaincode get(int id) {
        return chaincodeMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return chaincodeMapper.count(id);
    }

    @Override
    public int count() {
        return chaincodeMapper.countAll();
    }

    @Override
    public int delete(int id) {
        return DeleteUtil.obtain().deleteChaincode(id, chaincodeMapper, appMapper);
    }

    @Override
    public int deleteAll(int channelId) {
        List<Chaincode> chaincodes = chaincodeMapper.list(channelId);
        for (Chaincode chaincode : chaincodes) {
            FabricHelper.obtain().removeManager(chaincode.getId());
            chaincodeMapper.delete(chaincode.getId());
        }
        return 0;
    }

    enum ChainCodeIntent {
        INSTALL, INSTANTIATE, UPGRADE
    }

    private JSONObject chainCode(int chaincodeId, OrgMapper orgMapper, ChannelMapper channelMapper, ChaincodeMapper chainCodeMapper,
                                 OrdererMapper ordererMapper, PeerMapper peerMapper, ChainCodeIntent intent, String[] args) {
        Map<String, String> resultMap = null;
        try {
            FabricManager manager = FabricHelper.obtain().get(orgMapper, channelMapper, chainCodeMapper, ordererMapper, peerMapper,
                    chaincodeId);
            switch (intent) {
                case INSTALL:
                    resultMap = manager.install(chainCodeMapper.get(chaincodeId).getVersion());
                    break;
                case INSTANTIATE:
                    resultMap = manager.instantiate(args);
                    break;
                case UPGRADE:
                    resultMap = manager.upgrade(args);
                    break;
            }
            if (resultMap.get("code").equals("error")) {
                return responseFailJson(resultMap.get("data"));
            } else {
                return responseSuccessJson(resultMap.get("data"), resultMap.get("txid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return responseFailJson(String.format("Request failed： %s", e.getMessage()));
        }
    }

    private boolean verify(Chaincode chaincode) {
        return StringUtils.isEmpty(chaincode.getName()) ||
                StringUtils.isEmpty(chaincode.getVersion()) ||
                chaincode.getProposalWaitTime() == 0;
    }

    private boolean upload(Chaincode chaincode, MultipartFile file){
        String chaincodeSource = String.format("%s%s%s%s%s%s%s%s%s%schaincode",
                env.getProperty("config.dir"),
                File.separator,
                chaincode.getLeagueName(),
                File.separator,
                chaincode.getOrgName(),
                File.separator,
                chaincode.getPeerName(),
                File.separator,
                chaincode.getChannelName(),
                File.separator);
        String chaincodePath = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0];
        String childrenPath = String.format("%s%ssrc%s%s", chaincodeSource, File.separator, File.separator, Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0]);
        chaincode.setSource(chaincodeSource);
        chaincode.setPath(chaincodePath);
        chaincode.setPolicy(String.format("%s%spolicy.yaml", childrenPath, File.separator));
        chaincode.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        try {
            FileUtil.unZipAndSave(file, String.format("%s%ssrc", chaincodeSource, File.separator), childrenPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
