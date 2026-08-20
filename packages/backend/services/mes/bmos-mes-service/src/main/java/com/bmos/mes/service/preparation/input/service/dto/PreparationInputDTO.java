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
public class PreparationInputDTO extends BusinessDataHandleBaseDTO {

    /**
     * 配液单id
     */
    @ApiModelProperty(value = "配液单id", example = "1")
    @NotNull
    private Long preparationPlanId;

    /**
     * 投入物料件编号列表
     */
    @ApiModelProperty("投入物料件编号列表")
    @NotEmpty
    private List<String> storateMaterialNoList;

    /**
     * 投入设备id
     */
    @ApiModelProperty(value = "投入设备id", example = "1")
    @NotNull
    private Long deviceId;

    /**
     * 投入人id
     */
    @ApiModelProperty(value = "投入人id", example = "1")
    @NotBlank
    private String inputUserId;

    /**
     * 投入备注
     */
    @ApiModelProperty(value = "投入备注", example = "备注")
    @Length(max = 100)
    private String remark;

}
