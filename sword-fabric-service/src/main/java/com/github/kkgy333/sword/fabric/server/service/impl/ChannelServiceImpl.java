package com.github.kkgy333.sword.fabric.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.kkgy333.sword.fabric.server.dao.mapper.*;
import com.github.kkgy333.sword.fabric.server.dao.*;
import com.github.kkgy333.sword.fabric.server.service.ChannelService;
import com.github.kkgy333.sword.fabric.server.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("channelService")
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, Channel> implements ChannelService {

    @Resource
    private ChannelMapper channelMapper;
//    @Resource
//    private ChaincodeMapper chaincodeMapper;
//    @Resource
//    private AppMapper appMapper;

    @Override
    public boolean add(Channel channel) {
        if (StringUtils.isEmpty(channel.getName())) {
            log.debug("channel name is empty");
            return false;
        }
        if (null != channelMapper.check(channel)) {
            log.debug("had the same channel in this peer");
            return false;
        }
        channel.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        return super.save(channel);
    }

    @Override
    public boolean update(Channel channel) {
//        FabricHelper.obtain().removeChaincodeManager(chaincodeMapper.list(channel.getId()));
        return super.updateById(channel);
    }

    @Override
    public List<Channel> listAll() {
        return super.list(null);
    }

    @Override
    public List<Channel> listById(int id) {
        Wrapper<Channel> queryWrapper = new QueryWrapper<Channel>();
        ((QueryWrapper<Channel>) queryWrapper).eq("peer_id",id);
        return super.list(queryWrapper);
    }

    @Override
    public Channel get(int id) {
        Wrapper<Channel> queryWrapper = new QueryWrapper<Channel>();
        ((QueryWrapper<Channel>) queryWrapper).eq("id",id);
        return super.getOne(queryWrapper);
    }

    @Override
    public int countById(int id) {
        Wrapper<Channel> queryWrapper = new QueryWrapper<Channel>();
        ((QueryWrapper<Channel>) queryWrapper).eq("peer_id",id);
        return super.count(queryWrapper);
    }

    @Override
    public int count() {
        return super.count(null);
    }

    @Override
    public boolean delete(int id) {
        //return DeleteUtil.obtain().deleteChannel(id, channelMapper, chaincodeMapper, appMapper);
        return false;
    }
}
