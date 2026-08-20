package com.bmos.mes.common.utils;

import cn.hutool.core.codec.Base64Decoder;
import com.bmos.mes.common.enums.signature.SignatureConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * base64工具类
 */
public class Base64Util {

    /**
     * base64转换为文件流转
     * @param base64： 文件base64
     * @param suffix: 文件后缀
     * @return
     */
    public static File convertFile(String base64, String suffix) throws IOException {
        byte[] decode = Base64Decoder.decode(base64);
        File file = File.createTempFile(SignatureConstant.TEMPORARY_FOLDER, suffix);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decode);
        }
        return file;
    }

}
