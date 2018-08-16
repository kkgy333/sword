package com.github.kkgy333.sword.fabric.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.kkgy333.sword.fabric.server.dao.mapper.*;
import com.github.kkgy333.sword.fabric.server.dao.*;
import com.github.kkgy333.sword.fabric.server.service.OrgService;
import com.github.kkgy333.sword.fabric.server.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Service("orgService")
public class OrgServiceImpl extends ServiceImpl<OrgMapper, Org> implements  OrgService {


//    @Resource
//    private PeerMapper peerMapper;
//    @Resource
//    private CAMapper caMapper;
//    @Resource
//    private OrdererMapper ordererMapper;
//    @Resource
//    private ChannelMapper channelMapper;
//    @Resource
//    private ChaincodeMapper chaincodeMapper;
//    @Resource
//    private AppMapper appMapper;


    @Override
    public boolean add(Org org) {
        if (StringUtils.isEmpty(org.getMspId())) {
            return false;
        }
        org.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        return super.save(org);
    }

    @Override
    public boolean update(Org org) {
        //FabricHelper.obtain().removeChaincodeManager(peerMapper.list(org.getId()), channelMapper, chaincodeMapper);
        return super.updateById(org);
    }

    @Override
    public List<Org> listAll() {
        return super.list(null);
    }

    @Override
    public List<Org> listById(int id) {

        Wrapper<Org> queryWrapper = new QueryWrapper<Org>();
        ((QueryWrapper<Org>) queryWrapper).eq("league_id",id);
        return super.list(queryWrapper);
    }

    @Override
    public Org get(int id) {

        Wrapper<Org> queryWrapper = new QueryWrapper<Org>();
        ((QueryWrapper<Org>) queryWrapper).eq("id",id);
        return super.getOne(queryWrapper);

    }

    @Override
    public int countById(int id) {

        Wrapper<Org> queryWrapper = new QueryWrapper<Org>();
        ((QueryWrapper<Org>) queryWrapper).eq("league_id",id);
        return super.count(queryWrapper);
    }

    @Override
    public int count() {
        return super.count(null);

    }

    @Override
    public boolean delete(int id) {
        //return DeleteUtil.obtain().deleteOrg(id, orgMapper, ordererMapper, peerMapper, caMapper, channelMapper, chaincodeMapper, appMapper);
        return false;
    }

}
