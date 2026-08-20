package com.bmos.mes.service.inspect.service.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 请验单配置保存请求参数
 */
@Getter
@Setter
@ApiModel("请验单配置保存请求参数")
public class InspectConfigUpdateDTO extends InspectConfigSaveDTO {

    /**
     * 请验单配置id
     */
    private Long id;

}
