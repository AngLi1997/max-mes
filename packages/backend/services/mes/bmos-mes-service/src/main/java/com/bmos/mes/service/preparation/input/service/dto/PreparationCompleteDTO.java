package com.bmos.mes.service.preparation.input.service.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 配液投入参数
 */
@Data
@ApiModel("配液投入参数")
public class PreparationCompleteDTO {

    /**
     * 配液单id
     */
    @ApiModelProperty(value = "组件实例id", example = "1")
    @NotNull
    private Long componentInstanceId;
}
