package com.bmos.lims2.server.inspect.retention.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description: 留样样品打印标签结果DTO
 * @Author: yigaohui
 * @Date: 2026/02/12
 */
@Data
@ApiModel("留样样品打印标签结果数据对象")
public class RetentionSamplePrintTagResultDTO {

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("全检品名称（编码-名称）")
    private String fullMaterialName;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("样品数量")
    private String quantity;

    @ApiModelProperty("单位ID")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("数量带单位")
    private String quantityWithUnit;

    @ApiModelProperty("取样时间")
    private LocalDateTime samplingTime;

    @ApiModelProperty("接收人ID")
    private String receiverId;

    @ApiModelProperty("留样时间（接收时间）")
    private LocalDateTime retentionTime;

    @ApiModelProperty("留样期限")
    private LocalDate retentionExpiryDate;

    @ApiModelProperty("储存位置")
    private String storageLocation;

    /**
     * 获取留样人名称（格式：用户名-登录名）
     */
    public String getRetentionUserName() {
        BaseUserDO user = UserUtils.getUser(receiverId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
