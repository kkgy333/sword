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
@TableName("league")
public class League {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type= IdType.AUTO)
    private int id; // required
    private String name; // required
    private String date; // required
    private String version;
    @TableField(exist=false)
    private int orgCount; // required

}
