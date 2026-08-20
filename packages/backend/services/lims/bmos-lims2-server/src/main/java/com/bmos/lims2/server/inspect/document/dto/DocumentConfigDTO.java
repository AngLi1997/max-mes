package com.bmos.lims2.server.inspect.document.dto;


import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@ApiModel("请验单配置DTO")
@Data
public class DocumentConfigDTO {

    @ApiModelProperty("请验单id")
    private Long id;

    @ApiModelProperty("请验单名称")
    private String name;

    @ApiModelProperty("请验单创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("最后更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("最后更新人id")
    private String updateUser;

    @ApiModelProperty("最后更新人")
    private String updateBy;

    @ApiModelProperty("启停状态")
    private Boolean status;

    @ApiModelProperty("备注信息")
    private String remark;

    public String getUpdateUser() {
        BaseUserDO user = UserUtils.getUser(updateBy);
        return Optional.ofNullable(user).map(e -> e.getUserName() + "-" + e.getLoginName()).orElse(null);
    }


}
