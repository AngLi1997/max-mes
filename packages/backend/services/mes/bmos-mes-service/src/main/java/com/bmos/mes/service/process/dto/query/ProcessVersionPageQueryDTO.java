package com.bmos.mes.service.process.dto.query;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工艺版本分页查询DTO")
public class ProcessVersionPageQueryDTO extends BasePage {

    @ApiModelProperty(value = "工艺id",required = true)
    @NotNull
    private Long processId;

    @ApiModelProperty(hidden = true)
    private List<Long> deptIds;
}
