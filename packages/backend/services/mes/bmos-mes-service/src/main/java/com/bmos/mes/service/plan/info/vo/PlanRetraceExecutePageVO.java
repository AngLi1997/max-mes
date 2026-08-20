package com.bmos.mes.service.plan.info.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批次追溯-生产批次执行信息
 */
@ApiModel("批次追溯-生产批次执行信息")
@Data
public class PlanRetraceExecutePageVO {

    /**
     * 工序名称
     */
    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("工步/步骤名称")
    private String procedureStepName;

    /**
     * 工艺班次 班次1
     */
    @ApiModelProperty("工艺班次")
    private String processChangeNum;

    /**
     * 工序班次 班次1
     */
    @ApiModelProperty("工序班次")
    private String procedureStepNum;

    @ApiModelProperty("工步开始时间")
    private String procedureStepStartTime;

    /**
     * 工步结束时间
     */
    @ApiModelProperty("工步结束时间")
    private String procedureStepEndTime;

    /**
     * 完成人 用户名称-登录名称
     */
    @ApiModelProperty("完成人")
    private String completer;

    public String getCompleter(){
        if (StrUtil.isBlank(completer)){
            return "";
        }
        BaseUserDO user = UserUtils.getUser(completer);
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }

}
