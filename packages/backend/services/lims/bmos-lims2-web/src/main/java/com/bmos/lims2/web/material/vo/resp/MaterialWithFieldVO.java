package com.bmos.lims2.web.material.vo.resp;

import com.bmos.lims2.server.material.dto.MaterialDTO;
import com.bmos.lims2.server.material.dto.MaterialFieldDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("检品管理列表 分页查询结果返回VO")
public class MaterialWithFieldVO extends MaterialDTO {
    @ApiModelProperty("自定义信息")
    private List<MaterialFieldDTO> fieldList;

    @ApiModelProperty("单位名称")
    private String unit;
}
