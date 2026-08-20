package com.bmos.mes.service.execute.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.service.operate.dto.version.UpdateStateDTO;
import com.bmos.mes.service.utils.UserUtils;
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
public class AttachmentVO {
    @Tolerate
    public AttachmentVO(){}

    private Long id;

    private String path;

    @ApiModelProperty("文件类型")
    private String type;

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
