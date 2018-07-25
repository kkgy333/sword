package com.github.kkgy333.sword.fabric.server.controller;

import com.github.kkgy333.sword.fabric.server.bean.State;
import com.github.kkgy333.sword.fabric.server.service.StateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@CrossOrigin
@RestController
@RequestMapping("state")
public class StateController {

    @Resource
    private StateService stateService;

    @PostMapping(value = "invoke")
    public String invoke(@RequestBody State state) {
        return stateService.invoke(state);
    }

    @PostMapping(value = "query")
    public String query(@RequestBody State state) {
        return stateService.query(state);
    }

}
