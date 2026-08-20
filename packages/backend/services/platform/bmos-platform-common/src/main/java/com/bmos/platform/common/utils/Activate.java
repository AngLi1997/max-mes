package com.bmos.platform.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Activate {
    /**
     * 年月日时分秒 | ALL 代码永久激活
     */
    private String date;

    /**
     * mac码
     */
    private String mac;

    /**
     * 所属应用名称
     */
    private String applicationName;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public static String getMACAddress(InetAddress ia) throws SocketException {
        //获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        // 下面代码是把mac地址拼装成String
        if (mac == null) {
            return null;
        }
        return IntStream.range(0, mac.length)
                .mapToObj(i -> convertMacAddress(mac[i]))
                .collect(Collectors.joining("-"));
    }

    public static Set<String> getAllMACAddress() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        Set<String> set = new HashSet<>();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            byte[] hardwareAddress = networkInterface.getHardwareAddress();
            if (hardwareAddress != null) {
                set.add(
                        IntStream.range(0, hardwareAddress.length)
                                .mapToObj(i -> convertMacAddress(hardwareAddress[i]))
                                .collect(Collectors.joining("-"))
                );
            }
        }
        return set;
    }

    public static String convertMacAddress(byte macValue) {
        String resultStr = Integer.toHexString(macValue & 0xFF).toUpperCase();
        if (resultStr.length() == 1) return "0" + resultStr;
        return resultStr;
    }
}
