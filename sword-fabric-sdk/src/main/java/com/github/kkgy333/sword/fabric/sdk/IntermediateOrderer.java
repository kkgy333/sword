package com.github.kkgy333.sword.fabric.sdk;

/**
 * 描述：中继Orderer排序服务对象
 *
 * @author : Aberic 【2018/5/4 15:30】
 */
class IntermediateOrderer {

    /** orderer 排序服务器的域名 */
    private String ordererName;
    /** orderer 排序服务器的访问地址 */
    private String ordererLocation;
    /** tls请求证书 */
    private String serverCrtPath;

    IntermediateOrderer(String ordererName, String ordererLocation, String serverCrtPath) {
        super();
        this.ordererName = ordererName;
        this.ordererLocation = ordererLocation;
        this.serverCrtPath = serverCrtPath;
    }

    String getOrdererName() {
        return ordererName;
    }

    void setOrdererLocation(String ordererLocation) {
        this.ordererLocation = ordererLocation;
    }

    String getOrdererLocation() {
        return ordererLocation;
    }

    String getServerCrtPath() {
        return serverCrtPath;
    }
}
