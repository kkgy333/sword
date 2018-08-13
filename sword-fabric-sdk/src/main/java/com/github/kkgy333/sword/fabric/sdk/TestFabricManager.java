package com.github.kkgy333.sword.fabric.sdk;


import com.github.kkgy333.sword.fabric.sdk.Manager.OrgManager;
import com.github.kkgy333.sword.fabric.sdk.entity.SampleOrg;
import com.github.kkgy333.sword.fabric.sdk.entity.SampleStore;
import com.github.kkgy333.sword.fabric.sdk.entity.SampleUser;
import com.github.kkgy333.sword.fabric.sdk.utils.PropertiesHelper;
import com.github.kkgy333.sword.fabric.sdk.utils.TestConfig;
import com.github.kkgy333.sword.fabric.sdk.utils.Util;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.HFCAInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hyperledger.fabric.sdk.Channel.PeerOptions.createPeerOptions;

/**
 * Author: kkgy333
 * Date: 2018/8/6
 **/
public class TestFabricManager {

    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

        try {
            OrgManager orgManager = new OrgManager();
            orgManager.init("peerOrg1","peerOrg1","Org1MSP",false);
            orgManager.init("peerOrg2","peerOrg2","Org2MSP",false);

            SampleOrg sampleOrg1 = orgManager.get("peerOrg1");
            sampleOrg1.addPeerLocation("peer0.org1.example.com",orgManager.grpcTLSify(false, "grpc://192.168.109.128:7051"));
            sampleOrg1.addPeerLocation("peer1.org1.example.com",orgManager.grpcTLSify(false, "grpc://192.168.109.128:7056"));

            sampleOrg1.addOrdererLocation("orderer.example.com",orgManager.grpcTLSify(false, "grpc://192.168.109.128:7050"));
            sampleOrg1.setDomainName("org1.example.com");
            sampleOrg1.setCALocation(orgManager.httpTLSify(false,"http://192.168.109.128:7054"));
            sampleOrg1.setCAName("ca0");


            SampleOrg sampleOrg2 = orgManager.get("peerOrg2");
            sampleOrg2.addPeerLocation("peer0.org2.example.com",orgManager.grpcTLSify(false, "grpc://192.168.109.128:8051"));
            sampleOrg2.addPeerLocation("peer1.org2.example.com",orgManager.grpcTLSify(false, "grpc://192.168.109.128:8056"));

            sampleOrg2.addOrdererLocation("orderer.example.com",orgManager.grpcTLSify(false, "grpc://192.168.109.128:7050"));
            sampleOrg2.setDomainName("org2.example.com");
            sampleOrg2.setCALocation(orgManager.httpTLSify(false,"http://192.168.109.128:8054"));

            orgManager.initCAClient();




            HFCAClient ca = sampleOrg1.getCAClient();

            final String orgName = sampleOrg1.getName();
            final String mspid = sampleOrg1.getMSPID();
            ca.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());



            HFCAInfo info = ca.info(); //看看我们是否连上了。
            System.out.println(info);
            String infoName = info.getCAName();
            if (infoName != null && !infoName.isEmpty()) {
                System.out.println(ca.getCAName()+" "+ infoName);
            }
            File sampleStoreFile = new File(System.getProperty("java.io.tmpdir") + "/HFCSampletest.properties");
            if (sampleStoreFile.exists()) { //如果存在则清空
                sampleStoreFile.delete();
            }
            SampleStore sampleStore = new SampleStore(sampleStoreFile);

            SampleUser admin = sampleStore.getMember("admin", orgName);
            if (!admin.isEnrolled()) {  //Preregistered admin only needs to be enrolled with Fabric caClient.
                admin.setEnrollment(ca.enroll(admin.getName(), "adminpw"));
                admin.setMspId(mspid);
            }
            sampleOrg1.setAdmin(admin); // The admin of this org --

            final String sampleOrgName = sampleOrg1.getName();
            final String sampleOrgDomainName = sampleOrg1.getDomainName();

            // src/test/fixture/sdkintegration/e2e-2Orgs/channel/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/

            SampleUser peerOrgAdmin = sampleStore.getMember(sampleOrgName + "Admin", sampleOrgName, sampleOrg1.getMSPID(),
                    Util.findFileSk(Paths.get(getTestChannelPath(), "crypto-config/peerOrganizations/",
                            sampleOrgDomainName, format("/users/Admin@%s/msp/keystore", sampleOrgDomainName)).toFile()),
                    Paths.get(getTestChannelPath(), "crypto-config/peerOrganizations/", sampleOrgDomainName,
                            format("/users/Admin@%s/msp/signcerts/Admin@%s-cert.pem", sampleOrgDomainName, sampleOrgDomainName)).toFile());

            sampleOrg1.setPeerAdmin(peerOrgAdmin); //A special user that can create channels, join peers and install chaincode


            HFClient client = HFClient.createNewInstance();

            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

            client.setUserContext(sampleOrg1.getPeerAdmin());



            Collection<Orderer> orderers = new LinkedList<>();

