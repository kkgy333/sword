package com.github.kkgy333.sword.fabric.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.kkgy333.sword.fabric.server.dao.mapper.ChaincodeMapper;
import com.github.kkgy333.sword.fabric.server.dao.mapper.ChannelMapper;
import com.github.kkgy333.sword.fabric.server.dao.mapper.OrdererMapper;
import com.github.kkgy333.sword.fabric.server.dao.mapper.PeerMapper;
import com.github.kkgy333.sword.fabric.server.dao.Orderer;
import com.github.kkgy333.sword.fabric.server.dao.Org;
import com.github.kkgy333.sword.fabric.server.dao.Peer;
import com.github.kkgy333.sword.fabric.server.service.OrdererService;
import com.github.kkgy333.sword.fabric.server.utils.DateUtil;
import com.github.kkgy333.sword.fabric.server.utils.FabricHelper;
import com.github.kkgy333.sword.fabric.server.utils.FileUtil;
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
public class OrdererServiceImpl implements OrdererService {

    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private Environment env;

    @Override
    public int add(Orderer orderer, MultipartFile serverCrtFile) {
        if (StringUtils.isEmpty(orderer.getName()) ||
                StringUtils.isEmpty(orderer.getLocation())) {
            return 0;
        }
        if (StringUtils.isNotEmpty(serverCrtFile.getOriginalFilename())) {
            if (saveFileFail(orderer, serverCrtFile)) {
                return 0;
            }
        }
        orderer.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        return ordererMapper.insert(orderer);
    }

    @Override
    public int update(Orderer orderer, MultipartFile serverCrtFile) {
        FabricHelper.obtain().removeChaincodeManager(peerMapper.list(orderer.getOrgId()), channelMapper, chaincodeMapper);
        if (null == serverCrtFile) {
            return ordererMapper.updateWithNoFile(orderer);
        }
        if (saveFileFail(orderer, serverCrtFile)) {
            return 0;
        }
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