package com.github.kkgy333.sword.fabric.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Setter
@Getter
@ToString
public class Chaincode {

    private int id; // required
    private String name; // required
    private String source; // optional
    private String path; // optional
    private String policy; // optional
    private String version; // required
    private int proposalWaitTime = 90000; // required
    private int channelId; // required
    private String date; // optional
    private String channelName; // optional
    private String peerName; // optional
    private String orgName; // optional
    private String leagueName; // optional
    private boolean open;
}
