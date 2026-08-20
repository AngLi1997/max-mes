package com.bmos.platform.service.tag.util;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.zebra.sdk.graphics.ZebraImageFactory;
import com.zebra.sdk.graphics.ZebraImageI;
import com.zebra.sdk.printer.PrinterUtil;
import com.zebra.sdk.util.internal.ZPLUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * zpl协议打印机工具
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/23 14:53
 */
public class ZplPrinterUtil {
    private static Logger log = LoggerFactory.getLogger(ZplPrinterUtil.class);

    /**
     * 最大超时时间 3s
     */
    private static final int CONNECTION_TIMEOUT = 3000;

    private static final String ZPL_CMD_TEMPLATE =
            "^XA" +
            "^PW%s^FS" +
            "^LL%s^FS" +
            "^LH0,0^FS" +
            "%s" +
            "^FO0,0^XGR:LOGO.GRF,1,1^FS" +
            "^IDR:LOGO.GRF" +
            "^XZ";

    /**
     * @param ip
     * @param port
     * @param zplCmd zpl指令
     * @throws Exception
     */
    public static void sendZpl(String ip, Integer port, String zplCmd) {
        if (StrUtil.isBlank(zplCmd)){
            return;
        }
        System.out.println("正在向打印机发送指令");
        if (StrUtil.isBlank(ip) || port == null){
            throw new BmosException(PlatformResponseCode.PRINTER_IP_ILLEGAL);
        }
        boolean ping = NetUtil.ping(ip, CONNECTION_TIMEOUT);
        if (!ping) {
            throw new BmosException(PlatformResponseCode.PRINTER_IP_ILLEGAL);
        }
        try (Socket socket = new Socket(ip, port);
             OutputStream outputStream = socket.getOutputStream()) {
            outputStream.write(zplCmd.getBytes());
            outputStream.flush();
            System.out.println("指令发送成功, 打印完成");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("发送zpl指令失败:{}", e.getMessage());
            throw new BmosException(PlatformResponseCode.PRINTER_SEND_ZPL_ERROR);
        }
    }

    public static String getZplCmdFromImage(File file) {
        if (file == null) {
            return null;
        }
        try {
            System.out.println("正在转换ZPL打印机指令...");
            ByteArrayOutputStream cmd = new ByteArrayOutputStream();
            ZebraImageI image = ZebraImageFactory.getImage(file.getPath());
            PrinterUtil.convertGraphic("R:LOGO.GRF", image, cmd);
            byte[] byteArray = cmd.toByteArray();
            ZPLUtilities.replaceInternalCharactersWithReadableCharacters(byteArray);
            String line = new String(byteArray);
            return String.format(ZPL_CMD_TEMPLATE, image.getWidth(), image.getHeight(), line);
        } catch (Exception e) {
            log.error("图片文件转zpl指令失败:{}", e.getMessage());
            return null;
        }
    }
}
