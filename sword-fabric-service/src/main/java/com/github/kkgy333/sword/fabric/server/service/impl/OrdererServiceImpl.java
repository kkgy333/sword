package com.github.kkgy333.sword.fabric.server.service.impl;

import com.github.kkgy333.sword.fabric.server.mapper.ChaincodeMapper;
import com.github.kkgy333.sword.fabric.server.mapper.ChannelMapper;
import com.github.kkgy333.sword.fabric.server.mapper.OrdererMapper;
import com.github.kkgy333.sword.fabric.server.mapper.PeerMapper;
import com.github.kkgy333.sword.fabric.server.model.Orderer;
import com.github.kkgy333.sword.fabric.server.service.OrdererService;
import com.github.kkgy333.sword.fabric.server.utils.DateUtil;
import com.github.kkgy333.sword.fabric.server.utils.FabricHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Service("ordererService")
public class OrdererServiceImpl implements OrdererService {

    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;

    @Override
    public int add(Orderer orderer) {
        if (StringUtils.isEmpty(orderer.getName()) ||
                StringUtils.isEmpty(orderer.getLocation())) {
            return 0;
        }
        orderer.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return ordererMapper.add(orderer);
    }

    @Override
    public int update(Orderer orderer) {
        FabricHelper.obtain().removeManager(peerMapper.list(orderer.getOrgId()), channelMapper, chaincodeMapper);
        return ordererMapper.update(orderer);
    }

    @Override
    public List<Orderer> listAll() {
        return ordererMapper.listAll();
    }

    @Override
    public List<Orderer> listById(int id) {
        return ordererMapper.list(id);
    }

    @Override
    public Orderer get(int id) {
        return ordererMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return ordererMapper.count(id);
    }

    @Override
    public int count() {
        return ordererMapper.countAll();
    }

    @Override
    public int delete(int id) {
        return ordererMapper.delete(id);
    }
}