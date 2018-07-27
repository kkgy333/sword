package com.github.kkgy333.sword.fabric.server.utils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.kkgy333.sword.fabric.sdk.FabricManager;
import com.github.kkgy333.sword.fabric.sdk.OrgManager;
import com.github.kkgy333.sword.fabric.server.mapper.*;
import com.github.kkgy333.sword.fabric.server.model.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public class FabricHelper {

    /** 当前正在运行的智能合约Id */
    private int chainCodeId;

    private static FabricHelper instance;

    private final Map<Integer, FabricManager> fabricManagerMap;

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
        fabricManagerMap = new LinkedHashMap<>();
    }

    public void removeManager(List<Peer> peers, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper) {
        for (Peer peer : peers) {
            List<Channel> channels = channelMapper.list(peer.getId());
            for (Channel channel : channels) {
                List<Chaincode> chaincodes = chaincodeMapper.list(channel.getId());
                for (Chaincode chaincode : chaincodes) {
                    fabricManagerMap.remove(chaincode.getId());
                }
            }
        }
    }

    public void removeManager(List<Channel> channels, ChaincodeMapper chaincodeMapper) {
        for (Channel channel : channels) {
            List<Chaincode> chaincodes = chaincodeMapper.list(channel.getId());
            for (Chaincode chaincode : chaincodes) {
                fabricManagerMap.remove(chaincode.getId());
            }
        }
    }

    public void removeManager(int chainCodeId) {
        fabricManagerMap.remove(chainCodeId);
    }

    public FabricManager get(OrgMapper orgMapper, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper,
                             OrdererMapper ordererMapper, PeerMapper peerMapper) throws Exception {
        return get(orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, -1);
    }

    public FabricManager get(OrgMapper orgMapper, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper,
                             OrdererMapper ordererMapper, PeerMapper peerMapper, int chaincodeId) throws Exception {
        if (chaincodeId == -1) {
            chaincodeId = this.chainCodeId;
        } else {
            this.chainCodeId = chaincodeId;
        }

        // 尝试从缓存中获取fabricManager
        FabricManager fabricManager = fabricManagerMap.get(chaincodeId);
        if (null == fabricManager) { // 如果不存在fabricManager则尝试新建一个并放入缓存
            synchronized (fabricManagerMap) {
                Chaincode chaincode = chaincodeMapper.get(chaincodeId);
//                log.debug(String.format("chaincode = %s", chaincode.toString()));
                Channel channel = channelMapper.get(chaincode.getChannelId());
//                log.debug(String.format("channel = %s", channel.toString()));


                Wrapper<Peer> queryWrapper = new QueryWrapper<Peer>();
                ((QueryWrapper<Peer>) queryWrapper).eq("id",channel.getPeerId());
                Peer peer = peerMapper.selectOne(queryWrapper);
//                log.debug(String.format("peer = %s", peer.toString()));
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
//                log.debug(String.format("org = %s", org.toString()));
                if (orderers.size() != 0 && peers.size() != 0) {
                    fabricManager = createFabricManager(org, channel, chaincode, orderers, peers);
                    fabricManagerMap.put(chaincodeId, fabricManager);
                }
            }
        }
        return fabricManager;
    }


    private FabricManager createFabricManager(Org org, Channel channel, Chaincode chainCode, List<Orderer> orderers, List<Peer> peers) throws Exception {
        OrgManager orgManager = new OrgManager();
        orgManager
                .init(chainCodeId, org.isTls())
                .setUser(org.getUsername(), org.getCryptoConfigDir())
                .setPeers(org.getName(), org.getMspId(), org.getDomainName())
                .setOrderers(org.getOrdererDomainName())
                .setChannel(channel.getName())
                .setChainCode(chainCode.getName(), chainCode.getPath(), chainCode.getSource(), chainCode.getPolicy(), chainCode.getVersion(), chainCode.getProposalWaitTime())
                .setBlockListener(map -> {
//                    log.debug(map.get("code"));
//                    log.debug(map.get("data"));
                });
        for (Orderer orderer : orderers) {
            orgManager.addOrderer(orderer.getName(), orderer.getLocation());
        }
        for (Peer peer : peers) {
            orgManager.addPeer(peer.getName(), peer.getEventHubName(), peer.getLocation(), peer.getEventHubLocation(), peer.isEventListener());
        }
        orgManager.add();
        return orgManager.use(chainCodeId);
    }

}