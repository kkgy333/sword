package com.github.kkgy333.sword.fabric.server.utils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.kkgy333.sword.fabric.server.mapper.*;
import com.github.kkgy333.sword.fabric.server.model.*;

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
                            OrdererMapper ordererMapper, PeerMapper peerMapper,
                            ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        Wrapper<Org> queryWrapper = new QueryWrapper<Org>();
        ((QueryWrapper<Org>) queryWrapper).eq("league_id",leagueId);
        List<Org> orgs =  orgMapper.selectList(queryWrapper);
        for (Org org : orgs) {
            if (deleteOrg(org.getId(), orgMapper, ordererMapper, peerMapper, channelMapper, chaincodeMapper, appMapper) <= 0) {
                return 0;
            }
        }
        return leagueMapper.deleteById(leagueId);
    }

    public int deleteOrg(int orgId, OrgMapper orgMapper, OrdererMapper ordererMapper,
                         PeerMapper peerMapper, ChannelMapper channelMapper,
                         ChaincodeMapper chaincodeMapper, AppMapper appMapper) {

        Wrapper<Peer> qwPeer = new QueryWrapper<Peer>();
        ((QueryWrapper<Peer>) qwPeer).eq("org_id",orgId);
        List<Peer> peers = peerMapper.selectList(qwPeer);
        for (Peer peer : peers) {
            if (deletePeer(peer.getId(), peerMapper, channelMapper, chaincodeMapper, appMapper) <= 0) {
                return 0;
            }
        }

        Wrapper<Orderer> qwOrderer = new QueryWrapper<Orderer>();
        ((QueryWrapper<Orderer>) qwOrderer).eq("org_id",orgId);

        if (ordererMapper.delete(qwOrderer) <= 0) {
            return 0;
        }
        return orgMapper.deleteById(orgId);
    }

    public int deletePeer(int peerId, PeerMapper peerMapper, ChannelMapper channelMapper,
                          ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        List<Channel> channels = channelMapper.list(peerId);
        for (Channel channel : channels) {
            if (deleteChannel(channel.getId(), channelMapper, chaincodeMapper, appMapper) <= 0) {
                return 0;
            }
        }
        return peerMapper.deleteById(peerId);
    }

    public int deleteChannel(int channelId, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        List<Chaincode> chaincodes = chaincodeMapper.list(channelId);
        for (Chaincode chaincode : chaincodes) {
            if (deleteChaincode(chaincode.getId(), chaincodeMapper, appMapper) <= 0) {
                return 0;
            }
        }
        return channelMapper.delete(channelId);
    }

    public int deleteChaincode(int chaincodeId, ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        if (appMapper.deleteAll(chaincodeId) <= 0) {
            return 0;
        }
        FabricHelper.obtain().removeManager(chaincodeId);
        return chaincodeMapper.delete(chaincodeId);
    }

}