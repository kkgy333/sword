package com.github.kkgy333.sword.fabric.server.utils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.kkgy333.sword.fabric.server.dao.mapper.*;
import com.github.kkgy333.sword.fabric.server.dao.*;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public class DeleteUtil {

    private static DeleteUtil instance;

    public static DeleteUtil obtain() {
        if (null == instance) {
            synchronized (DeleteUtil.class) {
                if (null == instance) {
                    instance = new DeleteUtil();
                }
            }
        }
        return instance;
    }

    public int deleteLeague(int leagueId, LeagueMapper leagueMapper, OrgMapper orgMapper,
                            OrdererMapper ordererMapper, PeerMapper peerMapper, CAMapper caMapper,
                            ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        List<Org> orgs = orgMapper.list(leagueId);
        for (Org org : orgs) {
            deleteOrg(org.getId(), orgMapper, ordererMapper, peerMapper, caMapper, channelMapper, chaincodeMapper, appMapper);
        }
        return leagueMapper.delete(leagueId);
    }

    public int deleteOrg(int orgId, OrgMapper orgMapper, OrdererMapper ordererMapper,
                         PeerMapper peerMapper, CAMapper caMapper, ChannelMapper channelMapper,
                         ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        List<Peer> peers = peerMapper.list(orgId);
        for (Peer peer : peers) {
            deletePeer(peer.getId(), peerMapper, caMapper, channelMapper, chaincodeMapper, appMapper);
        }
        ordererMapper.deleteAll(orgId);
        return orgMapper.delete(orgId);
    }

    public int deletePeer(int peerId, PeerMapper peerMapper, CAMapper caMapper, ChannelMapper channelMapper,
                          ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        List<Channel> channels = channelMapper.list(peerId);
        for (Channel channel : channels) {
            deleteChannel(channel.getId(), channelMapper, chaincodeMapper, appMapper);
        }
        caMapper.deleteAll(peerId);
        return peerMapper.delete(peerId);
    }

    public int deleteChannel(int channelId, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        List<Chaincode> chaincodes = chaincodeMapper.list(channelId);
        for (Chaincode chaincode : chaincodes) {
            deleteChaincode(chaincode.getId(), chaincodeMapper, appMapper);
        }
        return channelMapper.delete(channelId);
    }

    public int deleteChaincode(int chaincodeId, ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        appMapper.deleteAll(chaincodeId);
        FabricHelper.obtain().removeChaincodeManager(chaincodeMapper.get(chaincodeId).getCc());
        return chaincodeMapper.delete(chaincodeId);
    }

}