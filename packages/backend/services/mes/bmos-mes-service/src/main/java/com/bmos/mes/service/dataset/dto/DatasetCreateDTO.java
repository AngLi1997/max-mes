package com.bmos.mes.service.dataset.dto;

import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 15:02
 */
@Data
@ApiModel("数据集创建dto")
public class DatasetCreateDTO {

    @ApiModelProperty(value = "数据集分类id", example = "1")
    @NotNull
    private Long datasetCategoryId;

    @ApiModelProperty(value = "数据集名称", example = "数据集")
    @NotBlank
    @Length(max = 100)
    private String name;

    @ApiModelEnumProperty(value = "数据集类型", enumClass = DatasetType.class)
    @NotNull
    private String datasetType;

    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "工艺id", example = "1")
    private Long processId;

    @Valid
    @ApiModelProperty(value = "数据点列表(类型为批记录数据)")
    private List<DatasetPointCreateDTO> datasetPointList = new ArrayList<>();

    @Valid
    @ApiModelProperty(value = "动态数据填报列表(类型为动态填报数据)")
    private List<DatasetDynamicReportDataCreateDTO> datasetDynamicReportDataList = new ArrayList<>();

    @Valid
    @ApiModelProperty(value = "批签发引用列表(类型为批签发引用)")
    private List<DatasetLotReleaseLinkCreateDTO> datasetLotReleaseLinkList = new ArrayList<>();
}
