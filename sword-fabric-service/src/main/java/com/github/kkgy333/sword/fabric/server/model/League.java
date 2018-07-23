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
public class League {

    private int id; // required
    private String name; // required
    private String date; // required
    private String version;
    private int orgCount; // required

}
