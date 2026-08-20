package com.bmos.platform.service.execute.parameter.dto;

import cn.hutool.core.util.NumberUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.platform.common.enums.execute.parameter.ValueTypeEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("UpdateBusinessParameterDTO:更新DTO")
public class UpdateBusinessParameterDTO {
    @NotNull
    @ApiModelProperty("编码")
    private Long id;

    @NotEmpty
    @ApiModelProperty("值")
    private String value;

    @JsonIgnore
    public void validType(ValueTypeEnum valueTypeEnum) {
        if (valueTypeEnum == ValueTypeEnum.BOOLEAN) {
            if (!"true".equals(value) && !"false".equals(value)) {
                throw new BmosException(PlatformResponseCode.NUMBER_TYPE_NOT_MATCH_VALUE);
            }
        }
        if (valueTypeEnum == ValueTypeEnum.JSON && !JsonUtils.isJson(value)) {
            throw new BmosException(PlatformResponseCode.NUMBER_TYPE_NOT_MATCH_VALUE);
        }
        if (valueTypeEnum == ValueTypeEnum.NUMBER && !NumberUtil.isNumber(value)) {
            throw new BmosException(PlatformResponseCode.NUMBER_TYPE_NOT_MATCH_VALUE);
        }
    }
}
