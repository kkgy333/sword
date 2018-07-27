package com.github.kkgy333.sword.fabric.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.kkgy333.sword.fabric.server.mapper.ChaincodeMapper;
import com.github.kkgy333.sword.fabric.server.mapper.ChannelMapper;
import com.github.kkgy333.sword.fabric.server.mapper.OrdererMapper;
import com.github.kkgy333.sword.fabric.server.mapper.PeerMapper;
import com.github.kkgy333.sword.fabric.server.model.Orderer;
import com.github.kkgy333.sword.fabric.server.model.Org;
import com.github.kkgy333.sword.fabric.server.model.Peer;
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
        return ordererMapper.insert(orderer);
    }

    @Override
    public int update(Orderer orderer) {
        Wrapper<Peer> queryWrapper = new QueryWrapper<Peer>();
        ((QueryWrapper<Peer>) queryWrapper).eq("org_id",orderer.getOrgId());
        FabricHelper.obtain().removeManager(peerMapper.selectList(queryWrapper), channelMapper, chaincodeMapper);
        return ordererMapper.updateById(orderer);
    }

    @Override
    public List<Orderer> listAll() {
        return ordererMapper.selectList(null);
    }

    @Override
    public List<Orderer> listById(int id) {
        Wrapper<Orderer> queryWrapper = new QueryWrapper<Orderer>();
        ((QueryWrapper<Orderer>) queryWrapper).eq("org_id",id);
        return ordererMapper.selectList(queryWrapper);
    }

    @Override
    public Orderer get(int id) {

        Wrapper<Orderer> queryWrapper = new QueryWrapper<Orderer>();
        ((QueryWrapper<Orderer>) queryWrapper).eq("id",id);
        return ordererMapper.selectOne(queryWrapper);

    }

    @Override
    public int countById(int id) {

        Wrapper<Orderer> queryWrapper = new QueryWrapper<Orderer>();
        ((QueryWrapper<Orderer>) queryWrapper).eq("org_id",id);
        return ordererMapper.selectCount(queryWrapper);
    }

    @Override
    public int count() {
        return ordererMapper.selectCount(null);
    }

    @Override
    public int delete(int id) {
        return ordererMapper.deleteById(id);
    }
}