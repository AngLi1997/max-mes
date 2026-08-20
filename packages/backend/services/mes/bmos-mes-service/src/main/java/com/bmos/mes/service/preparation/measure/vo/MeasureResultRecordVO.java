package com.bmos.mes.service.preparation.measure.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.preparation.MeasureTypeEnum;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("配液量取结果记录VO")
public class MeasureResultRecordVO {

    @ApiModelProperty("签名状态")
    private WeighSignStatus signStatus;

    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("物料批次id")
    private Long storageMaterialBatchId;

    @ApiModelProperty("物料件号")
    private String storageMaterialNo;

    @ApiModelProperty("物料量")
    private BigDecimal quantity;

    @ApiModelProperty("单位")
    private String unitName;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("量取人名称")
    private String measurerName;

    @ApiModelProperty("量取人id")
    private String measurerId;

    @ApiModelProperty("量取类型")
    private MeasureTypeEnum measureType;

    @ApiModelProperty("复核人id")
    private String reCheckerId;

    @ApiModelProperty("复核人名称")
    private String reCheckerName;

    @ApiModelProperty("容器编码-名称")
    private String containerName;

    @ApiModelProperty("货位编码-名称")
    private String positionName;

    @ApiModelProperty("物料规格")
    private String specification;

    @ApiModelProperty("量取时间")
    private LocalDateTime measureTime;

    @ApiModelProperty("量取批次id")
    private Long measureBatchId;

    @ApiModelProperty("物料类型")
    private Integer materialType;

    public CategoryInfoTypeEnum getCategoryType() {
        return CategoryInfoTypeEnum.getEnumByValue(materialType);
    }

    public String getMeasurerName() {
        BaseUserDO user = UserUtils.getUser(measurerId);
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }

    public String getReCheckerName() {
        BaseUserDO user = UserUtils.getUser(reCheckerId);
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }
}
