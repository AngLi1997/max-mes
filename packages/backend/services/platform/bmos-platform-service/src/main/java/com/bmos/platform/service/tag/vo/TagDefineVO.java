package com.bmos.platform.service.tag.vo;

import com.bmos.platform.common.enums.tag.PrintCmdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 标签定义vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 14:38
 */
@Data
@ApiModel("标签定义vo")
public class TagDefineVO {

    /**
     * 标签定义id
     */
    @ApiModelProperty(value = "标签定义id", example = "1")
    private Long id;

    /**
     * 标签场景id
     */
    @ApiModelProperty(value = "标签场景id", example = "1")
    private Long tagSceneId;

    /**
     * 标签样式
     */
    @ApiModelProperty(value = "标签样式", example = "默认样式")
    private String tagStyle;

    /**
     * 标签宽度(mm)
     */
    @ApiModelProperty(value = "标签宽度(mm)", example = "400")
    private Integer tagWidth;

    /**
     * 标签高度(mm)
     */
    @ApiModelProperty(value = "标签高度(mm)", example = "300")
    private Integer tagHeight;

    /**
     * 标签样式zpl指令模板
     */
    @ApiModelProperty(value = "指令模板", example = "^XA^FO20,20^A0N,25,25^FDHello, World!^FS^XZ")
    private String cmd;

    /**
     * 指令类型
     */
    @ApiModelProperty(value = "指令类型", example = "zpl")
    private PrintCmdType cmdType;

    /**
     * 预览html模板
     */
    @ApiModelProperty(value = "预览html模板", example = "<div>Hello, World!</div>")
    private String previewHtml;

    /**
     * 数据源服务名称
     */
    @ApiModelProperty(value = "数据源服务名称", example = "bmos-platform-service")
    private String dataSourceServiceName;

    /**
     * 数据源接口地址
     */
    @ApiModelProperty(value = "数据源接口地址", example = "/test.txt")
    private String dataSourceInterface;

    /**
     * 二维码字段（标签显示的二维码信息， 来源与数据源接口）
     */
    @ApiModelProperty(value = "二维码字段", example = "qrCode")
    private String qrCodeField;

    /**
     * 获取标签样式全名 (样式(宽*高))
     *
     * @return 标签样式全名
     */
    @ApiModelProperty(value = "标签样式全名", example = "默认样式(400*300)")
    private String getFullName() {
        return tagStyle + "(" + tagWidth + "*" + tagHeight + ")";
    }
}
