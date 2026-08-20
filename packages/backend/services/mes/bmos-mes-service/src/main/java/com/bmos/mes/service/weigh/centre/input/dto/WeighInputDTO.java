package com.bmos.mes.service.weigh.centre.input.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 物料投入投料参数
 * @author liang
 * @version 1.0.0
 * @date 2024/7/26 09:44
 */
@Data
@ApiModel("物料投入投料参数")
public class WeighInputDTO {

    /**
     * 组件实例id
     */
    @ApiModelProperty(value = "组件实例id", example = "1")
    @NotNull
    private Long componentInstanceId;

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
