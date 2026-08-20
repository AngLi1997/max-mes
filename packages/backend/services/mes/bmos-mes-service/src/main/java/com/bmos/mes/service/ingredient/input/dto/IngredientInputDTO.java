package com.bmos.mes.service.ingredient.input.dto;

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
 * @author liang
 * @version 1.0.0
 * @date 2024/4/25 22:53
 */
@Data
@ApiModel("投料参数")
public class IngredientInputDTO extends BusinessDataHandleBaseDTO {

    /**
     * 配料单id
     */
    @ApiModelProperty(value = "配料单id", example = "1")
    @NotNull
    private Long ingredientPlanId;

    /**
     * 投料物料件编号列表
     */
    @ApiModelProperty("投料物料件编号列表")
    @NotEmpty
    private List<String> storateMaterialNoList;

    /**
     * 投料设备id
     */
    @ApiModelProperty(value = "投料设备id", example = "1")
    @NotNull
    private Long deviceId;

    /**
     * 投料人id
     */
    @ApiModelProperty(value = "投料人id", example = "1")
    @NotBlank
    private String inputUserId;

    /**
     * 投料备注
     */
    @ApiModelProperty(value = "投料备注", example = "备注")
    @Length(max = 100)
    private String remark;
}
