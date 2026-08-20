package com.bmos.mes.service.ingredient.input.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.ingredient.IngredientInputStatus;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/26 11:24
 */
@Data
@ApiModel("待投料物料件")
public class IngredientInputRecordVO {
    /**
     * 物料件id
     */
    @ApiModelProperty(value = "物料件id", example = "1")
    private Long storageMaterialId;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码", example = "001")
    private String materialMergeCode;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "001")
    private String storageMaterialBatchNo;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "001")
    private String storageMaterialNo;

    /**
     * 物料量
     */
    @ApiModelProperty(value = "物料量", example = "1")
    @PrecisionValue
    private BigDecimal quantity;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    @PrecisionUnitId
    private Long unitId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    /**
     * 投料状态
     */
    @ApiModelEnumProperty(value = "投料状态", enumClass = IngredientInputStatus.class)
    @EnumValidate(IngredientInputStatus.class)
    private IngredientInputStatus inputStatus;

    /**
     * 投料人id
     */
    @ApiModelProperty(value = "投料人id", example = "1")
    private String importerId;

    /**
     * 投料人姓名
     */
    @ApiModelProperty(value = "投料人姓名", example = "张三")
    private String importerName;

    /**
     * 投料时间
     */
    @ApiModelProperty(value = "投料时间", example = "2024-04-25 00:00:00")
    private LocalDateTime inputTime;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id", example = "1")
    private Long deviceId;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称", example = "不锈钢盆儿")
    private String deviceName;

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号", example = "01")
    private String deviceCode;

    /**
     * 签名状态
     */
    @ApiModelEnumProperty(value = "签名状态", enumClass = WeighSignStatus.class)
    @EnumValidate(WeighSignStatus.class)
    private WeighSignStatus weighSignStatus;

    /**
     * 组件实例id
     */
    @ApiModelProperty(value = "组件实例id", example = "1")
    private Long componentInstanceId;

    public String getImporterName() {
        if(importerId == null){
            return StrUtil.EMPTY;
        }
        BaseUserDO user = Optional.ofNullable(UserUtils.getUser(importerId)).orElse(new BaseUserDO());
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }
}
