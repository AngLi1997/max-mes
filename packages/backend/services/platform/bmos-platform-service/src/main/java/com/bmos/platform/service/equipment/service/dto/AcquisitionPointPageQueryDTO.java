package com.bmos.platform.service.equipment.service.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointDataTypeEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointStatusEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yigaohui
 * @date 2024/4/20
 **/
@Data
public class AcquisitionPointPageQueryDTO extends BasePage {

    private String code;

    private String name;

    private AcquisitionPointTypeEnum type;

    private AcquisitionPointStatusEnum status;
}
