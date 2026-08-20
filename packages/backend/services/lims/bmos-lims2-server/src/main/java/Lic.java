import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.bmos.web.interceptor.Activate;

import java.util.Base64;

/**
 * @className: Lic
 * @author: yigaohui
 * @date: 2025/6/5 10:08
 * @Version: 1.0
 * @description:
 */

public class Lic {
    public static void main(String[] args) {
        RSA rsa = new RSA(null, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcw1cp/Rblb9P33zh82Euz+KgvayLHUPyCFNFw5CHazP21ZhxGLtfpDEAi5fDaQ6RcLqg5VX/IuC2FEo533twy4RvfUnJ4zGs1kVw/dBuhvTuAwUk0lnW2xjwTOaEswSboZuTasf30A7gOlDYf5OOUBgJ3gTIHGpuWWYf22bj7VQIDAQAB");
        Activate active = new Activate();
        active.setDate("2025-07-05 00:00:00");
        active.setMac("6C-92-BF-3B-FE-E3");
        active.setApplicationName("bmos-wms-service");
        System.out.println(Base64.getEncoder().encodeToString(rsa
                .encrypt(StrUtil.bytes(JSON.toJSONString(active), CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey)));
    }
}
