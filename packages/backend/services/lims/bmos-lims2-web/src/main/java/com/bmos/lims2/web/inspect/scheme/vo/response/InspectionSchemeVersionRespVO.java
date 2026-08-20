package com.bmos.lims2.web.inspect.scheme.vo.response;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.common.enums.InspectionSchemeVersionStatusEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 检验方案版本响应VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案版本响应")
public class InspectionSchemeVersionRespVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("关联的检验方案ID")
    private Long schemeId;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("版本状态：EDITING-编辑中, APPROVING-审批中, ACTIVE-生效, INACTIVE-失效")
    private InspectionSchemeVersionStatusEnum status;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty("生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    /**
     * 关联的审批流程实例ID
     */
    @ApiModelProperty("关联的审批流程实例ID")
    private String processInstanceId;

    @ApiModelProperty("父版本ID")
    private Long parentVersionId;

    @ApiModelProperty("父版本号")
    private String parentVersionNo;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人ID")
    private Long createBy;

    @ApiModelProperty("创建人名称")
    private String createByName;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("更新人ID")
    private Long updateBy;

    @ApiModelProperty("更新人名称")
    private String updateByName;


    public String getCreateByName() {
        BaseUserDO user = UserUtils.getUser(createBy+"");
        return ObjectUtil.isNotEmpty(user) ? (user.getUserName()+ StrUtil.DASHED+user.getLoginName()) : "";
    }

    public String getUpdateByName() {
        BaseUserDO user = UserUtils.getUser(updateBy+"");
        return ObjectUtil.isNotEmpty(user) ? (user.getUserName()+ StrUtil.DASHED+user.getLoginName()) : "";
    }
}