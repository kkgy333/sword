package com.github.kkgy333.sword.fabric.server.base;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Setter
@Getter
public class BaseChain {
    private String key;
    private String flag;
    private int channelId;
    private String version;
}
