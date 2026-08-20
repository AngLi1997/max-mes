package com.bmos.mes.service.execute.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName("bm_execute_record_copy")
public class ExecuteRecordCopy extends BaseDO {

    private String batchNo;

    private Long productPlanId;

    private Long processId;

    private String processVersion;

    private Long procedureStepId;

    private Long recordItemId;

    private Long recordVersionId;

    private Long version;

    @TableField("is_discard")
    private Boolean discard;

    @TableField("is_reuse")
    private Boolean reuse;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;


}
