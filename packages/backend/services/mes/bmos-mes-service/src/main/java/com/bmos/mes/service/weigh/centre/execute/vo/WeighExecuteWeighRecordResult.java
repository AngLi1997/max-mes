package com.bmos.mes.service.weigh.centre.execute.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.ingredient.WeighType;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
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
 * @date 2024/7/10 16:36
 */
@Data
@ApiModel("物料称量称量记录列表结果")
public class WeighExecuteWeighRecordResult {

    @ApiModelProperty(value = "称量记录id", example = "1")
    private Long id;

    @ApiModelProperty(value = "物料件id", example = "1")
    private Long storageMaterialId;

    @ApiModelProperty(value = "物料件号", example = "123456")
    private String storageMaterialNo;

    @ApiModelProperty(value = "物料名称", example = "PBS缓冲液")
    private String materialName;

    @ApiModelProperty(value = "物料合并编码", example = "123")
    private String materialMergeCode;

    @ApiModelEnumProperty(value = "物料类型", enumClass = CategoryInfoTypeEnum.class)
    @EnumValidate(CategoryInfoTypeEnum.class)
    private CategoryInfoTypeEnum categoryType;

    @ApiModelProperty(value = "物料批号", example = "1")
    private String storageMaterialBatchNo;

    @ApiModelProperty(value = "皮重", example = "1.00")
    private BigDecimal tareWeight;

    @ApiModelProperty(value = "毛重", example = "1.00")
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "净重", example = "1.00")
    private BigDecimal netWeight;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    @ApiModelProperty(value = "产品名称", example = "PBS缓冲液")
    private String productName;

    @ApiModelProperty(value = "产品编码", example = "RY01001")
    private String productMergeCode;

    @ApiModelProperty(value = "生产批号", example = "RY01001-2406002")
    private String batchNo;

    @ApiModelProperty(value = "工艺名称", example = "PBS缓冲液配置")
    private String processName;

    @ApiModelProperty(value = "称量人id", example = "1")
    private String weigherId;

    @ApiModelProperty(value = "称量人名称", example = "张三")
    private String weigherName;

    @ApiModelProperty(value = "称量人登录名", example = "张三")
    private String weigherLoginName;

    @ApiModelProperty(value = "复核人id", example = "1")
    private String reCheckerId;

    @ApiModelProperty(value = "复核人名称", example = "张三")
    private String reCheckerName;

    @ApiModelProperty(value = "复核人登录名", example = "张三")
    private String reCheckerLoginName;

    @ApiModelProperty(value = "称量时间", example = "2024-07-10 16:36:00")
    private LocalDateTime weighTime;

    @ApiModelProperty(value = "容器名称", example = "1")
    private String containerName;

    @ApiModelProperty(value = "货位名称", example = "1")
    private String materialPositionName;

    @ApiModelEnumProperty(value = "称量类型", enumClass = WeighType.class)
    private WeighType weighType;

    @ApiModelEnumProperty(value = "称量状态", enumClass = WeighSignStatus.class)
    private WeighSignStatus signStatus;

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
        this.unit = SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }

    public void setWeigherId(String weigherId) {
        this.weigherId = weigherId;
        BaseUserDO user = UserUtils.getUser(weigherId);
        if (user != null){
            this.weigherName = user.getUserName();
            this.weigherLoginName = user.getLoginName();
        }
    }

    public void setReCheckerId(String reCheckerId) {
        this.reCheckerId = reCheckerId;
        BaseUserDO user = UserUtils.getUser(reCheckerId);
        if (user != null){
            this.reCheckerName = user.getUserName();
            this.reCheckerLoginName = user.getLoginName();
        }
    }
}