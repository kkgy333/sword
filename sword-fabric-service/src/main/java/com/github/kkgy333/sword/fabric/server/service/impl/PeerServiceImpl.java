package com.github.kkgy333.sword.fabric.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class PeerServiceImpl extends ServiceImpl<PeerMapper, Peer> implements PeerService {

    @Resource
    private PeerMapper peerMapper;
//    @Resource
//    private CAMapper caMapper;
//    @Resource
//    private ChannelMapper channelMapper;
//    @Resource
//    private ChaincodeMapper chaincodeMapper;
//    @Resource
//    private AppMapper appMapper;
    @Resource
    private Environment env;

    @Override
    public boolean add(Peer peer, MultipartFile serverCrtFile) {
        if (StringUtils.isEmpty(peer.getName()) ||
                StringUtils.isEmpty(peer.getLocation()) ||
                StringUtils.isEmpty(peer.getEventHubLocation())) {
            return false;
        }
        if (StringUtils.isNotEmpty(serverCrtFile.getOriginalFilename())) {
            if (saveFileFail(peer, serverCrtFile)) {
                return false;
            }
        }
        peer.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        return super.save(peer);
    }

    @Override
    public boolean update(Peer peer, MultipartFile serverCrtFile) {
//        FabricHelper.obtain().removeChaincodeManager(channelMapper.list(peer.getId()), chaincodeMapper);
//        CacheUtil.removeFlagCA(peer.getId(), caMapper);
        if (null == serverCrtFile) {
            return peerMapper.updateWithNoFile(peer);
        }
        if (saveFileFail(peer, serverCrtFile)) {
            return false;
        }
        return super.updateById(peer);
    }

    @Override
    public List<Peer> listAll() {
        return super.list(null);
    }

    @Override
    public List<Peer> listById(int id) {
        Wrapper<Peer> queryWrapper = new QueryWrapper<Peer>();
        ((QueryWrapper<Peer>) queryWrapper).eq("org_id",id);
        return super.list(queryWrapper);
    }

    @Override
    public Peer get(int id) {
        Wrapper<Peer> queryWrapper = new QueryWrapper<Peer>();
        ((QueryWrapper<Peer>) queryWrapper).eq("id",id);
        return super.getOne(queryWrapper);
    }

    @Override
    public int countById(int id) {
        Wrapper<Peer> queryWrapper = new QueryWrapper<Peer>();
        ((QueryWrapper<Peer>) queryWrapper).eq("org_id",id);
        return super.count(queryWrapper);
    }

    @Override
    public int count() {
        return super.count(null);
    }

    @Override
    public boolean delete(int id) {
        //return DeleteUtil.obtain().deletePeer(id, peerMapper, caMapper, channelMapper, chaincodeMapper, appMapper);
        return false;
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