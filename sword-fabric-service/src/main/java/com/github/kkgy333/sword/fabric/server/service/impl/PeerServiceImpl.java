package com.github.kkgy333.sword.fabric.server.service.impl;

import com.github.kkgy333.sword.fabric.server.mapper.AppMapper;
import com.github.kkgy333.sword.fabric.server.mapper.ChaincodeMapper;
import com.github.kkgy333.sword.fabric.server.mapper.ChannelMapper;
import com.github.kkgy333.sword.fabric.server.mapper.PeerMapper;
import com.github.kkgy333.sword.fabric.server.model.Peer;
import com.github.kkgy333.sword.fabric.server.service.PeerService;
import com.github.kkgy333.sword.fabric.server.utils.DateUtil;
import com.github.kkgy333.sword.fabric.server.utils.DeleteUtil;
import com.github.kkgy333.sword.fabric.server.utils.FabricHelper;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Service("peerService")
public class PeerServiceImpl implements PeerService {

    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private AppMapper appMapper;


    @Override
    public int add(Peer peer) {
        if (StringUtils.isEmpty(peer.getName()) ||
                StringUtils.isEmpty(peer.getLocation()) ||
                StringUtils.isEmpty(peer.getEventHubName()) ||
                StringUtils.isEmpty(peer.getEventHubLocation())) {
            return 0;
        }
        peer.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return peerMapper.add(peer);
    }

    @Override
    public int update(Peer peer) {
        FabricHelper.obtain().removeManager(peerMapper.list(peer.getOrgId()), channelMapper, chaincodeMapper);
        return peerMapper.update(peer);
    }

    @Override
    public List<Peer> listAll() {
        return peerMapper.listAll();
    }

    @Override
    public List<Peer> listById(int id) {
        return peerMapper.list(id);
    }

    @Override
    public Peer get(int id) {
        return peerMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return peerMapper.count(id);
    }

    @Override
    public int count() {
        return peerMapper.countAll();
    }

    @Override
    public int delete(int id) {
        return DeleteUtil.obtain().deletePeer(id, peerMapper, channelMapper, chaincodeMapper, appMapper);
    }
}