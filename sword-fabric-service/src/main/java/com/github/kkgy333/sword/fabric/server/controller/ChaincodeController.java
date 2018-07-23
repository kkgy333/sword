package com.github.kkgy333.sword.fabric.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.kkgy333.sword.fabric.server.bean.Api;
import com.github.kkgy333.sword.fabric.server.bean.State;
import com.github.kkgy333.sword.fabric.server.bean.Trace;
import com.github.kkgy333.sword.fabric.server.model.*;
import com.github.kkgy333.sword.fabric.server.service.*;
import com.github.kkgy333.sword.fabric.server.utils.SpringUtil;
import com.github.kkgy333.sword.fabric.server.utils.VerifyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.github.kkgy333.sword.fabric.server.bean.Api.Intent.INSTANTIATE;
import static com.github.kkgy333.sword.fabric.server.bean.Api.Intent.INVOKE;
import static com.github.kkgy333.sword.fabric.server.bean.Api.Intent.UPGRADE;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@CrossOrigin
@RestController
@RequestMapping("chaincode")
public class ChaincodeController {

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
    @Resource
    private StateService stateService;
    @Resource
    private TraceService traceService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute Chaincode chaincode,
                               @ModelAttribute Api api,
                               @RequestParam("init") boolean init,
                               @RequestParam("intent") String intent,
                               @RequestParam(value = "sourceFile", required = false) MultipartFile sourceFile,
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                chaincodeService.add(chaincode);
                break;
            case "edit":
                chaincode.setId(id);
                chaincodeService.update(chaincode);
                break;
            case "install":
                chaincode = resetChaincode(chaincode);
                chaincodeService.install(chaincode, sourceFile, api, init);
                break;
            case "upgrade":
                Chaincode chaincode1 = chaincodeService.get(id);
                chaincode1 = resetChaincode(chaincode1);
                chaincode1.setVersion(chaincode.getVersion());
                chaincodeService.upgrade(chaincode1, sourceFile, api);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @PostMapping(value = "verify")
    public ModelAndView verify(@ModelAttribute Api api, @RequestParam("chaincodeId") int id) {
        ModelAndView modelAndView = new ModelAndView("chaincodeResult");
        Api.Intent intent = Api.Intent.get(api.getIndex());
        String result = "";
        String url = String.format("http://localhost:port/%s", Objects.requireNonNull(intent).getApiUrl());
        modelAndView.addObject("url", url);
        switch (intent) {
            case INVOKE:
                State state = getState(id, api);
                result = stateService.invoke(state);
                modelAndView.addObject("jsonStr", formatState(state));
                modelAndView.addObject("method", "POST");
                break;
            case QUERY:
                state = getState(id, api);
                result = stateService.query(state);
                modelAndView.addObject("jsonStr", formatState(state));
                modelAndView.addObject("method", "POST");
                break;
            case INFO:
                result = traceService.queryBlockChainInfo(id, api.getKey());
                modelAndView.addObject("jsonStr", "");
                modelAndView.addObject("method", "GET");
                if (StringUtils.isNotBlank(api.getKey())) {
                    modelAndView.addObject("url", String.format("%s/%s", url, api.getKey()));
                } else {
                    modelAndView.addObject("url", url);
                }
                break;
            case HASH:
                Trace trace = getTrace(id, api);
                result = traceService.queryBlockByHash(trace);
                modelAndView.addObject("jsonStr", formatTrace(trace));
                modelAndView.addObject("method", "POST");
                break;
            case NUMBER:
                trace = getTrace(id, api);
                result = traceService.queryBlockByNumber(trace);
                modelAndView.addObject("jsonStr", formatTrace(trace));
                modelAndView.addObject("method", "POST");
                break;
            case TXID:
                trace = getTrace(id, api);
                result = traceService.queryBlockByTransactionID(trace);
                modelAndView.addObject("jsonStr", formatTrace(trace));
                modelAndView.addObject("method", "POST");
                break;
        }
        modelAndView.addObject("result", result);
        modelAndView.addObject("api", api);
        return modelAndView;
    }

