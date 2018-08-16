package com.github.kkgy333.sword.fabric.server.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.kkgy333.sword.fabric.server.dao.mapper.*;
import com.github.kkgy333.sword.fabric.server.dao.League;
import com.github.kkgy333.sword.fabric.server.service.LeagueService;
import com.github.kkgy333.sword.fabric.server.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Service("leagueService")
public class LeagueServiceImpl extends ServiceImpl<LeagueMapper, League> implements LeagueService {


//    @Resource
//    private OrgMapper orgMapper;
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
    public boolean add(League leagueInfo) {
        if (StringUtils.isEmpty(leagueInfo.getName())) {
            return false;
        }
        leagueInfo.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        return super.save(leagueInfo);
    }

    @Override
    public boolean update(League leagueInfo) {
        return super.updateById(leagueInfo);
    }

    @Override
    public List<League> listAll() {
        return super.list(null);
    }

    @Override
    public League get(int id) {
        Wrapper<League> ew = new QueryWrapper<League>();
        ((QueryWrapper<League>) ew).eq("id",id);
        League league = super.getOne(ew);
        return league;
    }

    @Override
    public boolean delete(int id) {
        //return DeleteUtil.obtain().deleteLeague(id, leagueMapper, orgMapper, ordererMapper, peerMapper, caMapper, channelMapper, chaincodeMapper, appMapper);
        return false;
    }

}
