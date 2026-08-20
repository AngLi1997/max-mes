package com.bmos.mes.service.storage.manage.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@ApiModel("批次已预定暂存物料VO")
@Data
public class BatchReservedMaterialVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("物料件号")
    private String materialNo;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("暂存物料件id")
    private String storageMaterialId;

    @ApiModelProperty("可用量")
    private BigDecimal availableQuantity;

    @ApiModelProperty("预定量")
    private BigDecimal reserveQuantity;

    @ApiModelProperty("物料量")
    private BigDecimal quantity;

    @ApiModelProperty("理论量")
    private BigDecimal theoreticalQuantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("货位名")
    private String materialPositionName;

    @ApiModelProperty("货位编码")
    private String materialPositionCode;

    @ApiModelProperty("计划人")
    private String userName;

    @ApiModelProperty("预订人id")
    private String reserveUserId;

    @ApiModelProperty("有效日期")
    private String expiredDate;

    @ApiModelProperty("拓展单位id")
    private Long unitExtendId;

    @ApiModelProperty("水分 无则0")
    @NotNull
    private BigDecimal hydration;

    @ApiModelProperty("含量 无或大于100则100")
    @NotNull
    private BigDecimal noHydrationContent;

    @ApiModelProperty("供应商")
    private String supplier;

    @ApiModelProperty("生产商")
    private String producer;

    /**
     * 签名状态
     */
    private WeighSignStatus signStatus;


    public String getUserName(){
        BaseUserDO user = UserUtils.getUser(reserveUserId);
        return ObjectUtil.isNotEmpty(user) ? (user.getUserName()+StrUtil.DASHED+user.getLoginName()) : "";
    }

    public Long getUnitId(){
        return unitExtendId == null ? unitId : unitExtendId;
    }

    /**
     * 是否可用
     *
     * @return
     */
    public Boolean isAvailable() {
        if (signStatus == null) {
            return !(BigDecimal.ZERO.equals(availableQuantity) && BigDecimal.ZERO.equals(reserveQuantity));
        } else {
            return Objects.equals(signStatus, WeighSignStatus.SIGNED) && !(BigDecimal.ZERO.equals(availableQuantity) && BigDecimal.ZERO.equals(reserveQuantity));
        }
    }
}
