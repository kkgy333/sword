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
public class Orderer {

    private int id; // required
    private String name; // required
    private String location; // required
    private int orgId; // required
    private String orgName; // required
    private String date; // required

}
