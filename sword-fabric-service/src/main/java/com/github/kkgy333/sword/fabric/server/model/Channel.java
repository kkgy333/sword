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
public class Channel {

    private int id; // required
    private String name; // required
    private int peerId; // required
    private String date; // optional
    private String peerName; // optional
    private String orgName; // optional
    private String leagueName; // optional
    private int chaincodeCount; // optional
}
