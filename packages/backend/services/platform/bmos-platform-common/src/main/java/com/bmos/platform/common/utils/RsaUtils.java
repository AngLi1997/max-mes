package com.bmos.platform.common.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.bmos.common.exception.ActiveException;
import com.bmos.common.util.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.time.LocalDateTime;

public class RsaUtils {

    private static Logger log = LoggerFactory.getLogger(RsaUtils.class);

    private static final RSA rsa = new RSA("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJzDVyn9FuVv0/ffOHzYS7P4qC9rIsdQ/IIU0XDkIdrM/bVmHEYu1+kMQCLl8NpDpFwuqDlVf8i4LYUSjnfe3DLhG99ScnjMazWRXD90G6G9O4DBSTSWdbbGPBM5oSzBJuhm5Nqx/fQDuA6UNh/k45QGAneBMgcam5ZZh/bZuPtVAgMBAAECgYAbqrQjAnp8gtiPujM/gXMW789LEyb/s3DlSmROfszkz2IFlmBuGI1doZ5eKmPwOZjz9f4VpFvZonvSLRy81jXZdBinInTbsQj2wNlTeYTVOdBqSOAW+4FkW0UjFksARNMu9ky5Makeh2f7vGksrqnzj5flSRWho0ouOjh2+A6QwQJBANX8Fiv/9LjwF+jkgvSjL8+Nbv54+SbLNq/ZnK9AoIyFfsyJ8UxIln9iLxL6ceMYHg07jDCD9eQ0IPaBa22eHWECQQC7iwOzR25VOSR0/vYJKdWtl1CI0/nMjKg+dNNP45hZbXAqbM5oqAEAPCxzvr+jsaB0cgrSXpJyuzhV39dQwk51AkEA0uOvwktulzlghxF2kr2laAs+waLeAkKysdpo1jt+Px9t2Q119zVuxToxpKWYyjRBoc73GfVjyDUr62WfR5hV4QJBAJOEb26veyvx/KlCe4kNrRQUd7aI9m5dHWGzRxwJ7CY7nQTh/SH5NIBY1KZeniNGbu3pXnXHCe7RMSjrH1RY4pkCQH6se0kdq0ruGENacCHYzU98NTIW/o//49fkw1/0x2e6vjb8XGnW4fg0OKAndgP/wLXWPVXWZe+8aVRwjjhoEzY=", null);

