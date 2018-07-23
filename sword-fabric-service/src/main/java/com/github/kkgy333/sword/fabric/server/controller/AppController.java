package com.github.kkgy333.sword.fabric.server.controller;

import com.github.kkgy333.sword.fabric.server.bean.App;
import com.github.kkgy333.sword.fabric.server.service.AppService;
import com.github.kkgy333.sword.fabric.server.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@CrossOrigin
@RestController
@RequestMapping("app")
public class AppController {

    @Resource
    private AppService appService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute App app,
                               @RequestParam("intent") String intent,
                               @RequestParam("chaincodeId") int chaincodeId) {
        switch (intent) {
            case "add":
                appService.add(app, chaincodeId);
                break;
            case "edit":
                appService.update(app);
                break;
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("id", chaincodeId);
        return new ModelAndView(new RedirectView("list"), map);
    }

    @GetMapping(value = "add")
    public ModelAndView add(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("appSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("new_app"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("app", new App());
        modelAndView.addObject("chaincodeId", chaincodeId);
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("appSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        App app = appService.get(id);
        modelAndView.addObject("app", app);
        modelAndView.addObject("chaincodeId", app.getChaincodeId());
        return modelAndView;
    }

    @GetMapping(value = "delete")
    public ModelAndView delete(@RequestParam("id") int id, @RequestParam("chaincodeId") int chaincodeId) {
        appService.delete(id);
        Map<String, Integer> map = new HashMap<>();
        map.put("id", chaincodeId);
        return new ModelAndView(new RedirectView("list"), map);
    }

    @GetMapping(value = "refresh")
    public ModelAndView refresh(@RequestParam("id") int id, @RequestParam("chaincodeId") int chaincodeId) {
        appService.updateKey(id);
        Map<String, Integer> map = new HashMap<>();
        map.put("id", chaincodeId);
        return new ModelAndView(new RedirectView("list"), map);
    }

    @GetMapping(value = "list")
    public ModelAndView list(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("apps");
        List<App> apps = appService.list(chaincodeId);
        modelAndView.addObject("apps", apps);
        modelAndView.addObject("chaincodeId", chaincodeId);
        return modelAndView;
    }

}