package com.bmos.mes.service.process.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel("工序查询VO")
public class ProcedureQueryDTO {

    @ApiModelProperty("工艺id，与 version 同时传")
    private Long processId;

    @ApiModelProperty("工艺版本id，可单独使用")
    private Long processVersionId;

    @ApiModelProperty("版本号")
    private String version;

    @JsonIgnore
    public boolean validate() {
        if (ObjectUtil.isNotNull(processId)) {
            if (StrUtil.isEmpty(version)) {
                throw new BmosException(MesResponseCode.PROCESS_VERSION_EMPTY);
            }
            return true;
        }
        if (ObjectUtil.isNotNull(processVersionId)) {
            throw new BmosException(MesResponseCode.PROCESS_VERSION_ID_EMPTY);
        }
        return true;
    }
}
