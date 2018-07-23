package com.github.kkgy333.sword.fabric.sdk;

import io.netty.util.internal.StringUtil;
import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.io.*;
import java.util.Set;

/**
 * 中继联盟用户对象
 *
 * @author 杨毅 【2017年9月7日 - 16:36】
 */
class IntermediateUser implements User, Serializable {

    private static final long serialVersionUID = 5695080465408336815L;

    /** 名称 */
    private String name;
    /** 规则 */
    private Set<String> roles;
    /** 账户 */
    private String account;
    /** 从属联盟 */
    private String affiliation;
    /** 组织 */
    private String organization;
    /** 注册操作的密密钥 */
    private String enrollmentSecret;
    /** 会员id */
    private String mspId;
    /** 注册登记操作 */
    private Enrollment enrollment = null;

    /** 存储配置对象 */
    private transient FabricStore fabricStore;
    private String keyForFabricStoreName;

    /**
     * Fabric网络用户对象
     *
     * @param name    用户名称（User1）
     * @param orgName 组织名称（Org1）
     * @param store   联盟存储配置对象
     */
    IntermediateUser(String name, String orgName, FabricStore store) {
        this.name = name;
        this.organization = orgName;
        this.fabricStore = store;
        this.keyForFabricStoreName = getKeyForFabricStoreName(this.name, orgName);

        String memberStr = fabricStore.getValue(keyForFabricStoreName);
        if (null != memberStr) {
            saveState();
        } else {
            restoreState();
        }
    }

    /**
     * 设置账户信息并将用户状态更新至存储配置对象
     *
     * @param account 账户
     */
    void setAccount(String account) {
        this.account = account;
        saveState();
    }

    @Override
    public String getAccount() {
        return this.account;
    }

    /**
     * 设置从属联盟信息并将用户状态更新至存储配置对象
     *
     * @param affiliation 从属联盟
     */
    void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
        saveState();
    }

    @Override
    public String getAffiliation() {
        return this.affiliation;
    }

    String getEnrollmentSecret() {
        return enrollmentSecret;
    }

    void setEnrollmentSecret(String enrollmentSecret) {
        this.enrollmentSecret = enrollmentSecret;
    }

    /**
     * 设置注册登记操作信息并将用户状态更新至存储配置对象
     *
     * @param enrollment 注册登记操作
     */
    void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
        saveState();
    }

    /**
     * 获取用户注册登记操作后信息
     *
     * @return 用户注册登记操作后信息
     */
    @Override
    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    /**
     * 设置会员id信息并将用户状态更新至存储配置对象
     *
     * @param mspID 会员id
     */
    void setMspId(String mspID) {
        this.mspId = mspID;
        saveState();
    }

    /**
     * 获取会员id
     *
     * @return 会员id
     */
    @Override
    public String getMspId() {
        return this.mspId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 设置角色规则信息并将用户状态更新至存储配置对象
     *
     * @param roles 规则
     */
    void setRoles(Set<String> roles) {
        this.roles = roles;
        saveState();
    }

    /**
     * 获取角色规则信息
     *
     * @return 角色规则信息
     */
    @Override
    public Set<String> getRoles() {
        return this.roles;
    }

    /**
     * 确定这个名称是否已注册
     *
     * @return 与否
     */
    boolean isRegistered() {
        return !StringUtil.isNullOrEmpty(enrollmentSecret);
    }

    /**
     * 确定这个名字是否已经注册
     *
     * @return 与否
     */
    boolean isEnrolled() {
        return this.enrollment != null;
    }

    /**
     * 将用户状态保存至存储配置对象
     */
    void saveState() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            fabricStore.setValue(keyForFabricStoreName, Hex.toHexString(bos.toByteArray()));
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从键值存储中恢复该用户的状态(如果找到的话)。如果找不到，什么也不要做
     */
    private void restoreState() {
        String memberStr = fabricStore.getValue(keyForFabricStoreName);
        if (null != memberStr) {
            // 用户在键值存储中被找到，因此恢复状态
            byte[] serialized = Hex.decode(memberStr);
            ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                IntermediateUser state = (IntermediateUser) ois.readObject();
                if (state != null) {
                    this.name = state.name;
                    this.roles = state.roles;
                    this.account = state.account;
                    this.affiliation = state.affiliation;
                    this.organization = state.organization;
                    this.enrollmentSecret = state.enrollmentSecret;
                    this.enrollment = state.enrollment;
                    this.mspId = state.mspId;
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format("Could not restore state of member %s", this.name), e);
            }
        }
    }

    /**
     * 得到联盟存储配置对象key
     *
     * @param name 用户名称（User1）
     * @param org  组织名称（Org1）
     * @return 类似user.Org1User1.Org1
     */
    static String getKeyForFabricStoreName(String name, String org) {
        System.out.println("toKeyValStoreName = " + "user." + name + org);
        return "user." + name + org;
    }

}
