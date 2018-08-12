package com.github.kkgy333.sword.fabric.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.kkgy333.sword.fabric.server.dao.mapper.*;
import com.github.kkgy333.sword.fabric.server.dao.*;
import com.github.kkgy333.sword.fabric.server.service.PeerService;
import com.github.kkgy333.sword.fabric.server.utils.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
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
    private CAMapper caMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private AppMapper appMapper;
    @Resource
    private Environment env;

    @Override
    public int add(Peer peer, MultipartFile serverCrtFile) {
        if (StringUtils.isEmpty(peer.getName()) ||
                StringUtils.isEmpty(peer.getLocation()) ||
                StringUtils.isEmpty(peer.getEventHubLocation())) {
            return 0;
        }
        if (StringUtils.isNotEmpty(serverCrtFile.getOriginalFilename())) {
            if (saveFileFail(peer, serverCrtFile)) {
                return 0;
            }
        }
        peer.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        return peerMapper.insert(peer);
    }

    @Override
    public int update(Peer peer, MultipartFile serverCrtFile) {
        FabricHelper.obtain().removeChaincodeManager(channelMapper.list(peer.getId()), chaincodeMapper);
        CacheUtil.removeFlagCA(peer.getId(), caMapper);
        if (null == serverCrtFile) {
            return peerMapper.updateWithNoFile(peer);
        }
        if (saveFileFail(peer, serverCrtFile)) {
            return 0;
        }
        return peerMapper.updateById(peer);
    }

    @Override
    public List<Peer> listAll() {
        return peerMapper.listAll();
    }

    @Override
    public List<Peer> listById(int id) {
        Wrapper<Peer> queryWrapper = new QueryWrapper<Peer>();
        ((QueryWrapper<Peer>) queryWrapper).eq("org_id",id);
        return peerMapper.selectList(queryWrapper);
    }

    @Override
    public Peer get(int id) {
        Wrapper<Peer> queryWrapper = new QueryWrapper<Peer>();
        ((QueryWrapper<Peer>) queryWrapper).eq("id",id);
        return peerMapper.selectOne(queryWrapper);
    }

    @Override
    public int countById(int id) {
        Wrapper<Peer> queryWrapper = new QueryWrapper<Peer>();
        ((QueryWrapper<Peer>) queryWrapper).eq("org_id",id);
        return peerMapper.selectCount(queryWrapper);
    }

    @Override
    public int count() {
        return peerMapper.selectCount(null);
    }

    @Override
    public int delete(int id) {
        return DeleteUtil.obtain().deletePeer(id, peerMapper, caMapper, channelMapper, chaincodeMapper, appMapper);
    }

    private boolean saveFileFail(Peer peer, MultipartFile serverCrtFile) {
        String peerTlsPath = String.format("%s%s%s%s%s%s%s%s",
                env.getProperty("config.dir"),
                File.separator,
                peer.getLeagueName(),
                File.separator,
                peer.getOrgName(),
                File.separator,
                peer.getName(),
                File.separator);
        String serverCrtPath = String.format("%s%s", peerTlsPath, serverCrtFile.getOriginalFilename());
        peer.setServerCrtPath(serverCrtPath);
        try {
            FileUtil.save(serverCrtFile, serverCrtPath);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }
}