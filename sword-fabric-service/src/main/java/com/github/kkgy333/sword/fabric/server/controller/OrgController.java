package com.github.kkgy333.sword.fabric.server.controller;

import com.github.kkgy333.sword.fabric.server.model.Org;
import com.github.kkgy333.sword.fabric.server.service.LeagueService;
import com.github.kkgy333.sword.fabric.server.service.OrgService;
import com.github.kkgy333.sword.fabric.server.service.PeerService;
import com.github.kkgy333.sword.fabric.server.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@CrossOrigin
@RestController
@RequestMapping("org")
public class OrgController {

    @Resource
    private OrgService orgService;
    @Resource
    private LeagueService leagueService;
    @Resource
    private PeerService peerService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute Org org,
                               @RequestParam("intent") String intent,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                orgService.add(org, file);
                break;
            case "edit":
                org.setId(id);
                orgService.update(org, file);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("orgSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("enter"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("orgVO", new Org());
        modelAndView.addObject("leagues", leagueService.listAll());
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("orgSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        modelAndView.addObject("orgVO", orgService.get(id));
        modelAndView.addObject("leagues", leagueService.listAll());
        return modelAndView;
    }

    @GetMapping(value = "delete")
    public ModelAndView delete(@RequestParam("id") int id) {
        orgService.delete(id);
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("orgs");
        List<Org> orgs = new ArrayList<>(orgService.listAll());
        for (Org org : orgs) {
            org.setOrdererCount(orgService.countById(org.getId()));
            org.setPeerCount(peerService.countById(org.getId()));
            org.setLeagueName(leagueService.get(org.getLeagueId()).getName());
        }
        modelAndView.addObject("orgs", orgs);
        return modelAndView;
    }

}