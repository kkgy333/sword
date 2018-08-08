package com.github.kkgy333.sword.fabric.sdk.utils;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Author: kkgy333
 * Date: 2018/8/7
 **/
public class PropertiesHelper {

    public static boolean isRunningAgainstFabric10() {

        return "IntegrationSuiteV1.java".equals(System.getProperty("org.hyperledger.fabric.sdktest.ITSuite"));

    }

    public static Properties getPeerProperties(String name,String baseChannelPath) {

        return getEndPointProperties("peer", name,baseChannelPath);

    }

    public static Properties getOrdererProperties(String name,String baseChannelPath) {

        return getEndPointProperties("orderer", name,baseChannelPath);

    }

    public static Properties getEndPointProperties(final String type, final String name,String baseChannelPath) {
        Properties ret = new Properties();

        final String domainName = getDomainName(name);

        File cert = Paths.get(baseChannelPath, "crypto-config/ordererOrganizations".replace("orderer", type), domainName, type + "s",
                name, "tls/server.crt").toFile();
        if (!cert.exists()) {
            throw new RuntimeException(String.format("Missing cert file for: %s. Could not find at location: %s", name,
                    cert.getAbsolutePath()));
        }

        if (!isRunningAgainstFabric10()) {
            File clientCert;
            File clientKey;
            if ("orderer".equals(type)) {
                clientCert = Paths.get(baseChannelPath, "crypto-config/ordererOrganizations/example.com/users/Admin@example.com/tls/client.crt").toFile();

                clientKey = Paths.get(baseChannelPath, "crypto-config/ordererOrganizations/example.com/users/Admin@example.com/tls/client.key").toFile();
            } else {
                clientCert = Paths.get(baseChannelPath, "crypto-config/peerOrganizations/", domainName, "users/User1@" + domainName, "tls/client.crt").toFile();
                clientKey = Paths.get(baseChannelPath, "crypto-config/peerOrganizations/", domainName, "users/User1@" + domainName, "tls/client.key").toFile();
            }

            if (!clientCert.exists()) {
                throw new RuntimeException(String.format("Missing  client cert file for: %s. Could not find at location: %s", name,
                        clientCert.getAbsolutePath()));
            }

            if (!clientKey.exists()) {
                throw new RuntimeException(String.format("Missing  client key file for: %s. Could not find at location: %s", name,
                        clientKey.getAbsolutePath()));
            }
            ret.setProperty("clientCertFile", clientCert.getAbsolutePath());
            ret.setProperty("clientKeyFile", clientKey.getAbsolutePath());
        }

        ret.setProperty("pemFile", cert.getAbsolutePath());

        ret.setProperty("hostnameOverride", name);
        ret.setProperty("sslProvider", "openSSL");
        ret.setProperty("negotiationType", "TLS");

        return ret;
    }


    private static String getDomainName(final String name) {
        int dot = name.indexOf(".");
        if (-1 == dot) {
            return null;
        } else {
            return name.substring(dot + 1);
        }

    }
}
