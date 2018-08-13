package com.github.kkgy333.sword.fabric.server.controller;

import com.github.kkgy333.sword.fabric.server.dao.Channel;
import com.github.kkgy333.sword.fabric.server.dao.League;
import com.github.kkgy333.sword.fabric.server.dao.Org;
import com.github.kkgy333.sword.fabric.server.dao.Peer;
import com.github.kkgy333.sword.fabric.server.service.*;
import com.github.kkgy333.sword.fabric.server.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@CrossOrigin
@RestController
@RequestMapping("channel")
public class ChannelController {

    @Resource
    private ChannelService channelService;
    @Resource
    private PeerService peerService;
    @Resource
    private OrgService orgService;
    @Resource
    private LeagueService leagueService;
    @Resource
    private ChaincodeService chaincodeService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute Channel channel,
                               @RequestParam("intent") String intent,
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                channelService.add(channel);
                break;
            case "edit":
                channel.setId(id);
                channelService.update(channel);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("channelSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("enter"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("channel", new Channel());
        List<Peer> peers = peerService.listAll();
        for (Peer peer : peers) {
            Org org = orgService.get(peer.getOrgId());
            peer.setOrgName(org.getMspId());
            League league = leagueService.get(org.getLeagueId());
            peer.setLeagueName(league.getName());
        }
        modelAndView.addObject("peers", peers);
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("channelSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        Channel channel = channelService.get(id);
        Org org = orgService.get(peerService.get(channel.getPeerId()).getOrgId());
        channel.setOrgName(org.getMspId());
        List<Peer> peers = peerService.listById(org.getId());
        League league = leagueService.get(orgService.get(org.getId()).getLeagueId());
        channel.setLeagueName(league.getName());
        for (Peer peer : peers) {
            peer.setLeagueName(league.getName());
            peer.setOrgName(org.getMspId());
        }
        modelAndView.addObject("channel", channel);
        modelAndView.addObject("peers", peers);
        return modelAndView;
    }

    @GetMapping(value = "delete")
    public ModelAndView delete(@RequestParam("id") int id) {
        channelService.delete(id);
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("channels");
        List<Channel> channels = channelService.listAll();
        for (Channel channel : channels) {
            channel.setPeerName(peerService.get(channel.getPeerId()).getName());
            channel.setChaincodeCount(chaincodeService.countById(channel.getId()));
        }
        modelAndView.addObject("channels", channels);
        return modelAndView;
    }

}