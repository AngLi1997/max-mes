package com.bmos.lims2.web.inspect.retention.vo.resp;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 留样销毁台账列表响应VO
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
@ApiModel("留样销毁台账列表响应")
public class RetentionDestructionLedgerListRespVO {

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("规格")
    private String materialSpec;

    @ApiModelProperty("销毁数量")
    private String quantity;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("销毁原因")
    private String destructionReason;

    @ApiModelProperty("销毁方式")
    private String destructionMethod;

    @ApiModelProperty("销毁地点")
    private String destructionLocation;

    @ApiModelProperty("销毁时间")
    private LocalDateTime destructionTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("销毁人ID")
    private String destructorId;

    @ApiModelProperty("监督人ID")
    private String supervisorId;

    /**
     * 销毁人名称
     */
    public String getDestructorName() {
        BaseUserDO user = UserUtils.getUser(destructorId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

    /**
     * 监督人名称
     */
    public String getSupervisorName() {
        BaseUserDO user = UserUtils.getUser(supervisorId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
