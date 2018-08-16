package com.github.kkgy333.sword.fabric.server.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.kkgy333.sword.fabric.server.dao.*;
import com.github.kkgy333.sword.fabric.server.dao.mapper.OrdererMapper;
import com.github.kkgy333.sword.fabric.server.dao.mapper.PeerMapper;
import com.github.kkgy333.sword.fabric.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    private UserService userService;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private OrdererService ordererService;


    @Autowired
    private PeerService peerService;


    @Resource
    private OrdererMapper ordererMapper;

    @Resource
    private PeerMapper peerMapper;



    @GetMapping("/User")
    public List<User> User() {
        User user1 = new User();
        user1.setUsername("test1");
        user1.setPassword("test1");
        userService.add(user1);
        user1.setPassword("pwd");
        userService.update(user1);

        User user2 = new User();
        user2.setUsername("test2");
        user2.setPassword("test2");
        userService.add(user2);

        Wrapper<User> queryWrapper = new QueryWrapper<User>();
        ((QueryWrapper<User>) queryWrapper).eq("username","test2");

        userService.remove(queryWrapper);


        return userService.list(null);
    }


    @GetMapping("/League")
    public List<League> League() {
        League league1 = new League();
        league1.setName("test1");
        league1.setDate("test1");
        leagueService.add(league1);
        league1.setDate("date");
        leagueService.update(league1);

        League league2 = new League();
        league2.setName("test2");
        league2.setDate("test2");
        leagueService.add(league2);

        Wrapper<League> queryWrapper = new QueryWrapper<League>();
        ((QueryWrapper<League>) queryWrapper).eq("name","test2");

        leagueService.remove(queryWrapper);


        return leagueService.list(null);
    }


    @GetMapping("/Org")
    public List<Org> Org() {

        Org org1 = new Org();
        org1.setMspId("mspId1");
        org1.setTls(true);
        org1.setLeagueId(1);
        org1.setDate("date1");
        orgService.add(org1);
        org1.setDate("date");
        orgService.update(org1);

        Org org2 = new Org();
        org2.setMspId("mspId2");
        org2.setTls(true);
        org2.setLeagueId(1);
        org2.setDate("date2");
        orgService.add(org2);

        Wrapper<Org> queryWrapper = new QueryWrapper<Org>();
        ((QueryWrapper<Org>) queryWrapper).eq("msp_id","mspId2");

        orgService.remove(queryWrapper);


        return orgService.list(null);
    }



    @GetMapping("/Orderer")
    public List<Orderer> Orderer() {

        Orderer orderer1 = new Orderer();
        orderer1.setName("name1");
        orderer1.setLocation("location");
        orderer1.setOrgId(1);
        orderer1.setDate("date1");
        orderer1.setServerCrtPath("Path");
        ordererService.save(orderer1);
        orderer1.setDate("date");
        ordererService.updateById(orderer1);

        Orderer orderer2 = new Orderer();
        orderer2.setName("name2");
        orderer2.setLocation("location2");
        orderer2.setOrgId(2);
        orderer2.setDate("date2");
        orderer2.setServerCrtPath("Path2");
        ordererService.save(orderer2);

        orderer2.setName("name3");

        ordererMapper.updateWithNoFile(orderer2);



        return ordererService.list(null);
    }




    @GetMapping("/Peer")
    public List<Peer> Peer() {

        Peer peer1 = new Peer();
        peer1.setName("name1");
        peer1.setLocation("location");
        peer1.setOrgId(1);
        peer1.setDate("date1");
        peer1.setServerCrtPath("Path");
        peer1.setEventHubLocation("EventHubLocation");
        peerService.save(peer1);
        peer1.setDate("date");
        peerService.updateById(peer1);

        Peer peer2 = new Peer();
        peer2.setName("name2");
        peer2.setLocation("location2");
        peer2.setOrgId(2);
        peer2.setDate("date2");
        peer2.setServerCrtPath("Path2");
        peer2.setEventHubLocation("EventHubLocation");
        peerService.save(peer2);

        peer2.setName("name3");

        peerMapper.updateWithNoFile(peer2);



        return peerService.list(null);
    }

}
