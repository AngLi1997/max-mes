package com.bmos.platform.service.equipment.controller.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointDataTypeEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointStatusEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author yigaohui
 * @date 2024/4/20
 **/
@Data
public class AcquisitionPointPageQueryVO extends BasePage {
    @ApiModelProperty(value = "编码")
    @Size(max = 128, message = "{acquisitionPoint.code.size}")
    private String code;

    @ApiModelProperty(value = "名称")
    @Size(max = 255, message = "{acquisitionPoint.name.size}")
    private String name;

    @ApiModelEnumProperty(value = "采集点类型", enumClass = AcquisitionPointTypeEnum.class, required = true)
    @EnumValidate(value = AcquisitionPointDataTypeEnum.class)
    private AcquisitionPointTypeEnum type;

    /**
     * 状态
     */
    private AcquisitionPointStatusEnum status;
}
