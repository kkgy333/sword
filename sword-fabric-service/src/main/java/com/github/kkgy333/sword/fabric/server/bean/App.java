package com.github.kkgy333.sword.fabric.server.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Setter
@Getter
public class App {

    private int id;
    private String name;
    private String key;
    private int chaincodeId;
    private String createDate;
    private String modifyDate;
    private String privateKey;
    private String publicKey;
    private boolean active;

}
