package com.bmos.file.docx.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author ren jin guang
 * @version 1.0.0
 * @date 2024/2/18 11:55
 */
public interface ComponentConstant {

    /**
     * 需显示图片的组件类型
     * 拍照上传组件
     * 手写签名组件
     * 手写签名复合组件
     * 设备数采绘图
     */
    List<String> PIC_COMPONENT = Arrays.asList("PHOTO", "HANDLE_SUBMIT_SIGN", "HANDLE_REVIEW_SIGN");

    /**
     * 单选组件
     */
    String RADIO = "RADIO";

    /**
     * 多选组件
     */
    String CHECKBOX = "CHECKBOX";

    String CONCLUSION = "CONCLUSION";

    List<String> AUTO_ADAPT_SIZE_PIC_COMPONENT = Arrays.asList("EQUIPMENT_DATA_DRAW");

    /**
     * 拓展表格组件
     */
    List<String> EXTEND_TABLE = Arrays.asList("EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE");
}