            for (String orderName : sampleOrg1.getOrdererNames()) {

                Properties ordererProperties = PropertiesHelper.getOrdererProperties(orderName,getTestChannelPath());

                //example of setting keepAlive to avoid timeouts on inactive http2 connections.
                // Under 5 minutes would require changes to server side to accept faster ping rates.
                ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[] {5L, TimeUnit.MINUTES});
                ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[] {8L, TimeUnit.SECONDS});
                ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveWithoutCalls", new Object[] {true});

                orderers.add(client.newOrderer(orderName, sampleOrg1.getOrdererLocation(orderName),
                        ordererProperties));
            }

            //Just pick the first orderer in the list to create the channel.

            Orderer anOrderer = orderers.iterator().next();
            orderers.remove(anOrderer);

            String name ="foo";

            ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(getTestChannelPath() + "/" + name + ".tx"));

            Channel newChannel  =null;
            try {
                //Create channel that has only one signer that is this orgs peer admin. If channel creation policy needed more signature they would need to be added too.
                 newChannel = client.newChannel(name, anOrderer, channelConfiguration, client.getChannelConfigurationSignature(channelConfiguration, sampleOrg1.getPeerAdmin()));
            }catch (Exception ex){
                newChannel = client.newChannel(name);
            }
            System.out.println("Created channel "+ name);

            for (Orderer orderer : orderers) { //add remaining orderers if any.
                newChannel.addOrderer(orderer);
            }

            for (String peerName : sampleOrg1.getPeerNames()) {
                String peerLocation = sampleOrg1.getPeerLocation(peerName);

                Properties peerProperties = PropertiesHelper.getPeerProperties(peerName,getTestChannelPath()); //test properties for peer.. if any.
                if (peerProperties == null) {
                    peerProperties = new Properties();
                }

                //Example of setting specific options on grpc's NettyChannelBuilder
                peerProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 9000000);

                Peer peer = client.newPeer(peerName, peerLocation, peerProperties);
                if (isFabricVersionAtOrAfter("1.3")) {
                    newChannel.joinPeer(peer, createPeerOptions().setPeerRoles(EnumSet.of(Peer.PeerRole.ENDORSING_PEER, Peer.PeerRole.LEDGER_QUERY, Peer.PeerRole.CHAINCODE_QUERY, Peer.PeerRole.EVENT_SOURCE))); //Default is all roles.

                } else {

                    try {
//                    if (doPeerEventing && everyother) {
                        newChannel.joinPeer(peer, createPeerOptions().setPeerRoles(EnumSet.of(Peer.PeerRole.ENDORSING_PEER, Peer.PeerRole.LEDGER_QUERY, Peer.PeerRole.CHAINCODE_QUERY, Peer.PeerRole.EVENT_SOURCE))); //Default is all roles.
//                    } else {
//                        // Set peer to not be all roles but eventing.
//                        newChannel.joinPeer(peer, createPeerOptions().setPeerRoles(EnumSet.of(Peer.PeerRole.ENDORSING_PEER, Peer.PeerRole.LEDGER_QUERY, Peer.PeerRole.CHAINCODE_QUERY)));
//                    }
                    }catch (Exception e){
                        newChannel.addPeer(peer, createPeerOptions().setPeerRoles(EnumSet.of(Peer.PeerRole.ENDORSING_PEER, Peer.PeerRole.LEDGER_QUERY, Peer.PeerRole.CHAINCODE_QUERY, Peer.PeerRole.EVENT_SOURCE)));
                    }
                }
                //peers.add(peer);
                System.out.println(String.format("Peer %s joined channel %s", peerName, name));
                //everyother = !everyother;
            }





            newChannel.initialize();

            for(Peer p: newChannel.getPeers()) {

                BlockchainInfo blockchianinfo = newChannel.queryBlockchainInfo(p);
                System.out.println(p.getName()+" 区块高度 :  " + blockchianinfo.getHeight() + " ");
            }



        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String FAB_CONFIG_GEN_VERS =
            Objects.equals(System.getenv("ORG_HYPERLEDGER_FABRIC_SDKTEST_VERSION"), "1.0.0") ? "v1.0" : "v1.2";
    public static String getTestChannelPath() {
        return "C:/Users/guyang/IdeaProjects/fabric-sdk-java/src/test/fixture/sdkintegration/e2e-2Orgs/" + FAB_CONFIG_GEN_VERS;
    }

    static int[]  fabricVersion = new int[3];

    public static boolean isFabricVersionAtOrAfter(String version) {

        final int[] vers = parseVersion(version);
        for (int i = 0; i < 3; ++i) {
            if (vers[i] > fabricVersion[i]) {
                return false;
            }
        }
        return true;
    }
    private static int[] parseVersion(String version) {
        if (null == version || version.isEmpty()) {
            throw new AssertionError("Version is bad :" + version);
        }
        String[] split = version.split("[ \\t]*\\.[ \\t]*");
        if (split.length < 1 || split.length > 3) {
            throw new AssertionError("Version is bad :" + version);
        }
        int[] ret = new int[3];
        int i = 0;
        for (; i < split.length; ++i) {
            ret[i] = Integer.parseInt(split[i]);
        }
        for (; i < 3; ++i) {
            ret[i] = 0;
        }
        return ret;

    }
}





