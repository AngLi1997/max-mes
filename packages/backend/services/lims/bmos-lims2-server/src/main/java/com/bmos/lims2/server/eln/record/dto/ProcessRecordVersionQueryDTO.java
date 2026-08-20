package com.bmos.lims2.server.eln.record.dto;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel("工艺批记录版本查询DTO")
@Data
public class ProcessRecordVersionQueryDTO {

    @ApiModelProperty("工艺版本id")
    @NotNull
    private Long processVersionId;

    @ApiModelProperty(value = "记录id字符串",example = "id1,id2,id3")
    @NotBlank
    private String recordIdStr;

    public List<Long> getRecordIdList() {
        return StrUtil.split(recordIdStr, StrUtil.COMMA).stream().map(Long::valueOf).collect(Collectors.toList());
    }

}
