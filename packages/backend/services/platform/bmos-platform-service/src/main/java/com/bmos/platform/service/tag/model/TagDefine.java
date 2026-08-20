package com.bmos.platform.service.tag.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.common.enums.tag.BarcodeFormat;
import com.bmos.platform.common.enums.tag.PrintCmdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签定义
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 12:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bp_tag_define")
public class TagDefine extends BaseDO {

    /**
     * 标签样式
     */
    private String tagStyle;

    /**
     * 标签宽度(mm)
     */
    private Integer tagWidth;

    /**
     * 标签高度(mm)
     */
    private Integer tagHeight;

    /**
     * 指令类型
     */
    private PrintCmdType cmdType;


    private BarcodeFormat barcodeFormat;

    /**
     * 预览html模板
     */
    private String previewHtml;

    /**
     * 字段最大长度
     */
    private Integer maxFieldSize;
}
