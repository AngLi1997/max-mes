package com.bmos.platform.service.system.user.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

public class DecryptPasswrdUtils {

    public static String decryptPasswrd(String password) {
        byte[] byteKey = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), password.getBytes()).getEncoded();
        SymmetricCrypto aes = SecureUtil.aes(byteKey);

        //解密
        return aes.decryptStr(password);
    }

}
