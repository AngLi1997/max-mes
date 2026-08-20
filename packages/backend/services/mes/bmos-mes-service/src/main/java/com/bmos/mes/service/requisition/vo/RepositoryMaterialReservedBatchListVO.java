package com.bmos.mes.service.requisition.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("仓库已预订物料批次列表VO")
@Data
public class RepositoryMaterialReservedBatchListVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("计划领料量")
    private BigDecimal plannedQuantity;

    @ApiModelProperty("理论量")
    private BigDecimal theoreticalQuantity;

    @ApiModelProperty("单位")
    private String unitName;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("计划人id")
    private String userId;

    @ApiModelProperty("有效日期")
    private String expiredDate;

    @ApiModelProperty("物料编码")
    private String mergeCode;

    @ApiModelProperty("物料规格")
    private String specification;

    @ApiModelProperty("计划人")
    private String userName;

    public String getUserName(){
        BaseUserDO user = UserUtils.getUser(userId);
        return ObjectUtil.isNotEmpty(user) ? (user.getUserName()+ StrUtil.DASHED+user.getLoginName()) : "";
    }

}
