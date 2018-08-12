package com.github.kkgy333.sword.fabric.server.controller;

import com.github.kkgy333.sword.fabric.server.bean.Trace;
import com.github.kkgy333.sword.fabric.server.service.TraceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@CrossOrigin
@RestController
@RequestMapping("trace")
public class TraceController {

    @Resource
    private TraceService traceService;

    @GetMapping(value = "txid")
    public String queryBlockByTransactionID(@RequestBody Trace trace) {
        return traceService.queryBlockByTransactionID(trace);
    }

    @GetMapping(value = "hash")
    public String queryBlockByHash(@RequestBody Trace trace) {
        return traceService.queryBlockByHash(trace);
    }

    @GetMapping(value = "number")
    public String queryBlockByNumber(@RequestBody Trace trace) {
        return traceService.queryBlockByNumber(trace);
    }

    @GetMapping(value = "info/{id}/{key}")
    public String queryBlockChainInfo(@PathVariable("id") int id, @PathVariable("key") String key) {
        return traceService.queryBlockChainInfo(id, key);
    }



    @GetMapping(value = "test")
    public String test() {
        return traceService.test();
    }

}
