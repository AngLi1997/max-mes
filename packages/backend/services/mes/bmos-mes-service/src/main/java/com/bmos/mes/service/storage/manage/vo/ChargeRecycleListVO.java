package com.bmos.mes.service.storage.manage.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.storage.ChargeRecycleTypeEnum;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel("投料回收列表VO")
@Data
public class ChargeRecycleListVO {

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("物料批次id")
    private String storageMaterialBatchId;

    @ApiModelProperty("物料件号")
    private String storageMaterialNo;

    @ApiModelProperty("物料件id")
    private Long storageMaterialId;

    @ApiModelProperty("物料量")
    private BigDecimal quantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("操作类型")
    private ChargeRecycleTypeEnum operationType;

    @ApiModelProperty("物料类型")
    private CategoryInfoTypeEnum categoryInfoType;

    @ApiModelProperty("操作人id")
    private String operatorId;

    @ApiModelProperty("charge_recycle_component表主键id")
    private Long chargeRecycleComponentId;

    @ApiModelProperty("设备id")
    private Long equipmentId;

    @ApiModelProperty("设备名称")
    private String equipmentName;

    @ApiModelProperty("设备编号")
    private String equipmentCode;

    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("是否已用尽")
    private Boolean useUp;

    public String getOperator(){
        BaseUserDO user = UserUtils.getUser(operatorId);
        if (ObjectUtil.isEmpty(user)) {
            return null;
        }
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }


}
