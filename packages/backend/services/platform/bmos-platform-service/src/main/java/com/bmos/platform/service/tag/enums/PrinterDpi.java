package com.bmos.platform.service.tag.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/12 16:36
 */
@Getter
@AllArgsConstructor
public enum PrinterDpi {

    DPI_203(203, 8),

    DPI_300(300, 12);

    /**
     * 打印机dpi
     */
    private final int dpi;

    /**
     * dpi对应的像素点
     */
    private final int dpiPoint;

    /**
     * 根据dpi获取像素点
     * @param dpi
     * @return
     */
    public static Integer getDpiPoint(Integer dpi) {
        if (dpi == null){
            return null;
        }
        for (PrinterDpi printerDpi : PrinterDpi.values()){
            if (printerDpi.getDpi() == dpi){
                return printerDpi.getDpiPoint();
            }
        }
        return null;
    }
}
