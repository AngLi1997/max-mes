package com.bmos.mes.service.trace.material.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.service.trace.material.entity.PercentYieldRange;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.UnitCalcDTO;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 物料追溯树节点
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/11/22 14:19
 */
@Data
@ApiModel("物料追溯树节点")
public class MaterialTraceMaterialView {

    /**
     * id
     */
    @ApiModelProperty("虚拟id")
    private Long id = CustomIdGenerator.nextId();

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1")
    private Long materialId;

    /**
     * 物料类型
     */
    @ApiModelEnumProperty(value = "物料类型", enumClass = CategoryInfoTypeEnum.class)
    private CategoryInfoTypeEnum materialCategoryType;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "物料名称")
    private String materialName;

    /**
     * 合并编码
     */
    @ApiModelProperty(value = "合并编码", example = "合并编码")
    private String mergeCode;

    /**
     * 物料批次id
     */
    @ApiModelProperty(value = "物料批次id", example = "1")
    private Long storageMaterialBatchId;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "123456789")
    private String storageMaterialBatchNo;

    /**
     * 生产批号
     */
    @ApiModelProperty(value = "生产批号", example = "123456789")
    private String batchNo;

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long productPlanId;

    /**
     * 来源批号
     */
    @ApiModelProperty(value = "来源批号")
    private String sourceBatchNo;

    /**
     * 来源批次id
     */
    @ApiModelProperty(value = "来源批次id")
    private Long sourceProductPlanId;

    /**
     * 产出量
     */
    @ApiModelProperty(value = "产出量", example = "100")
    private BigDecimal outputQuantity = BigDecimal.ZERO;

    /**
     * 消耗量
     */
    @ApiModelProperty(value = "消耗量", example = "100")
    private BigDecimal consumeQuantity = BigDecimal.ZERO;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    /**
     * 物料平衡(修正数值)
     */
    @ApiModelProperty(value = "物料平衡(修正数值)", example = "1")
    private BigDecimal fixRate = BigDecimal.ONE;

    /**
     * 是否显示收率
     */
    @ApiModelProperty(value = "是否显示收率", example = "true")
    private Boolean showPercentYield;

    /**
     * 工艺名称
     */
    @ApiModelProperty(value = "工艺名称")
    private String processName;

    /**
     * 工艺版本号
     */
    @ApiModelProperty(value = "工艺版本号")
    private String processVersion;

    /**
     * 来源工艺名称
     */
    @ApiModelProperty(value = "来源工艺名称")
    private String sourceProcessName;

    /**
     * 来源工艺版本号
     */
    @ApiModelProperty(value = "来源工艺版本号")
    private String sourceProcessVersion;

    /**
     * 下级列表
     */
    @ApiModelProperty("子物料列表")
    private List<MaterialTraceMaterialView> children = new ArrayList<>();

    /**
     * 消耗信息
     */
    @ApiModelProperty(value = "消耗信息")
    private List<MaterialTraceMaterialStepView> consumeList = new ArrayList<>();

    /**
     * 产出信息
     */
    @ApiModelProperty(value = "产出信息")
    private List<MaterialTraceMaterialStepView> outputList = new ArrayList<>();

    /**
     * 是否参与计算
     */
    @ApiModelProperty(value = "是否参与计算", example = "true")
    private Boolean calcFlag = true;

    /**
     * 收率范围
     */
    @ApiModelProperty(value = "收率范围")
    private PercentYieldRange percentYieldRange;

    @JsonIgnore
    private List<MaterialTraceMaterialStepView> getAllConsumeList() {
        List<MaterialTraceMaterialStepView> allConsumeList = new ArrayList<>();
        for (MaterialTraceMaterialView child : children) {
            allConsumeList.addAll(child.getConsumeList());
        }
        return allConsumeList.stream()
                .filter(MaterialTraceMaterialStepView::getCalcFlag)
                .collect(Collectors.toList());
    }

    public BigDecimal getConsumeQuantity() {
        List<MaterialTraceMaterialStepView> allConsumeList = consumeList;
        if (allConsumeList.isEmpty()) {
            return BigDecimal.ZERO;
        }
        UnitCache bean = SpringUtil.getBean(UnitCache.class);
        BigDecimal result = new BigDecimal(Objects.requireNonNull(bean.calcSumAdapt(UnitCalcDTO.builder()
                .list(allConsumeList.stream().map(item -> UnitCalcDTO.UnitCalc.builder()
                        .unitId(item.getUnitId())
                        .value(item.getQuantity().toPlainString())
                        .build()).collect(Collectors.toList()))
                .targetUnitId(this.getUnitId())
                .build())).getValue());
        return PrecisionHelper.precision(result, this.getUnitId());
    }

    public BigDecimal getOutputQuantity() {
        List<MaterialTraceMaterialStepView> allOutputList = outputList;
        if (allOutputList.isEmpty()) {
            return BigDecimal.ZERO;
        }
        UnitCache bean = SpringUtil.getBean(UnitCache.class);
        BigDecimal result = new BigDecimal(Objects.requireNonNull(bean.calcSumAdapt(UnitCalcDTO.builder()
                .list(allOutputList.stream().map(item -> UnitCalcDTO.UnitCalc.builder()
                        .unitId(item.getUnitId())
                        .value(item.getQuantity().toPlainString())
                        .build()).collect(Collectors.toList()))
                .targetUnitId(this.getUnitId())
                .build())).getValue());
        return PrecisionHelper.precision(result, getUnitId());
    }

    // 前端需要显示
    public BigDecimal getFixRate() {
        BigDecimal out = getOutputQuantity();
        if (Objects.equals(out, BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        List<MaterialTraceMaterialStepView> allConsumeList = getAllConsumeList();
        if (allConsumeList.isEmpty()) {
            return null;
        }
        // 和产品确定 目前先不考虑单位转换 直接数值相加
        BigDecimal consume = allConsumeList.stream().map(MaterialTraceMaterialStepView::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 保留百分比后两位小数
        BigDecimal result = out.divide(consume, 4, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100L));
        return result.setScale(2, RoundingMode.HALF_EVEN);
    }

    // 前端需要显示
    public String getFullName() {
        return mergeCode + "-" + materialName;
    }
}
