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
public class Org {

    private int id; // required
    private String name; // required
    private boolean tls; // required
    private String username; // required
    private String cryptoConfigDir; // required
    private String mspId; // required
    private String domainName; // required
    private String ordererDomainName; // required
    private int leagueId; // required
    private String leagueName; // required
    private String date; // required
    private int peerCount; // required
    private int ordererCount; // required
}
