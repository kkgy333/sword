package com.github.kkgy333.sword.fabric.server.utils;

import com.github.kkgy333.sword.fabric.server.mapper.*;
import com.github.kkgy333.sword.fabric.server.model.Chaincode;
import com.github.kkgy333.sword.fabric.server.model.Channel;
import com.github.kkgy333.sword.fabric.server.model.Org;
import com.github.kkgy333.sword.fabric.server.model.Peer;

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
        List<Org> orgs = orgMapper.list(leagueId);
        for (Org org : orgs) {
            if (deleteOrg(org.getId(), orgMapper, ordererMapper, peerMapper, channelMapper, chaincodeMapper, appMapper) <= 0) {
                return 0;
            }
        }
        return leagueMapper.delete(leagueId);
    }

    public int deleteOrg(int orgId, OrgMapper orgMapper, OrdererMapper ordererMapper,
                         PeerMapper peerMapper, ChannelMapper channelMapper,
                         ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        List<Peer> peers = peerMapper.list(orgId);
        for (Peer peer : peers) {
            if (deletePeer(peer.getId(), peerMapper, channelMapper, chaincodeMapper, appMapper) <= 0) {
                return 0;
            }
        }
        if (ordererMapper.deleteAll(orgId) <= 0) {
            return 0;
        }
        return orgMapper.delete(orgId);
    }

    public int deletePeer(int peerId, PeerMapper peerMapper, ChannelMapper channelMapper,
                          ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        List<Channel> channels = channelMapper.list(peerId);
        for (Channel channel : channels) {
            if (deleteChannel(channel.getId(), channelMapper, chaincodeMapper, appMapper) <= 0) {
                return 0;
            }
        }
        return peerMapper.delete(peerId);
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