package com.bmos.mes.service.weigh.centre.requirement.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.unit.service.UnitCache;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 称量需求分页查询vo
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 17:56
 */
@Data
@ApiModel("称量需求分页查询vo")
public class WeighRequirementVO {

    @ApiModelProperty(value = "称量需求id", example = "1")
    private Long id;

    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    @ApiModelProperty(value = "物料编码", example = "WH03")
    private String materialMergeCode;

    @ApiModelProperty(value = "物料规格", example = "1个")
    private String materialSpecification;

    @ApiModelProperty(value = "称量中心名称", example = "KQ-PYCL-101-狂犬疫苗配液称量中心")
    private String weighCentreName;

    @ApiModelProperty(value = "需求日期", example = "2024-07-01")
    private LocalDate requirementDate;

    @ApiModelProperty(value = "需求量", example = "8.000")
    private BigDecimal requirementQuantity;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位", example = "ml")
    private String unit;

    @ApiModelProperty(value = "产品名称", example = "PBS缓冲液")
    private String productName;

    @ApiModelProperty(value = "产品编码", example = "RY01001")
    private String productMergeCode;

    @ApiModelProperty(value = "工艺流程名称", example = "PBS缓冲液配置")
    private String processName;

    @ApiModelProperty(value = "生产批号", example = "RY01001-2406002")
    private String batchNo;

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
        this.unit = SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }
}
