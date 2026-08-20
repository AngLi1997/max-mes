package com.bmos.mes.service.process.dto.save;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "工序审核结论数据dto")
@Validated
public class ProcedureConfirmSaveDTO {

    @ApiModelProperty("工序名称")
    @NotBlank
    private String procedureName;

    @ApiModelProperty("工序完成时间")
    private LocalDateTime procedureTime;

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;

    @ApiModelProperty("工艺结论id")
    @NotNull
    private Long processConfirmId;

}
