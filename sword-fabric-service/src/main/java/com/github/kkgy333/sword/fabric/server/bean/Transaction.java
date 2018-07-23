package com.github.kkgy333.sword.fabric.server.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Setter
@Getter
public class Transaction {

    /** 序号，无实际意义 */
    private int index;
    /** 块高度 */
    private int num;
    private int txCount;
    private String channelName;
    private String createMSPID;
    private String date;

}
