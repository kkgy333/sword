package com.github.kkgy333.sword.fabric.server.model;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 作者：Aberic on 2018/6/27 21:12
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
@ToString
@TableName("user")
public class User {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type= IdType.AUTO)
    private int id; // required
    private String username; // required
    private String password; // required

}