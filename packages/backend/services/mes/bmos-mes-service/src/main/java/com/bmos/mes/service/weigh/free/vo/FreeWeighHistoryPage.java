package com.bmos.mes.service.weigh.free.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.unit.service.UnitCache;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/2/27 10:44
 */
@Data
@ApiModel("自由称量历史分页")
public class FreeWeighHistoryPage {

    @ApiModelProperty(value = "物料件号", example = "01")
    private String storageMaterialNo;

    @ApiModelProperty(value = "物料件id", example = "1")
    private Long storageMaterialId;

    @ApiModelProperty(value = "称量时间", example = "2025-02-27 10:44:00")
    private LocalDateTime weighTime;

    @ApiModelProperty(value = "物料合并编码", example = "01")
    private String mergeCode;

    @ApiModelProperty(value = "物料名称", example = "物料")
    private String materialName;

    @ApiModelProperty(value = "物料批次号", example = "01")
    private String storageMaterialBatchNo;

    @ApiModelProperty(value = "皮重", example = "1.00")
    private BigDecimal tareWeight;

    @ApiModelProperty(value = "毛重", example = "1.00")
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "净重", example = "1.00")
    private BigDecimal netWeight;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位", example = "g")
    private String unit;

    @ApiModelProperty(value = "称量人id", example = "1")
    private String weigherId;

    @ApiModelProperty(value = "称量人名称", example = "张三")
    private String weigherName;

    @ApiModelProperty(value = "复核人id", example = "1")
    private String reCheckerId;

    @ApiModelProperty(value = "复核人名称", example = "张三")
    private String reCheckerName;

    @ApiModelProperty(value = "容器名称", example = "RQ-容器")
    private String containerName;

    @ApiModelProperty(value = "货位名称", example = "HW-货位")
    private String positionName;

    @ApiModelEnumProperty(value = "物料类型", enumClass = CategoryInfoTypeEnum.class)
    private CategoryInfoTypeEnum categoryType;

    public String getUnit() {
        if (unitId == null){
            return null;
        }
        return SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }

    public String getReCheckerName() {
        if (reCheckerId == null){
            return null;
        }
        return UserUtils.getUsername(reCheckerId);
    }

    public String getWeigherName() {
        if (weigherId == null){
            return null;
        }
        return UserUtils.getUsername(weigherId);
    }
}
