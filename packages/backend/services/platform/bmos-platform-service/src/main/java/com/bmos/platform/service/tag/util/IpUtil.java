package com.bmos.platform.service.tag.util;

/**
 * ip地址工具类
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/23 14:53
 */
public class IpUtil {

    /**
     * 判断ip是否是正确的ipv4地址
     *
     * @param ip ip地址
     * @return true 正确的ipv4地址
     */
    public static boolean isCorrectIpv4Ip(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        String[] ipArray = ip.split("\\.");
        if (ipArray.length != 4) {
            return false;
        }
        for (String s : ipArray) {
            int ipInt = Integer.parseInt(s);
            if (ipInt < 0 || ipInt > 255) {
                return false;
            }
        }
        return true;
    }
}
