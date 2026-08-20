package com.bmos.mes.service.preparation.produce.controller.vo;

import com.bmos.mes.service.output.weigh.vo.OutputWeighStorageMaterialVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 配液产出结果VO
 */
@Getter
@Setter
@ApiModel("配液产出结果VO")
public class ProduceVO {

    /**
     * 产出产出流程id
     */
    @ApiModelProperty(value = "产出产出流程id", example = "1")
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
     * 产出人id
     */
    @ApiModelProperty(value = "产出人id", example = "1")
    private String producerId;

    /**
     * 产出人名称
     */
    @ApiModelProperty(value = "产出人名称", example = "1")
    private String producerName;

    /**
     * 产出人登录名
     */
    @ApiModelProperty(value = "产出人登录名", example = "1")
    private String producerLoginName;

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
    @ApiModelProperty(value = "配方物料id", example = "1")
    private Long formulaMaterialId;

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
     * 已产出列表
     */
    @ApiModelProperty(value = "已产出列表")
    private List<ProduceRecordVO> produceRecordList;

}
