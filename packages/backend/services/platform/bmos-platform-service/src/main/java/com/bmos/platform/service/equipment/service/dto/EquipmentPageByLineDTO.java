package com.bmos.platform.service.equipment.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("根据产线id分页查询设备信息")
public class EquipmentPageByLineDTO extends BasePage {

    @NotNull
    private Long productionLineId;

}
