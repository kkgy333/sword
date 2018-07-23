package com.github.kkgy333.sword.fabric.server.service.impl;

import com.github.kkgy333.sword.fabric.server.mapper.AppMapper;
import com.github.kkgy333.sword.fabric.server.mapper.ChaincodeMapper;
import com.github.kkgy333.sword.fabric.server.mapper.ChannelMapper;
import com.github.kkgy333.sword.fabric.server.model.Channel;
import com.github.kkgy333.sword.fabric.server.service.ChannelService;
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
@Service("channelService")
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private AppMapper appMapper;

    @Override
    public int add(Channel channel) {
        if (StringUtils.isEmpty(channel.getName())) {
            //log.debug("channel name is empty");
            return 0;
        }
        if (null != channelMapper.check(channel)) {
//            log.debug("had the same channel in this peer");
            return 0;
        }
        channel.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return channelMapper.add(channel);
    }

    @Override
    public int update(Channel channel) {
        FabricHelper.obtain().removeManager(channelMapper.list(channel.getPeerId()), chaincodeMapper);
        return channelMapper.update(channel);
    }

    @Override
    public List<Channel> listAll() {
        return channelMapper.listAll();
    }

    @Override
    public List<Channel> listById(int id) {
        return channelMapper.list(id);
    }

    @Override
    public Channel get(int id) {
        return channelMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return channelMapper.count(id);
    }

    @Override
    public int count() {
        return channelMapper.countAll();
    }

    @Override
    public int delete(int id) {
        return DeleteUtil.obtain().deleteChannel(id, channelMapper, chaincodeMapper, appMapper);
    }
}
