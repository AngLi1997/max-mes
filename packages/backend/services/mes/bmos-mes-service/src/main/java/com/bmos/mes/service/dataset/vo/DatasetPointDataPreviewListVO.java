package com.bmos.mes.service.dataset.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 数据点预览vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 13:46
 */
@Data
@ApiModel("数据点预览 list vo")
public class DatasetPointDataPreviewListVO {

    @ApiModelProperty("动态分页标题")
    private List<DatasetPointDataPreviewTitleVO> titles;

    @ApiModelProperty("动态列表数据")
    private List<DatasetPointDataPreviewPageVO> list;
}
