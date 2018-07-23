package com.github.kkgy333.sword.fabric.server.service.impl;

import com.github.kkgy333.sword.fabric.server.mapper.*;
import com.github.kkgy333.sword.fabric.server.model.Org;
import com.github.kkgy333.sword.fabric.server.service.OrgService;
import com.github.kkgy333.sword.fabric.server.utils.DateUtil;
import com.github.kkgy333.sword.fabric.server.utils.DeleteUtil;
import com.github.kkgy333.sword.fabric.server.utils.FabricHelper;
import com.github.kkgy333.sword.fabric.server.utils.FileUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Service("orgService")
public class OrgServiceImpl implements OrgService {


    @Resource
    private OrgMapper orgMapper;
    @Resource
    private Environment env;
    @Resource
    private LeagueMapper leagueMapper;
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
    public int add(Org org, MultipartFile file) {
        if (StringUtils.isEmpty(org.getName()) ||
                StringUtils.isEmpty(org.getMspId()) ||
                StringUtils.isEmpty(org.getDomainName()) ||
                StringUtils.isEmpty(org.getOrdererDomainName()) ||
                StringUtils.isEmpty(org.getUsername()) ||
                null == file) {
            return 0;
        }
        org.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        String parentPath = String.format("%s%s%s%s%s",
                env.getProperty("config.dir"),
                File.separator,
                leagueMapper.get(org.getLeagueId()).getName(),
                File.separator,
                org.getName());
        String childrenPath = parentPath + File.separator + Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0];
        org.setCryptoConfigDir(childrenPath);
        try {
            FileUtil.unZipAndSave(file, parentPath, childrenPath);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return orgMapper.add(org);
    }

    @Override
    public int update(Org org, MultipartFile file) {
        if (null != file) {
            String parentPath = String.format("%s%s%s%s%s",
                    env.getProperty("config.dir"),
                    File.separator,
                    leagueMapper.get(org.getLeagueId()).getName(),
                    File.separator,
                    org.getName());
            String childrenPath = parentPath + File.separator + Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0];
            org.setCryptoConfigDir(childrenPath);
            try {
                FileUtil.unZipAndSave(file, parentPath, childrenPath);
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }
        FabricHelper.obtain().removeManager(peerMapper.list(org.getId()), channelMapper, chaincodeMapper);
        return orgMapper.update(org);
    }

    @Override
    public List<Org> listAll() {
        return orgMapper.listAll();
    }

    @Override
    public List<Org> listById(int id) {
        return orgMapper.list(id);
    }

    @Override
    public Org get(int id) {
        return orgMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return orgMapper.count(id);
    }

    @Override
    public int count() {
        return orgMapper.countAll();
    }

    @Override
    public int delete(int id) {
        return DeleteUtil.obtain().deleteOrg(id, orgMapper, ordererMapper, peerMapper, channelMapper, chaincodeMapper, appMapper);
    }

}
