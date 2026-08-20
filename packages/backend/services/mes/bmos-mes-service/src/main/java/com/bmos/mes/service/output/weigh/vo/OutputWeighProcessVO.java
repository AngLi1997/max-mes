package com.bmos.mes.service.output.weigh.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/28 11:20
 */
@Data
@ApiModel("产出称量信息")
public class OutputWeighProcessVO {

    /**
     * 产出称量流程id
     */
    @ApiModelProperty(value = "产出称量流程id", example = "1")
    private Long id;

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long productPlanId;

    /**
     * 工序步骤模型id
     */
    @ApiModelProperty(value = "工序步骤模型id", example = "1")
    private Long procedureStepModelId;

    /**
     * 拷贝版本
     */
    @ApiModelProperty(value = "拷贝版本", example = "1")
    private Long copyVersion;

    /**
     * 组件id
     */
    @ApiModelProperty(value = "组件id", example = "1")
    private Long componentId;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1")
    private String weigherId;

    /**
     * 称量人名称
     */
    @ApiModelProperty(value = "称量人名称", example = "1")
    private String weigherName;

    /**
     * 称量人登录名
     */
    @ApiModelProperty(value = "称量人登录名", example = "1")
    private String weigherLoginName;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1")
    private String reCheckerId;

    /**
     * 复核人名称
     */
    @ApiModelProperty(value = "复核人名称", example = "1")
    private String reCheckerName;

    /**
     * 复核人登录名
     */
    @ApiModelProperty(value = "复核人登录名", example = "1")
    private String reCheckerLoginName;

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1")
    private Long materialId;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "矿泉水")
    private String materialName;

    /**
     * 物料合并编码
     */
    @ApiModelProperty(value = "物料合并编码", example = "0001-0001")
    private String materialMergeCode;

    /**
     * 物料基础单位id
     */
    @ApiModelProperty(value = "物料基础单位id", example = "1")
    private Long basicUnitId;

    /**
     * 物料基础单位名称
     */
    @ApiModelProperty(value = "物料基础单位名称", example = "1")
    private String basicUnit;

    /**
     * 配方单位id
     */
    @ApiModelProperty(value = "配方单位id", example = "1")
    private Long unitId;

    /**
     * 配方单位
     */
    @ApiModelProperty(value = "配方单位", example = "ml")
    private String unit;

    /**
     * 物料规格
     */
    @ApiModelProperty(value = "物料规格", example = "500ml")
    private String materialSpecification;

    /**
     * 物料批次id
     */
    @ApiModelProperty(value = "物料批次id", example = "1")
    private Long storageMaterialBatchId;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "B1")
    private String storageMaterialBatchNo;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", example = "2024-05-11")
    private LocalDate expiredDate;

    /**
     * 关联物料id
     */
    @ApiModelProperty(value = "关联物料id", example = "1")
    private Long relevanceMaterialId;

    /**
     * 关联物料批次id
     */
    @ApiModelProperty(value = "关联物料批次id", example = "1")
    private Long relevanceStorageMaterialBatchId;

    /**
     * 关联物料批号
     */
    @ApiModelProperty(value = "关联物料批号", example = "B1")
    private String relevanceStorageMaterialBatchNo;

    /**
     * 配方物料精度
     */
    @ApiModelProperty(value = "配方物料精度", example = "0.0001")
    private BigDecimal scale;

    /**
     * 已称量列表
     */
    @ApiModelProperty(value = "已称量列表")
    private List<OutputWeighStorageMaterialVO> weightRecordList = new ArrayList<>();

    /**
     * 工位id
     */
    @ApiModelProperty(value = "工位id", example = "1")
    private List<Long> stationIds = new ArrayList<>();
}
