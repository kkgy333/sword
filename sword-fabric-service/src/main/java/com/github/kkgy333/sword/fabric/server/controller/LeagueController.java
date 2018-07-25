package com.github.kkgy333.sword.fabric.server.controller;

import com.github.kkgy333.sword.fabric.server.model.League;
import com.github.kkgy333.sword.fabric.server.service.LeagueService;
import com.github.kkgy333.sword.fabric.server.service.OrgService;
import com.github.kkgy333.sword.fabric.server.utils.SpringUtil;
import com.github.kkgy333.sword.fabric.server.utils.VerifyUtil;
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
@RequestMapping("league")
public class LeagueController {

    @Resource
    private OrgService orgService;
    @Resource
    private LeagueService leagueService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute League league,
                               @RequestParam("intent") String intent,
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                leagueService.add(league);
                break;
            case "edit":
                league.setId(id);
                leagueService.update(league);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("leagueSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("enter"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("versions", VerifyUtil.versions());
        modelAndView.addObject("league", new League());
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("leagueSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        modelAndView.addObject("versions", VerifyUtil.versions());
        modelAndView.addObject("league", leagueService.get(id));
        return modelAndView;
    }

    @GetMapping(value = "delete")
    public ModelAndView delete(@RequestParam("id") int id) {
        leagueService.delete(id);
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("leagues");
        List<League> leagues = leagueService.listAll();
        for (League league : leagues) {
            league.setOrgCount(orgService.countById(league.getId()));
        }
        modelAndView.addObject("leagues", leagues);
        return modelAndView;
    }

}
