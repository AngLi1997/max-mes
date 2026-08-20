package com.bmos.platform.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签定义vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 14:38
 */
@Data
@ApiModel("标签定义详情vo")
public class TagSceneDetailVO {

    /**
     * 标签场景id
     */
    @ApiModelProperty(value = "标签场景id", example = "1")
    private Long id;

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
     * 字段定义
     */
    @ApiModelProperty(value = "可选字段")
    private List<TagSceneFieldVO> availableFields = new ArrayList<>();
}
