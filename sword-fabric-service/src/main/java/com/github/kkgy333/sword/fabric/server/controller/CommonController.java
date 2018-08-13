package com.github.kkgy333.sword.fabric.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.kkgy333.sword.fabric.server.bean.Home;
import com.github.kkgy333.sword.fabric.server.bean.Trace;
import com.github.kkgy333.sword.fabric.server.bean.Transaction;
import com.github.kkgy333.sword.fabric.server.dao.Chaincode;
import com.github.kkgy333.sword.fabric.server.dao.Channel;
import com.github.kkgy333.sword.fabric.server.dao.User;
import com.github.kkgy333.sword.fabric.server.service.*;
import com.github.kkgy333.sword.fabric.server.utils.CacheUtil;
import com.github.kkgy333.sword.fabric.server.utils.DataUtil;
import com.github.kkgy333.sword.fabric.server.utils.DateUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@RestController
@RequestMapping("")
public class CommonController {

    @Resource
    private CommonService commonService;
    @Resource
    private LeagueService leagueService;
    @Resource
    private OrgService orgService;
    @Resource
    private OrdererService ordererService;
    @Resource
    private PeerService peerService;
    @Resource
    private CAService caService;
    @Resource
    private ChannelService channelService;
    @Resource
    private ChaincodeService chaincodeService;
    @Resource
    private AppService appService;
    @Resource
    private TraceService traceService;
    @Resource
    private BlockService blockService;

    @GetMapping(value = "index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");

        Home home = CacheUtil.getHome();
        if (null == home) {
            home = DataUtil.obtain().home(leagueService, orgService, ordererService,
                    peerService, caService, channelService, chaincodeService,
                    appService, traceService, blockService);
            CacheUtil.putHome(home);
        }

        modelAndView.addObject("leagueCount", home.getLeagueCount());
        modelAndView.addObject("orgCount", home.getOrgCount());
        modelAndView.addObject("ordererCount", home.getOrdererCount());
        modelAndView.addObject("peerCount", home.getPeerCount());
        modelAndView.addObject("caCount", home.getCaCount());
        modelAndView.addObject("channelCount", home.getChannelCount());
        modelAndView.addObject("chaincodeCount", home.getChaincodeCount());
        modelAndView.addObject("appCount", home.getAppCount());
        modelAndView.addObject("blocks", home.getBlocks());
        //中间统计模块开始
        modelAndView.addObject("channelPercents", home.getChannelPercents());
        modelAndView.addObject("channelBlockList", home.getChannelBlockLists());
        modelAndView.addObject("dayStatistics", home.getDayStatistics());
        modelAndView.addObject("platform", home.getPlatform());
        //中间统计模块结束

        return modelAndView;
    }

    @PostMapping(value = "login")
    public ModelAndView submit(@ModelAttribute User user, HttpServletRequest request) {
        try {
            String token = commonService.login(user);
            if (null != token) {
                // 校验通过时，在session里放入一个标识
                // 后续通过session里是否存在该标识来判断用户是否登录
                request.getSession().setAttribute("username", user.getUsername());
                request.getSession().setAttribute("token", token);
                return new ModelAndView(new RedirectView("index"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView(new RedirectView("login"));
    }

    @GetMapping(value = "login")
    public ModelAndView login() {
        User user = new User();
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping(value = "logout")
    public ModelAndView logout(HttpServletRequest request) {
        CacheUtil.removeString((String) request.getSession().getAttribute("username"));
        request.getSession().invalidate();
        return new ModelAndView(new RedirectView("login"));
    }
}