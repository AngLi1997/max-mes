package com.bmos.lims2.server.eln.entry.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("附件VO")
@Builder
public class AttachmentDTO {
    @Tolerate
    public AttachmentDTO(){}

    private Long id;

    private String path;

    @ApiModelProperty("文件类型")
    private String type;

    private String fileName;

    private String createBy;

    private String createUsername;

    private LocalDateTime createTime;

    @ApiModelProperty("备注信息")
    private String remark;

    public String getCreateUsername() {
        BaseUserDO user = UserUtils.getUser(createBy);
        if (ObjectUtil.isEmpty(user)){
            return "";
        }
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }
}