    @PostMapping(value = "instantiate")
    public ModelAndView instantiate(@ModelAttribute Api api, @RequestParam("chaincodeId") int id) {
        Chaincode chaincode = chaincodeService.get(id);
        Channel channel = channelService.get(chaincode.getChannelId());
        Peer peer = peerService.get(channel.getPeerId());
        Org org = orgService.get(peer.getOrgId());
        League league = leagueService.get(org.getLeagueId());
        chaincode.setLeagueName(league.getName());
        chaincode.setOrgName(org.getName());
        chaincode.setPeerName(peer.getName());
        chaincode.setChannelName(channel.getName());

        chaincodeService.instantiate(chaincode, Arrays.asList(api.getExec().split(",")));
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("chaincodeSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("enter"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("chaincode", new Chaincode());
        modelAndView.addObject("channels", getChannelFullList());
        modelAndView.addObject("init", false);
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("chaincodeSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        modelAndView.addObject("init", false);
        Chaincode chaincode = chaincodeService.get(id);
        Peer peer = peerService.get(channelService.get(chaincode.getChannelId()).getPeerId());
        Org org = orgService.get(peer.getOrgId());
        League league = leagueService.get(org.getLeagueId());
        chaincode.setPeerName(peer.getName());
        chaincode.setOrgName(org.getName());
        chaincode.setLeagueName(league.getName());
        List<Channel> channels = channelService.listById(peer.getId());
        for (Channel channel : channels) {
            channel.setPeerName(peer.getName());
            channel.setOrgName(org.getName());
            channel.setLeagueName(league.getName());
        }
        modelAndView.addObject("chaincode", chaincode);
        modelAndView.addObject("channels", channels);
        return modelAndView;
    }

    @GetMapping(value = "instantiate")
    public ModelAndView instantiate(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("chaincodeInstantiate");
        modelAndView.addObject("intentLittle", SpringUtil.get("instantiate"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("chaincodeId", chaincodeId);

        Api apiInstantiate = new Api("实例化智能合约", INSTANTIATE.getIndex());

        modelAndView.addObject("api", apiInstantiate);
        return modelAndView;
    }

    @GetMapping(value = "install")
    public ModelAndView install() {
        ModelAndView modelAndView = new ModelAndView("chaincodeInstall");
        modelAndView.addObject("intentLittle", SpringUtil.get("install"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "install");
        modelAndView.addObject("chaincode", new Chaincode());
        modelAndView.addObject("channels", getChannelFullList());
        modelAndView.addObject("init", false);

        Api apiInstantiate = new Api("实例化智能合约", INSTANTIATE.getIndex());

        modelAndView.addObject("api", apiInstantiate);
        return modelAndView;
    }

    @GetMapping(value = "upgrade")
    public ModelAndView upgrade(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("chaincodeUpgrade");
        modelAndView.addObject("intentLittle", SpringUtil.get("upgrade"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "upgrade");
        modelAndView.addObject("init", true);
        modelAndView.addObject("chaincode", chaincodeService.get(chaincodeId));

        Api apiInstantiate = new Api("升级智能合约", UPGRADE.getIndex());

        modelAndView.addObject("api", apiInstantiate);
        return modelAndView;
    }

    @GetMapping(value = "verify")
    public ModelAndView verify(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("chaincodeVerify");
        modelAndView.addObject("intentLittle", SpringUtil.get("verify"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("chaincodeId", chaincodeId);
        modelAndView.addObject("versions", VerifyUtil.versions());

        List<Api> apis = new ArrayList<>();
        Api apiInvoke = new Api(SpringUtil.get("chaincode_invoke"), INVOKE.getIndex());
        Api apiQuery = new Api(SpringUtil.get("chaincode_query"), Api.Intent.QUERY.getIndex());
        Api api = new Api(SpringUtil.get("chaincode_block_info"), Api.Intent.INFO.getIndex());
        Api apiHash = new Api(SpringUtil.get("chaincode_block_get_by_hash"), Api.Intent.HASH.getIndex());
        Api apiTxid = new Api(SpringUtil.get("chaincode_block_get_by_txid"), Api.Intent.TXID.getIndex());
        Api apiNumber = new Api(SpringUtil.get("chaincode_block_get_by_height"), Api.Intent.NUMBER.getIndex());
        apis.add(apiInvoke);
        apis.add(apiQuery);
        apis.add(api);
        apis.add(apiHash);
        apis.add(apiTxid);
        apis.add(apiNumber);

        Api apiIntent = new Api();

        modelAndView.addObject("apis", apis);
        modelAndView.addObject("apiIntent", apiIntent);
        return modelAndView;
    }

    @GetMapping(value = "delete")
    public ModelAndView delete(@RequestParam("id") int id) {
        chaincodeService.delete(id);
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("chaincodes");
        List<Chaincode> chaincodes = chaincodeService.listAll();
        for (Chaincode chaincode : chaincodes) {
            chaincode.setChannelName(channelService.get(chaincode.getChannelId()).getName());
        }
        modelAndView.addObject("chaincodes", chaincodes);
        return modelAndView;
    }

    private List<Channel> getChannelFullList() {
        List<Channel> channels = channelService.listAll();
        for (Channel channel : channels) {
            Peer peer = peerService.get(channel.getPeerId());
            channel.setPeerName(peer.getName());
            Org org = orgService.get(peer.getOrgId());
            channel.setOrgName(org.getName());
            League league = leagueService.get(org.getLeagueId());
            channel.setLeagueName(league.getName());
        }
        return channels;
    }

    private State getState(int id, Api api) {
        State state = new State();
        state.setId(id);
        state.setKey(api.getKey());
        state.setVersion(api.getVersion());
        state.setStrArray(Arrays.asList(api.getExec().trim().split(",")));
        return state;
    }

    private String formatState(State state) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", state.getId());
        if (StringUtils.isNotBlank(state.getKey())) {
            jsonObject.put("key", state.getKey());
        }
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(state.getStrArray()));
        jsonObject.put("strArray", jsonArray);
        return jsonObject.toJSONString();
    }

    private Trace getTrace(int id, Api api) {
        Trace trace = new Trace();
        trace.setId(id);
        trace.setKey(api.getKey());
        trace.setVersion(api.getVersion());
        trace.setTrace(api.getExec().trim());
        return trace;
    }

    private String formatTrace(Trace trace) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", trace.getId());
        if (StringUtils.isNotBlank(trace.getKey())) {
            jsonObject.put("key", trace.getKey());
        }
        jsonObject.put("trace", trace.getTrace());
        return jsonObject.toJSONString();
    }

    private Chaincode resetChaincode(Chaincode chaincode) {
        Channel channel = channelService.get(chaincode.getChannelId());
        Peer peer = peerService.get(channel.getPeerId());
        Org org = orgService.get(peer.getOrgId());
        League league = leagueService.get(org.getLeagueId());
        chaincode.setLeagueName(league.getName());
        chaincode.setOrgName(org.getName());
        chaincode.setPeerName(peer.getName());
        chaincode.setChannelName(channel.getName());
        return chaincode;
    }

}
