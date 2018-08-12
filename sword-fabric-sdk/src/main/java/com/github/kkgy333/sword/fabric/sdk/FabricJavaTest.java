package com.github.kkgy333.sword.fabric.sdk;

import com.google.protobuf.ByteString;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;
import java.util.Set;

import static java.lang.String.format;

/**
 * Author: kkgy333
 * Date: 2018/8/1
 **/

public class FabricJavaTest {


    //设置加密方式
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }


    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException {








        try {

            //创建客户端代理
            HFClient hfclient = HFClient.createNewInstance();
            CryptoSuite cryptosuite = CryptoSuite.Factory.getCryptoSuite();
            hfclient.setCryptoSuite(cryptosuite);


            //设置用户
            //User user = getFabricUser4Local("admin","peerOrg1","Org1MSP");
            User user = getFabricUser4FabricCA("admin","peerOrg1","Org1MSP");
            hfclient.setUserContext(user);

            //创建通道的客户端代理
            Channel channel = hfclient.newChannel("foo");

            //Orderer的TLS设置
//            Properties ordererProperties = new Properties();
//            ordererProperties.setProperty("pemFile", "/Users/sagay/fabric-tools/fabric-scripts/hlfv11/composer/crypto-config/ordererOrganizations/example.com/orderers/orderer.example.com/tls/ca.crt");crt

            //创建orderer服务器客户端代理并加到通道中
            Orderer orderer = hfclient.newOrderer("orderer", "grpc://192.168.109.128:7050");
            channel.addOrderer(orderer);


            //Peer的TLS设置
            Properties peerProperties = new Properties();
            peerProperties.setProperty("pemFile" ,  "/Volumes/Work/MY_Project/区块链/sdk/fabric-sdk-java/src/test/fixture/sdkintegration/e2e-2Orgs/v1.2/crypto-config/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/server.crt");
            peerProperties.setProperty("negotiationType" ,  "TLS");
            peerProperties.setProperty("grpc.NettyChannelBuilderOption.maxInboundMessageSize" ,  "9000000");
            peerProperties.setProperty("sslProvider" ,  "openSSL");
            //peerProperties.setProperty("clientCertFile" ,  "/Volumes/Work/MY_Project/区块链/sdk/fabric-sdk-java/src/test/fixture/sdkintegration/e2e-2Orgs/v1.2/crypto-config/peerOrganizations/org1.example.com/users/User1@org1.example.com/tls/client.crt");
            peerProperties.setProperty("hostnameOverride" ,  "peer0.org1.example.com");
            //peerProperties.setProperty("clientKeyFile" ,  "/Volumes/Work/MY_Project/区块链/sdk/fabric-sdk-java/src/test/fixture/sdkintegration/e2e-2Orgs/v1.2/crypto-config/peerOrganizations/org1.example.com/users/User1@org1.example.com/tls/client.key");


            //创建Peer服务器节点的客户端代理并加入到通道中
            Peer peer = hfclient.newPeer("peer0.org1.example.com", "grpc://192.168.109.128:7051");
            channel.addPeer(peer);

            //初始化通道
            channel.initialize();


            //获取IP地址为：192.168.23.212 的Peer服务器的roberttestchannel12的相关信息，包括前面，区块链高度等信息
            BlockchainInfo blockchianinfo = channel.queryBlockchainInfo(peer);
            System.out.println(" 区块高度 :  "+blockchianinfo.getHeight() + " ");


            //根据交易编号获取区块的详细信息。
            /*BlockInfo blockinfo = channel.queryBlockByTransactionID(peer,"");
            String blockhashstr = Hex.encodeHexString(blockinfo.getPreviousHash());
			System.out.println(blockhashstr);*/


            BlockInfo blockinfo= channel.queryBlockByNumber(0);
            ByteString blockhash = blockinfo.getBlock().getHeader().getPreviousHash();
            String blockhashstr = Hex.encodeHexString(blockinfo.getPreviousHash());
            System.out.println(" 最后区块hash :  "+blockhashstr);

            System.out.println(" 交易数量 :  "+blockinfo.getTransactionCount());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CryptoException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ProposalException e) {
            e.printStackTrace();
        } catch (TransactionException e) {
            e.printStackTrace();
        } /*catch (EnrollmentException e) {
            e.printStackTrace();
        }catch (org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException e) {
            e.printStackTrace();
        }*/


    }


    /**
     * 通过Fabric CA获取Fabric 账号
     *
     * @param username
     * @param org
     * @param orgId
     * @return
     */
    private static User getFabricUser4FabricCA(String username, String org, String orgId)  {

        try {
            FabricUsersImpl user = new FabricUsersImpl(username, org);
            user.setMspId(orgId);


            CryptoSuite cryptosuite = CryptoSuite.Factory.getCryptoSuite();
            HFCAClient caclient = HFCAClient.createNewInstance("http://192.168.109.128:7054", null);

            caclient.setCryptoSuite(cryptosuite);
            Enrollment enrollment = caclient.enroll(username, "adminpw");

            user.setEnrollment(enrollment);


            return user;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据cryptogen模块生成的账号创建Fabirc账号
     *
     * @param username
     * @param org
     * @param orgId
     * @return
     */
    private static User getFabricUser4Local(String username, String org, String orgId) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {


        FabricUsersImpl user = new FabricUsersImpl(username, org);



        user.setMspId(orgId);

        String certificate = new String(IOUtils.toByteArray(new FileInputStream(new File("/Users/sagay/fabric-tools/fabric-scripts/hlfv11/composer/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem"))), "UTF-8");
        //File privatekeyfile = findFileSk("/project/opt_fabric/fabricconfig/crypto-config/peerOrganizations/org1.robertfabrictest.com/users/Admin@org1.robertfabrictest.com/msp/keystore");
        File privatekeyfile = new File("/Users/sagay/fabric-tools/fabric-scripts/hlfv11/composer/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/114aab0e76bf0c78308f89efc4b8c9423e31568da0c340ca187a9b17aa9a4457_sk");

        PrivateKey privateKey = getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream(privatekeyfile)));
        EnrolleMentImpl enrollement = new EnrolleMentImpl(privateKey, certificate);


        user.setEnrollment(enrollement);



        return user;

    }


    /**
     * 配置文件中获取私钥
     *
     * @param data
     * @return
     * @throws IOException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PrivateKey getPrivateKeyFromBytes(byte[] data) throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {

        final Reader pemReader = new StringReader(new String(data));

        final PrivateKeyInfo pemPair;

        PEMParser pemParser = new PEMParser(pemReader);
        pemPair = (PrivateKeyInfo) pemParser.readObject();


        PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);

        return privateKey;


    }


    /**
     * 获取私钥文件
     *
     * @param directorys
     * @return
     */
    private static File findFileSk(String directorys) {

        File directory = new File(directorys);

        File[] matches = directory.listFiles((dir, name) -> name.endsWith("_sk"));

        if (null == matches) {
            throw new RuntimeException(format("Matches returned null does %s directory exist?", directory.getAbsoluteFile().getName()));
        }

        if (matches.length != 1) {
            throw new RuntimeException(format("Expected in %s only 1 sk file but found %d", directory.getAbsoluteFile().getName(), matches.length));
        }

        return matches[0];

    }


    /**
     * enrollm 实现类
     */
    static final class EnrolleMentImpl implements Enrollment, Serializable {


        private static final long serialVersionUID = -2784835212445309006L;
        private final PrivateKey privateKey;
        private final String certificate;


        public EnrolleMentImpl(PrivateKey privateKey, String certificate) {


            this.certificate = certificate;

            this.privateKey = privateKey;
        }

        @Override
        public PrivateKey getKey() {
            return privateKey;
        }

        @Override
        public String getCert() {
            return certificate;
        }
    }


    /**
     * Fabric user 实现类
     */
    static final class FabricUsersImpl implements User, Serializable {


        private String name;
        private Set<String> roles;
        private String account;
        private String affiliation;
        private String organization;
        private String enrollmentSecret;
        Enrollment enrollment = null; //need access in test env.

        private String keyValStoreName;

        FabricUsersImpl(String name, String org) {

            this.name = name;
            this.organization = org;


        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Set<String> getRoles() {
            return this.roles;
        }

        public void setRoles(Set<String> roles) {

            this.roles = roles;
        }

        @Override
        public String getAccount() {
            return this.account;
        }

        /**
         * Set the account.
         *
         * @param account The account.
         */
        public void setAccount(String account) {

            this.account = account;
        }

        @Override
        public String getAffiliation() {
            return this.affiliation;
        }

        /**
         * Set the affiliation.
         *
         * @param affiliation the affiliation.
         */
        public void setAffiliation(String affiliation) {
            this.affiliation = affiliation;

        }

        @Override
        public Enrollment getEnrollment() {
            return this.enrollment;
        }


        /**
         * Determine if this name has been enrolled.
         *
         * @return {@code true} if enrolled; otherwise {@code false}.
         */
        public boolean isEnrolled() {
            return this.enrollment != null;
        }


        public String getEnrollmentSecret() {
            return enrollmentSecret;
        }

        public void setEnrollmentSecret(String enrollmentSecret) {
            this.enrollmentSecret = enrollmentSecret;
        }

        public void setEnrollment(Enrollment enrollment) {

            this.enrollment = enrollment;

        }


        @Override
        public String getMspId() {
            return mspId;
        }

        String mspId;

        public void setMspId(String mspID) {
            this.mspId = mspID;

        }
    }

}