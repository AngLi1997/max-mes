package com.bmos.platform.service.system.user.service;

/**
 * 手写签名
 */
public interface UserSignService {

    /**
     * 签名保存接口，若之前有签名，则直接进行覆盖
     *
     * @param imageBase64
     * @param suffix
     * @param userId
     * @return
     */
    String saveUserSign(String imageBase64, String suffix, String userId);

    /**
     * 获取当前登录人的签名地址
     * @return
     */
    String getUserSign(String userId);
}
