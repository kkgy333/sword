package com.github.kkgy333.sword.fabric.server.service.impl;

import com.github.kkgy333.sword.fabric.server.dao.mapper.*;
import com.github.kkgy333.sword.fabric.server.dao.*;
import com.github.kkgy333.sword.fabric.server.service.OrgService;
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
@Service("orgService")
public class OrgServiceImpl implements OrgService {

    @Resource
    private OrgMapper orgMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private CAMapper caMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private AppMapper appMapper;


    @Override
    public int add(Org org) {
        if (StringUtils.isEmpty(org.getMspId())) {
            return 0;
        }
        org.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        return orgMapper.insert(org);
    }

    @Override
    public int update(Org org) {
        FabricHelper.obtain().removeChaincodeManager(peerMapper.list(org.getId()), channelMapper, chaincodeMapper);
        return orgMapper.updateById(org);
    }

    @Override
    public List<Org> listAll() {
        return orgMapper.selectList(null);
    }

    @Override
    public List<Org> listById(int id) {

        Wrapper<Org> queryWrapper = new QueryWrapper<Org>();
        ((QueryWrapper<Org>) queryWrapper).eq("league_id",id);
        return orgMapper.selectList(queryWrapper);
    }

    @Override
    public Org get(int id) {

        Wrapper<Org> queryWrapper = new QueryWrapper<Org>();
        ((QueryWrapper<Org>) queryWrapper).eq("id",id);
        return orgMapper.selectOne(queryWrapper);

    }

    @Override
    public int countById(int id) {

        Wrapper<Org> queryWrapper = new QueryWrapper<Org>();
        ((QueryWrapper<Org>) queryWrapper).eq("league_id",id);
        return orgMapper.selectCount(queryWrapper);
    }

    @Override
    public int count() {
        return orgMapper.selectCount(null);

    }

    @Override
    public int delete(int id) {
        return DeleteUtil.obtain().deleteOrg(id, orgMapper, ordererMapper, peerMapper, caMapper, channelMapper, chaincodeMapper, appMapper);
    }

}
