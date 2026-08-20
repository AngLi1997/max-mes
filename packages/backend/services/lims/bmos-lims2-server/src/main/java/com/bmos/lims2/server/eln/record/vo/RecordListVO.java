package com.bmos.lims2.server.eln.record.vo;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.eln.record.enums.RecordStateEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@ApiModel("记录管理页vo")
public class RecordListVO {

    @ApiModelProperty(value = "记录id")
    private Long recordId;

    @ApiModelProperty(value = "记录名称")
    private String name;

    @ApiModelProperty(value = "记录编码")
    private String code;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态")
    private RecordStateEnum state;

    @ApiModelProperty(value = "状态名称")
    private String stateName;

    @ApiModelProperty(value = "流程实例id")
    private String instanceId;

    /*public String getStateName() {
        return EnumUtils.getValueByName(RecordStateEnum.values(),state);
    }*/

    @ApiModelProperty(value = "版本id")
    private Long versionId;

    @ApiModelProperty(value = "分析项ID")
    private Long parameterId;

    @ApiModelProperty(value = "分析项编码")
    private String parameterCode;

    @ApiModelProperty(value = "已绑定的操作规程ID集合")
    private java.util.List<Long> operateIdList;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty("创建人名称")
    private String createByUsername;

    @ApiModelProperty(value = "生效日期")
    private String effectDate;

    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effectiveTime;


    public String getCreateByUsername() {
        BaseUserDO user = UserUtils.getUser(createBy);
        if (ObjectUtil.isEmpty(user)){
            return "";
        }
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }

    public void setEffectiveTime(LocalDateTime effectiveTime) {
        this.effectiveTime = effectiveTime;
        if (effectiveTime != null) {
            this.effectDate = effectiveTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            this.effectDate = null;
        }
    }
}
