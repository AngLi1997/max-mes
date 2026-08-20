package com.bmos.lims2.server.audit.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@ApiModel(value = "流程参数vo")
public class SettingVO {

    private List<String> buttons;

    private String completeType;

    private List<String> strategy;

    private Boolean needCommit;

    private Boolean needRemark;

    private Boolean needPwdValidate;
}
