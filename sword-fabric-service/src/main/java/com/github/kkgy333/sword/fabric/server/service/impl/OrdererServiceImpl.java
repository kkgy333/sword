package com.github.kkgy333.sword.fabric.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.kkgy333.sword.fabric.server.dao.mapper.*;
import com.github.kkgy333.sword.fabric.server.dao.*;
import com.github.kkgy333.sword.fabric.server.service.LeagueService;
import com.github.kkgy333.sword.fabric.server.service.OrdererService;
import com.github.kkgy333.sword.fabric.server.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service("ordererService")
public class OrdererServiceImpl extends ServiceImpl<OrdererMapper, Orderer> implements OrdererService {

    //    @Resource
//    private PeerMapper peerMapper;

//    @Resource
//    private PeerMapper peerMapper;
//    @Resource
//    private ChannelMapper channelMapper;
//    @Resource
//    private ChaincodeMapper chaincodeMapper;
    @Resource
    private Environment env;

    @Override
    public boolean add(Orderer orderer, MultipartFile serverCrtFile) {
        if (StringUtils.isEmpty(orderer.getName()) ||
                StringUtils.isEmpty(orderer.getLocation())) {
            return false;
        }
        if (StringUtils.isNotEmpty(serverCrtFile.getOriginalFilename())) {
            if (saveFileFail(orderer, serverCrtFile)) {
                return false;
            }
        }
        orderer.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        return super.save(orderer);
    }

    @Override
    public boolean update(Orderer orderer, MultipartFile serverCrtFile) {
        //FabricHelper.obtain().removeChaincodeManager(peerMapper.list(orderer.getOrgId()), channelMapper, chaincodeMapper);
        if (null == serverCrtFile) {
            return this.baseMapper.updateWithNoFile(orderer);
        }
        if (saveFileFail(orderer, serverCrtFile)) {
            return false;
        }
        return super.updateById(orderer);
    }

    @Override
    public List<Orderer> listAll() {
        return super.list(null);
    }

    @Override
    public List<Orderer> listById(int id) {
        Wrapper<Orderer> queryWrapper = new QueryWrapper<Orderer>();
        ((QueryWrapper<Orderer>) queryWrapper).eq("org_id",id);
        return super.list(queryWrapper);
    }

    @Override
    public Orderer get(int id) {

        Wrapper<Orderer> queryWrapper = new QueryWrapper<Orderer>();
        ((QueryWrapper<Orderer>) queryWrapper).eq("id",id);
        return super.getOne(queryWrapper);

    }

    @Override
    public int countById(int id) {

        Wrapper<Orderer> queryWrapper = new QueryWrapper<Orderer>();
        ((QueryWrapper<Orderer>) queryWrapper).eq("org_id",id);
        return super.count(queryWrapper);
    }

    @Override
    public int count() {
        return super.count(null);
    }

    @Override
    public boolean delete(int id) {
        Wrapper<Orderer> queryWrapper = new QueryWrapper<Orderer>();
        ((QueryWrapper<Orderer>) queryWrapper).eq("id",id);
        return super.remove(queryWrapper);
    }

    private boolean saveFileFail(Orderer orderer, MultipartFile serverCrtFile) {
        String ordererTlsPath = String.format("%s%s%s%s%s%s%s%s",
                env.getProperty("config.dir"),
                File.separator,
                orderer.getLeagueName(),
                File.separator,
                orderer.getOrgName(),
                File.separator,
                orderer.getName(),
                File.separator);
        String serverCrtPath = String.format("%s%s", ordererTlsPath, serverCrtFile.getOriginalFilename());
        orderer.setServerCrtPath(serverCrtPath);
        try {
            FileUtil.save(serverCrtFile, serverCrtPath);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

}