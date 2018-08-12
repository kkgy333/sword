package com.github.kkgy333.sword.fabric.sdk.Manager;


import com.github.kkgy333.sword.fabric.sdk.entity.SampleOrg;
import org.hyperledger.fabric.sdk.helper.Utils;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Author: kkgy333
 * Date: 2018/8/7
 **/
public class OrgManager {

    private Map<String, SampleOrg> orgMap;
    private String orgName;

    public OrgManager() {
        orgMap = new LinkedHashMap<>();
    }

    /**
     * 初始化组织名称，该对象的必须首次调用方法
     *
     * @param orgName 组织Hash
     * @param openTLS     设置是否开启TLS
     *
     * @return self
     */
    public OrgManager init(String orgName,String name, String mspid,  boolean openTLS) {
        this.orgName = orgName;
        if (orgMap.get(orgName) != null) {
            throw new RuntimeException(String.format("OrgManager had the same id of %s", orgName));
        } else {
            orgMap.put(orgName, new SampleOrg(name,mspid));
        }
        //orgMap.get(orgName).openTLS(openTLS);
        return this;
    }

    public SampleOrg get(String orgName){

        if (orgMap.get(orgName) != null) {
            return orgMap.get(orgName);
        }
        return null;
    }


    public void initCAClient() throws MalformedURLException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException {
        for (SampleOrg sampleOrg : orgMap.values()) {
            String caName = sampleOrg.getCAName(); //Try one of each name and no name.
            if (caName != null && !caName.isEmpty()) {

                sampleOrg.setCAClient(HFCAClient.createNewInstance(caName, sampleOrg.getCALocation(), sampleOrg.getCAProperties()));
            } else {
                sampleOrg.setCAClient(HFCAClient.createNewInstance(sampleOrg.getCALocation(), sampleOrg.getCAProperties()));
            }
        }


    }



//    /**
//     * 设置默认用户（一个特殊用户，即可对Channel及ChainCode进行操作的用户，一般为Admin，在有特殊操作需求的情况下，channelArtifactsPath不为null；
//     * 也可以是一个已经在服务器生成好用户相关证书文件的用户，在没有特殊操作需求的情况下，一般channelArtifactsPath设置为null即可）
//     *
//     * @param username         用户名
//     * @param cryptoConfigPath 用户/节点组织/排序服务证书文件路径
//     *
//     * @return self
//     */
//    public  OrgManager setUser(@Nonnull String username, @Nonnull String cryptoConfigPath) {
//        orgMap.get(orgName).setUsername(username);
//        orgMap.get(orgName).setCryptoConfigPath(cryptoConfigPath);
//        return this;
//    }
//
//    public  OrgManager setOrderers(String ordererDomainName) {
//        orgMap.get(orgName).setOrdererDomainName(ordererDomainName);
//        return this;
//    }

//    public OrgManager addOrderer(String name, String location) {
//        orgMap.get(chainCodeId).addOrderer(name, location);
//        return this;
//    }

//    public OrgManager setPeers(String orgName, String orgMSPID, String orgDomainName) {
//        orgMap.get(orgName).setOrgName(orgName);
//        orgMap.get(orgName).setOrgMSPID(orgMSPID);
//        orgMap.get(orgName).setOrgDomainName(orgDomainName);
//        return this;
//    }

//    public OrgManager addPeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, boolean isEventListener) {
//        orgMap.get(chainCodeId).addPeer(peerName, peerEventHubName, peerLocation, peerEventHubLocation, isEventListener);
//        return this;
//    }

//    /**
//     * 设置智能合约
//     *
//     * @param chaincodeName    智能合约名称
//     * @param chaincodePath    智能合约路径
//     * @param chaincodeSource  智能合约安装路径所在路径
//     * @param chaincodePolicy  智能合约背书文件路径
//     * @param chaincodeVersion 智能合约版本
//     * @param proposalWaitTime 单个提案请求的超时时间以毫秒为单位
//     */
//    public com.github.kkgy333.sword.fabric.sdk.OrgManager setChainCode(String chaincodeName, String chaincodePath, String chaincodeSource, String chaincodePolicy, String chaincodeVersion, int proposalWaitTime) {
//        IntermediateChaincodeID chaincode = new IntermediateChaincodeID();
//        chaincode.setChaincodeName(chaincodeName);
//        chaincode.setChaincodeSource(chaincodeSource);
//        chaincode.setChaincodePath(chaincodePath);
//        chaincode.setChaincodePolicy(chaincodePolicy);
//        chaincode.setChaincodeVersion(chaincodeVersion);
//        chaincode.setProposalWaitTime(proposalWaitTime);
//        orgMap.get(chainCodeId).setChainCode(chaincode);
//        return this;
//    }

//    /**
//     * 设置频道
//     *
//     * @param channelName 频道名称
//     *
//     * @return Fabric
//     */
//    public OrgManager setChannel(String channelName) {
//        IntermediateChannel channel = new IntermediateChannel();
//        channel.setChannelName(channelName);
//        orgMap.get(chainCodeId).setChannel(channel);
//        return this;
//    }

//    /**
//     * 设置监听事件
//     *
//     * @param blockListener BlockListener
//     */
//    public OrgManager setBlockListener(BlockListener blockListener) {
//        orgMap.get(chainCodeId).setBlockListener(blockListener);
//        return this;
//    }

//    public void add() {
//        if (orgMap.get(chainCodeId).getPeers().size() == 0) {
//            throw new RuntimeException("peers is null or peers size is 0");
//        }
//        if (orgMap.get(chainCodeId).getOrderers().size() == 0) {
//            throw new RuntimeException("orderers is null or orderers size is 0");
//        }
//        if (orgMap.get(chainCodeId).getChainCode() == null) {
//            throw new RuntimeException("chaincode must be instantiated");
//        }
//
//        // 根据TLS开启状态循环确认Peer节点各服务的请求grpc协议
//        for (int i = 0; i < orgMap.get(chainCodeId).getPeers().size(); i++) {
//            orgMap.get(chainCodeId).getPeers().get(i).setPeerLocation(grpcTLSify(orgMap.get(chainCodeId).openTLS(), orgMap.get(chainCodeId).getPeers().get(i).getPeerLocation()));
//            orgMap.get(chainCodeId).getPeers().get(i).setPeerEventHubLocation(grpcTLSify(orgMap.get(chainCodeId).openTLS(), orgMap.get(chainCodeId).getPeers().get(i).getPeerEventHubLocation()));
//        }
//        // 根据TLS开启状态循环确认Orderer节点各服务的请求grpc协议
//        for (int i = 0; i < orgMap.get(chainCodeId).getOrderers().size(); i++) {
//            orgMap.get(chainCodeId).getOrderers().get(i).setOrdererLocation(grpcTLSify(orgMap.get(chainCodeId).openTLS(), orgMap.get(chainCodeId).getOrderers().get(i).getOrdererLocation()));
//        }
//    }

//    public FabricManager use(int chainCodeId) throws Exception {
//        FabricOrg org = orgMap.get(chainCodeId);
//        // java.io.tmpdir : C:\Users\aberic\AppData\Local\Temp\
//        File storeFile = new File(String.format("%s/HFCStore%s.properties", System.getProperty("java.io.tmpdir"), chainCodeId));
//        FabricStore fabricStore = new FabricStore(storeFile);
//        org.init(fabricStore);
//        org.setClient(HFClient.createNewInstance());
//        org.getChannel().init(org);
//        return new FabricManager(org);
//    }

    public String grpcTLSify(boolean openTLS, String location) {
        location = location.trim();
        Exception e = Utils.checkGrpcUrl(location);
        if (e != null) {
            throw new RuntimeException(String.format("Bad TEST parameters for grpc url %s", location), e);
        }
        return openTLS ? location.replaceFirst("^grpc://", "grpcs://") : location;

    }


    public String httpTLSify(boolean openCATLS, String location) {
        location = location.trim();
        return openCATLS ? location.replaceFirst("^http://", "https://") : location;
    }

}
