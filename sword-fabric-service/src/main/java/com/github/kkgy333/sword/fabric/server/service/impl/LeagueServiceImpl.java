package com.github.kkgy333.sword.fabric.server.service.impl;

import com.github.kkgy333.sword.fabric.server.mapper.*;
import com.github.kkgy333.sword.fabric.server.model.League;
import com.github.kkgy333.sword.fabric.server.service.LeagueService;
import com.github.kkgy333.sword.fabric.server.utils.DateUtil;
import com.github.kkgy333.sword.fabric.server.utils.DeleteUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Service("leagueService")
public class LeagueServiceImpl implements LeagueService {

    @Resource
    private LeagueMapper leagueMapper;
    @Resource
    private OrgMapper orgMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private AppMapper appMapper;

    @Override
    public int add(League leagueInfo) {
        if (StringUtils.isEmpty(leagueInfo.getName())) {
            return 0;
        }
        leagueInfo.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return leagueMapper.add(leagueInfo);
    }

    @Override
    public int update(League leagueInfo) {
        return leagueMapper.update(leagueInfo);
    }

    @Override
    public List<League> listAll() {
        return leagueMapper.listAll();
    }

    @Override
    public League get(int id) {
        return leagueMapper.get(id);
    }

    @Override
    public int delete(int id) {
        return DeleteUtil.obtain().deleteLeague(id, leagueMapper, orgMapper, ordererMapper, peerMapper, channelMapper, chaincodeMapper, appMapper);
    }

}
