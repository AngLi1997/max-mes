package com.bmos.mes.service.plan.team.vo;

import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
* 生产计划班组
*/
@Getter
@Setter
@NoArgsConstructor
@ApiModel("ProductPlanTeamDetailItemVO:用户信息回显")
public class ProductPlanTeamDetailItemVO {
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("登录名")
    private String loginName;
    @ApiModelProperty("用户名")
    private String userName;

    public ProductPlanTeamDetailItemVO(BaseUserDO baseUserDO) {
        this.userId = baseUserDO.getUserId();
        this.loginName = baseUserDO.getLoginName();
        this.userName = baseUserDO.getUserName();
    }
}
