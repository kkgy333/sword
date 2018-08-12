package com.github.kkgy333.sword.fabric.server.utils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.kkgy333.sword.fabric.sdk.FabricManager;
import com.github.kkgy333.sword.fabric.sdk.OrgManager;
import com.github.kkgy333.sword.fabric.server.dao.mapper.*;
import com.github.kkgy333.sword.fabric.server.dao.*;
import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Slf4j
public class FabricHelper {

    private static FabricHelper instance;

    public static FabricHelper obtain() {
        if (null == instance) {
            synchronized (FabricHelper.class) {
                if (null == instance) {
                    instance = new FabricHelper();
                }
            }
        }
        return instance;
    }

    private FabricHelper() {
    }

    public void removeChaincodeManager(List<Peer> peers, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper) {
        for (Peer peer : peers) {
            removeChaincodeManager(channelMapper.list(peer.getId()), chaincodeMapper);
        }
    }

    public void removeChaincodeManager(List<Channel> channels, ChaincodeMapper chaincodeMapper) {
        for (Channel channel : channels) {
            removeChaincodeManager(chaincodeMapper.list(channel.getId()));
            removeChannelManager(channel.getId());
        }
    }

    public void removeChaincodeManager(List<Chaincode> chaincodes) {
        for (Chaincode chaincode : chaincodes) {
            CacheUtil.removeStringFabric(chaincode.getCc());
        }
    }

    public void removeChaincodeManager(String cc) {
        CacheUtil.removeStringFabric(cc);
    }

    private void removeChannelManager(int channelId) {
        CacheUtil.removeIntegerFabric(channelId);
    }

    public FabricManager get(OrgMapper orgMapper, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper,
                             OrdererMapper ordererMapper, PeerMapper peerMapper, CA ca, String cc) throws Exception {
        // 尝试从缓存中获取fabricManager
        FabricManager fabricManager = CacheUtil.getStringFabric(cc);
        if (null == fabricManager) { // 如果不存在fabricManager则尝试新建一个并放入缓存
            synchronized (CacheUtil.class) {
                Chaincode chaincode = chaincodeMapper.getByCC(cc);
                log.debug(String.format("chaincode = %s", chaincode.toString()));
                Channel channel = channelMapper.get(chaincode.getChannelId());
                log.debug(String.format("channel = %s", channel.toString()));
                Peer peer = peerMapper.get(channel.getPeerId());
                log.debug(String.format("peer = %s", peer.toString()));
                int orgId = peer.getOrgId();
                List<Peer> peers = peerMapper.list(orgId);
                List<Orderer> orderers = ordererMapper.list(orgId);
                Org org = orgMapper.get(orgId);
                log.debug(String.format("org = %s", org.toString()));
                if (orderers.size() != 0 && peers.size() != 0 && null != ca) {
                    fabricManager = createFabricManager(org, channel, chaincode, orderers, peers, ca, cc);
                    CacheUtil.putStringFabric(cc, fabricManager);
                }
            }
        }
        assert ca != null;
        assert fabricManager != null;
        fabricManager.setUser(ca.getName(), ca.getSkPath(), ca.getCertificatePath());
        return fabricManager;
    }

    public FabricManager get(OrgMapper orgMapper, ChannelMapper channelMapper,
                             OrdererMapper ordererMapper, PeerMapper peerMapper, CA ca, int channelId) throws Exception {
        // 尝试从缓存中获取fabricManager
        FabricManager fabricManager = CacheUtil.getIntegerFabric(channelId);
        if (null == fabricManager) { // 如果不存在fabricManager则尝试新建一个并放入缓存
            synchronized (CacheUtil.class) {
                Channel channel = channelMapper.get(channelId);
                log.debug(String.format("channel = %s", channel.toString()));
                Wrapper<Peer> queryWrapper = new QueryWrapper<Peer>();
                ((QueryWrapper<Peer>) queryWrapper).eq("id",channel.getPeerId());
                Peer peer = peerMapper.selectOne(queryWrapper);
                log.debug(String.format("peer = %s", peer.toString()));
                int orgId = peer.getOrgId();
                Wrapper<Peer> qwPeer = new QueryWrapper<Peer>();
                ((QueryWrapper<Peer>) qwPeer).eq("org_id",orgId);
                List<Peer> peers = peerMapper.selectList(qwPeer);
                Wrapper<Orderer> qwOrderer = new QueryWrapper<Orderer>();
                ((QueryWrapper<Orderer>) qwOrderer).eq("org_id",orgId);
                List<Orderer> orderers = ordererMapper.selectList(qwOrderer);
                Wrapper<Org> qwOrg = new QueryWrapper<Org>();
                ((QueryWrapper<Org>) qwOrg).eq("id",orgId);
                Org org =  orgMapper.selectOne(qwOrg);
                if (orderers.size() != 0 && peers.size() != 0) {
                    fabricManager = createFabricManager(org, channel, null, orderers, peers, ca, String.valueOf(channelId));
                    CacheUtil.putIntegerFabric(channelId, fabricManager);
                }
            }
        }
        return fabricManager;
    }

    private FabricManager createFabricManager(Org org, Channel channel, Chaincode chaincode, List<Orderer> orderers, List<Peer> peers, CA ca, String cacheName) throws Exception {
        OrgManager orgManager = new OrgManager();
        orgManager
                .init(cacheName, org.getMspId(), org.isTls())
                .setUser(ca.getName(), ca.getSkPath(), ca.getCertificatePath())
                .setChannel(channel.getName())
                .setChainCode(null == chaincode ? "" : chaincode.getName(),
                        null == chaincode ? "" : chaincode.getPath(),
                        null == chaincode ? "" : chaincode.getSource(),
                        null == chaincode ? "" : chaincode.getPolicy(),
                        null == chaincode ? "" : chaincode.getVersion(),
                        null == chaincode ? 0 : chaincode.getProposalWaitTime());
        for (Orderer orderer : orderers) {
            orgManager.addOrderer(orderer.getName(), orderer.getLocation(), orderer.getServerCrtPath());
        }
        for (Peer peer : peers) {
            orgManager.addPeer(peer.getName(), peer.getLocation(), peer.getEventHubLocation(), peer.getServerCrtPath());
        }
        if (channel.isBlockListener() && StringUtils.isNotEmpty(channel.getCallbackLocation()) && null == chaincode) {
            orgManager.setBlockListener(jsonObject -> {
                try {
                    HttpUtil.post(channel.getCallbackLocation(), jsonObject.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        if (null != chaincode && chaincode.isChaincodeEventListener() && StringUtils.isNotEmpty(chaincode.getCallbackLocation())
                && StringUtils.isNotEmpty(chaincode.getEvents())) {
            orgManager.setChaincodeEventListener(chaincode.getEvents(), (handle, jsonObject, eventName, chaincodeId, txId) -> {
                log.debug(String.format("handle = %s", handle));
                log.debug(String.format("eventName = %s", eventName));
                log.debug(String.format("chaincodeId = %s", chaincodeId));
                log.debug(String.format("txId = %s", txId));
                log.debug(String.format("code = %s", String.valueOf(jsonObject.getInteger("code"))));
                log.debug(String.format("data = %s", jsonObject.getJSONObject("data").toJSONString()));
                try {
                    jsonObject.put("handle", handle);
                    jsonObject.put("eventName", eventName);
                    jsonObject.put("chaincodeId", chaincodeId);
                    jsonObject.put("txId", txId);
                    HttpUtil.post(chaincode.getCallbackLocation(), jsonObject.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        orgManager.add();
        return orgManager.use(cacheName, ca.getName());
    }

}