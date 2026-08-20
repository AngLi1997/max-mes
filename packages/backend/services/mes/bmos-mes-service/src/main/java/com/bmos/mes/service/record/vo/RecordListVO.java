package com.bmos.mes.service.record.vo;

import com.bmos.common.util.enums.EnumUtils;
import com.bmos.mes.service.record.enums.RecordStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("记录管理页vo")
public class RecordListVO {

    @ApiModelProperty(value = "记录id")
    private Long recordId;

    @ApiModelProperty(value = "记录名称")
    private String name;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态")
    private RecordStateEnum state;

    @ApiModelProperty(value = "状态名称")
    private String stateName;

    @ApiModelProperty(value = "流程实例id")
    private String instanceId;

    /*public String getStateName() {
        return EnumUtils.getValueByName(RecordStateEnum.values(),state);
    }*/

    @ApiModelProperty(value = "版本id")
    private Long versionId;
}
