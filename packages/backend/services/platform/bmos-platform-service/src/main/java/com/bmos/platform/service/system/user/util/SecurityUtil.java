package com.bmos.platform.service.system.user.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtil {

    /**
     * do not modify
     */
    public final static String KEY = "2jn8jMeEzOi34B/nTgrgSUwUA077GwVFt5s61KZpLmg=";

    private final static String DATA = "ceHVR8ubac8wyGH67497Zw==";


    public static String generateAesKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256, new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] byteKey = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(byteKey);
    }

    /**
     * 对数据进行aes加密
     * @param aesKey aes的key
     * @param data 要加密的数据
     * @return
     */
    public static String aesEncrypt(String aesKey, String data) throws Exception {
        Key key = new SecretKeySpec(Base64.getDecoder().decode(aesKey),"AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 对数据进行aes解密
     * @param aesKey aes的key
     * @param data 要解密的数据
     * @return
     */
    public static String aesDecrypt(String aesKey, String data) throws Exception {
        Key key = new SecretKeySpec(Base64.getDecoder().decode(aesKey),"AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(result);
    }
}
