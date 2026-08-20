package com.bmos.mes.service.workflow.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @ClassName StepChangeTeamListVO
 * @Description 查询工步班次信息vo
 * @Author Ren Jin Guang
 * @Date 2024/8/27 10:45
 */
@Setter
@Getter
@ToString
@ApiModel("工步换班信息vo")
public class StepChangeTeamListVO {

    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("状态名称")
    private String stateName;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("完成人")
    private String completeBy;

    public String getCompleteBy(){
        if (StrUtil.isBlank(completeBy)){
            return null;
        }
        BaseUserDO user = UserUtils.getUser(completeBy);
        return user.getLoginName() + StrUtil.DASHED + user.getUserName();
    }
}
