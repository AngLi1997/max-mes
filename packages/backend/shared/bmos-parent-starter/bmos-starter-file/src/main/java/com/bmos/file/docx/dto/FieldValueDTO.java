package com.bmos.file.docx.dto;

import lombok.Data;

import java.util.List;

/**
 * @author yigaohui
 * @date 2024/6/7
 **/
@Data
public class FieldValueDTO {
    private Long fieldId;

    private String value;

    private String componentType;

    private String evidenceName;

    private String evidenceTime;

    /**
     * 拍照组件、手写签名的图片base64集合
     */
    private List<ImageVO> imgs;

    /**
     * 是否是空值
     * 录入空值、修订空值时此处为true
     */
    private Boolean emptyValue;

    /**
     * 扩展字段 前端使用
     */
    private String valueExtension;
}
