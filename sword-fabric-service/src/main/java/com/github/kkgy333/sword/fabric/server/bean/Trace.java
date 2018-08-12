package com.github.kkgy333.sword.fabric.server.bean;

import com.github.kkgy333.sword.fabric.server.base.BaseChain;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Setter
@Getter
public class Trace extends BaseChain {
    private String trace; // required
}
