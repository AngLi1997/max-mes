package com.bmos.mes.service.plan.info.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mes.common.enums.plan.PlanArchiveStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;

/**
* 生产计划关联关系表
*/
@Getter
@Setter
@ApiModel("ProductPlanRelationListVO:生产计划关联工艺列表VO")
public class ProductPlanRelationListVO {
    @Tolerate
    public ProductPlanRelationListVO() {}

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("工序id")
    private Long processId;

    @ApiModelProperty("工序名称")
    private String processName;

    @ApiModelProperty("生产工艺版本")
    private String processVersion;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("生产批次开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("归档状态")
    private PlanArchiveStatusEnum archiveStatus;

    @ApiModelProperty("归档文件Url")
    private String archiveFileUrl;

    @ApiModelProperty("是否被其他批次关联")
    private Boolean related;
}
