package com.bmos.mes.service.weigh.centre2.requirement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 称量需求查询参数DTO
 *
 * @author liang
 * @version 1.0.0
 * @date 2025/5/20 10:05
 */
@Data
@ApiModel("称量需求查询参数")
public class RequirementQueryDTO {

    @ApiModelProperty(value = "物料名称", example = "乳糖")
    private String materialName;

    @ApiModelProperty(value = "物料编码", example = "37A")
    private String materialMergeCode;

    @ApiModelProperty(value = "物料批次", example = "11111111")
    private String storageMaterialBatchNo;

    @ApiModelProperty(value = "称量中心", example = "C3-3车间称量中心")
    private String weighCentreName;

    @ApiModelProperty(value = "产品信息", example = "爱哥列特片")
    private String productName;

    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "生产批号", example = "CPX0012309")
    private String batchNo;

    @ApiModelProperty(value = "计划生产日期开始", example = "2025-05-15")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate planDateStart;

    @ApiModelProperty(value = "计划生产日期结束", example = "2025-05-20")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate planDateEnd;

    @ApiModelProperty(value = "物料ID")
    private Long materialId;

    @ApiModelProperty(value = "物料批次ID")
    private Long storageMaterialBatchId;

    @ApiModelProperty(value = "单位ID")
    private Long unitId;

    @ApiModelProperty(value = "称量中心ID")
    private Long weighCentreId;

    @ApiModelProperty(value = "工单ID")
    private Long ticketId;

    @ApiModelProperty(value = "页面上标记了删除的数据，查询时候会附带", example = "[1, 2, 3]")
    private List<Long> deleteRequirementIds;

    @ApiModelProperty(value = "页面上标记了新增的数据，查询时候会过滤掉", example = "[4, 5, 6]")
    private List<Long> addRequirementIds;
} 