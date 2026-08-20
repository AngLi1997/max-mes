package com.bmos.mes.service.dataset.dto;

import com.bmos.common.base.enums.CommonEnum;
import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据集编辑dto
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 15:02
 */
@Data
@ApiModel("数据集编辑dto")
public class DatasetEditDTO {

    @ApiModelProperty(value = "数据集id", example = "1")
    @NotNull
    private Long id;

    @ApiModelEnumProperty(value = "数据集类型", enumClass = DatasetType.class)
    @NotNull
    private String datasetType;

    @ApiModelProperty(value = "数据点列表(类型为批记录数据)")
    private List<DatasetPointEditDTO> datasetPointList = new ArrayList<>();

    @ApiModelProperty(value = "删除的数据点id列表(类型为批记录数据)")
    private List<Long> removeDatasetPointIds = new ArrayList<>();

    @ApiModelProperty(value = "动态数据填报列表(类型为动态填报数据)")
    private List<DatasetDynamicReportDataEditDTO> datasetDynamicReportDataList = new ArrayList<>();

    @ApiModelProperty(value = "批签发引用列表(类型为批签发引用)")
    private List<DatasetLotReleaseLinkEditDTO> datasetLotReleaseLinkList = new ArrayList<>();


    public List<? extends DatasetPointEditBaseDTO> getList(){
        DatasetType dt = CommonEnum.getEnumByValue(DatasetType.class, datasetType);
        if (dt == null){
            return new ArrayList<>();
        }
        if (dt == DatasetType.POINT){
            return datasetPointList;
        } else if (dt == DatasetType.DYNAMIC_REPORT){
            return datasetDynamicReportDataList;
        } else if (dt == DatasetType.LOT_RELEASE_LINK){
            return datasetLotReleaseLinkList;
        }  else {
            return new ArrayList<>();
        }
    }
}
