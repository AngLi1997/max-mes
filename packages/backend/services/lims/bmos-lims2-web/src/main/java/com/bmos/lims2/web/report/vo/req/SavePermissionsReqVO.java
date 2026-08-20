package com.bmos.lims2.web.report.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@ApiModel("保存模板数据权限请求")
public class SavePermissionsReqVO {

    @ApiModelProperty("部门ID集合")
    @NotEmpty
    private List<Long> deptIds;
}
