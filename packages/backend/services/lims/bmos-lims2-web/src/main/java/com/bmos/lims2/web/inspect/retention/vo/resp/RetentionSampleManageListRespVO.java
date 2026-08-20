package com.bmos.lims2.web.inspect.retention.vo.resp;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description: 留样样品管理列表响应VO
 * @Author: yigaohui
 * @Date: 2026/02/09
 */
@Data
@ApiModel("留样样品管理列表数据")
public class RetentionSampleManageListRespVO {

    @ApiModelProperty("样品ID")
    private Long id;

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

    @ApiModelProperty("接收数量")
    private String quantity;

    @ApiModelProperty("当前样品数量")
    private String currentQuantity;

    @ApiModelProperty("单位ID")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("留样时间（接收时间）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime retentionTime;

    @ApiModelProperty("留样人ID")
    private String retentionUserId;

    @ApiModelProperty("留样人名称")
    private String retentionUserName;

    @ApiModelProperty("留样期限")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate retentionExpiryDate;

    @ApiModelProperty("储存位置")
    private String storageLocation;

    @ApiModelProperty("接收状态")
    private Boolean received;

    @ApiModelProperty("待销毁状态")
    private Boolean toBeDestroyed;

    @ApiModelProperty("已销毁状态")
    private Boolean destroyed;

    public String getRetentionUserName() {
        BaseUserDO user = UserUtils.getUser(getRetentionUserId());
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
