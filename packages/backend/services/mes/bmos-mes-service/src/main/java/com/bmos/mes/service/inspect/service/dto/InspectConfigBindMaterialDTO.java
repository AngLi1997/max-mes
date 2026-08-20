package com.bmos.mes.service.inspect.service.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 请验单绑定物料
 */
@Getter
@Setter
@ApiModel("请验单绑定物料")
public class InspectConfigBindMaterialDTO {

    /**
     * 请验单id
     */
    private Long id;

    /**
     * 绑定物料id集合
     */
    private List<Long> materialIdList;

}
