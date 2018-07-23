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
public class Peer {

    private int id; // required
    private String name; // required
    private String eventHubName; // required
    private String location; // required
    private String eventHubLocation; // required
    private boolean eventListener; // required
    private int orgId; // required
    private String orgName; // required
    private String leagueName; // optional
    private String date; // required
    private int channelCount; // required
}
