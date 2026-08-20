package com.bmos.lims2.server.audit.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName AuditMessageVO
 * @Author Ren Jin Guang
 * @Date 2025/1/9 17:34
 */
@Setter
@Getter
@ToString
public class AuditMessageVO {

    @ApiModelProperty("业务主体")
    private String businessText;

    @ApiModelProperty("用户id集合")
    private List<String> userIdList;

    @ApiModelProperty("部门集合")
    private Long businessId;
}
