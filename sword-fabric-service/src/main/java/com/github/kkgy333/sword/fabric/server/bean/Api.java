package com.github.kkgy333.sword.fabric.server.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Setter
@Getter
public class Api {

    /** API 意图 */
    public enum Intent {
        INVOKE(1, "state/invoke"),
        QUERY(2, "state/query"),
        INFO(3, "trace/info/1"),
        HASH(4, "trace/hash"),
        NUMBER(5, "trace/number"),
        TXID(6, "trace/txid"),
        INSTANTIATE(7, "chaincode/instantiate"),
        UPGRADE(8, "chaincode/upgrade");

        private int index;
        private String apiUrl;

        Intent(int index, String apiUrl) {
            this.index = index;
            this.apiUrl = apiUrl;
        }

        public static Intent get(int index) {
            for (Intent i : Intent.values()) {
                if (i.getIndex() == index) {
                    return i;
                }
            }
            return null;
        }

        public String getApiUrl() {
            return apiUrl;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    /** 接口名称 */
    private String name = "";
    /** 接口意图 */
    private int index = 0;
    /** 请求app key */
    private String key = "";
    /** 请求Fabric版本号 */
    private String version = "";
    /** 接口执行参数 */
    private String exec = "";

    public Api() {
    }

    public Api(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
