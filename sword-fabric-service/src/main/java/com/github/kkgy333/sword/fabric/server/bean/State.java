package com.github.kkgy333.sword.fabric.server.bean;

import com.github.kkgy333.sword.fabric.server.base.BaseChain;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Setter
@Getter
public class State extends BaseChain {
    private List<String> strArray; // required
}