    /**
     * 密钥校验加解密
     */
    private static final RSA decryptPwdRsa = new RSA("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC/YIwQXYekVX5ql1jxXNemZjaVYoShuqSGuukDCT6rPfgL8ByEWv3FPXgH4l0dPDCsqVGw0N2+xALZxGzRxCIY573OWvvxFh9Ye5uqWeR5I2x8qciTLpkyGKZAt3mu4AdDiLDeLlNP+yWTYoNr6Klo+Y6YypMqyLbRw3LgAF7CpAy3c6w1mjovPG69RdbLeJtqWIU8S/Qo9BHHtwWxT8qvQc9sCjkRFDCkAp4cJLDpSI7Azsh9hv1myU8NwQf8s8Ki1syhw6CkEjLcMuLcqkkG3eIUQ5vPPisBsUaAp7fBoJ76KZed1f/lB4XIGLhEJ/ouEeotTJ7oLbz23ay3PlQjAgMBAAECggEAUuWbRit9ae2uiLVuTX6N2wJDeonmMbgLNGEaNbp3bu202Fb1w082xY1OxPuEoflEjEX7oL+Yjd2R7oprKKHwsyEO0qgOoeuXQFiqoSu3D9IMWCOjQj3ZFh4fRcEz8FASVWqMUnF2nRqIuELf8oohd9tBrWVyQgIXTt1+kREX+u8cLNDpJDpXXO3lI3oR65fmLweBy1gISxPU3aWp6GgtkD+qUFrte3tvlC5+i3952aVEKAkIl/z5yxNNe91eecnEysuAWzo5iNQtrvozHj3UicDUjnsVG+fglFsfepXMlQ025WZBBehCvURQHl6QvnmDtpDv5VKIMAYmlnq4LQlHgQKBgQDxKP5pAk9UtyaEQAU9MH7KB8z/pXyoLFm3xnagxOUq0s0QgLUWtPYH9zEemrC7KYePl7w8blygfmGt0kKHrbFAP76Bq+61I8taoiJFso7GQbqrktR7Gm9eqhDIZnwS8r9OOmPnnTh8iD/o5dj6iXPViE12u/JdrBLdLDGqlICQUwKBgQDLJ1HSkxp0t0WMQgeqnbypGkdEWJHexR+sMtz8JyiGB7HdLuIoU3PNIPx4hqEJis8MYxCX9CI7En2OaRlV3CfbMLNDk7rVU1ooBlbcym/krA4yy+ObSWC0EGNjicOxaE/7xudtvMoFBqY7721OuklZ/yWbB2ysfRW6fO0Ffhjy8QKBgFJErs152UBIHp6dDGx8X7RiBt/rP2RLhOu5i3x6zsbWFNidX6l4YDoFbL9I/CNNHo6wr6lZ6MVjd8UJZmLMsRv+4ulvksY+iL/JcbGfIS+m+pxPQachNH7xuznyJD9+ih6QssYvjzaEcHDuACAvxdFBZ1Oq+BpWejTzP5o2gMk7AoGAcsfQbw02EWPLI6fa9QcSJOj4AyGXqSnu7zf9q2nkd3W5hKKMnQQplWhhvSczrVSEDxeZtID3JjEYR3ISInS1AB7P6gp5sTXa5mpOgYjPF4AutXyFKKOYw6POw9p+eqnZJwQGlfXonlZXbd2lAKo372U9Ng5H1F0H/cm/kS7hocECgYEAucv0Ood+soamVzysor6MuCxn2ePOuTkUpg1lfkTx5R7R/r9r+y3SxwtLK8QQP/rpbo5miOksR8sMnVFYNkooI61n4pAQcTZ5Sj3jDmhQtgW4pfszpGHQUcOCW3tkENK/vX7n/wVAhYAqu8J01p4npTUFf09gqFwXP11/XteHvEA=", null);


    public static Boolean validate(String activeCode, String applicationName) {
        if (!validateNoDate(activeCode, applicationName)) {
            return Boolean.FALSE;
        }
        Activate activate = getParseData(activeCode);
        if (StrUtil.isEmpty(activate.getDate()) || StrUtil.isEmpty(activate.getMac())) {
            return Boolean.FALSE;
        }
        if ("ALL".equals(activate.getDate())) {
            return Boolean.TRUE;
        }
        if (!LocalDateTime.now().isBefore(LocalDateTimeUtil.parse(activate.getDate(), DatePattern.NORM_DATETIME_FORMATTER))) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static Boolean validateNoDate(String activeCode, String applicationName) {
        Activate activate = getParseData(activeCode);
        try {
            if (!Activate.getAllMACAddress().contains(activate.getMac())) {
                return Boolean.FALSE;
            }
            if (!activate.getApplicationName().equals(applicationName)) {
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static Activate getParseData(String activeCode) {
        if (StrUtil.isEmpty(activeCode)) {
            throw new ActiveException();
        }
        try {
            return JsonUtils.parseObject(
                    StrUtil.str(rsa.decrypt(activeCode, KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8),
                    Activate.class
            );
        } catch (Exception exception) {
            throw new ActiveException();
        }
    }

    /**
     * 解密前端传的加密密码
     * @param encryptPwd
     * @return
     */
    public static String decryptPwd(String encryptPwd){
        if (StrUtil.isEmpty(encryptPwd)){
            return null;
        }
        try {
            return StrUtil.str(decryptPwdRsa.decrypt(encryptPwd, KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            log.error("密码解密失败", e);
            return null;
        }
    }
}
