package com.github.kkgy333.sword.fabric.server.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("org")
public class Org {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type= IdType.AUTO)
    private int id; // required
    private String name; // required
    private boolean tls; // required
    private String username; // required
    private String cryptoConfigDir; // required
    private String mspId; // required
    private String domainName; // required
    private String ordererDomainName; // required
    private int leagueId; // required
    @TableField(exist=false)
    private String leagueName; // required
    private String date; // required
    @TableField(exist=false)
    private int peerCount; // required
    @TableField(exist=false)
    private int ordererCount; // required
}
