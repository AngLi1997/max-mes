package com.bmos.mes.service.dataset.vo;

import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 数据集详情vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:13
 */
@Data
@ApiModel("数据集详情vo")
public class DatasetVO {

    @ApiModelProperty(value = "数据集id", example = "1")
    private Long id;

    @ApiModelProperty(value = "数据集名称", example = "数据集名称")
    private String name;

    @ApiModelProperty(value = "数据集分类id", example = "1")
    private Long datasetCategoryId;

    @ApiModelEnumProperty(enumClass = DatasetType.class, value = "数据集类型")
    private DatasetType type;

    @ApiModelProperty(value = "数据集key(流水号)", example = "1")
    private String datasetKey;

    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "产品名称", example = "产品名称")
    private String productName;

    @ApiModelProperty(value = "工艺id", example = "1")
    private Long processId;

    @ApiModelProperty(value = "工艺名称", example = "工艺名称")
    private String processName;

    @ApiModelProperty(value = "数据点列表(类型为批记录数据)")
    private List<DatasetPointVO> datasetPointList;

    @ApiModelProperty(value = "动态数据填报列表(类型为动态填报数据)")
    private List<DatasetDynamicReportDataVO> datasetDynamicReportDataList;

    @ApiModelProperty(value = "批签发引用列表(类型为批签发引用)")
    private List<DatasetLotReleaseLinkVO> datasetLotReleaseLinkList;
}
