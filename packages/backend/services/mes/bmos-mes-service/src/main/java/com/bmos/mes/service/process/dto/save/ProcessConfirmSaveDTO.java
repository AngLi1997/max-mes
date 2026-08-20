package com.bmos.mes.service.process.dto.save;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "添加工艺审批结论数据")
@Validated
public class ProcessConfirmSaveDTO {

    @ApiModelProperty("产品id")
    @NotBlank
    private Long productId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("工艺id")
    @NotBlank
    private Long processId;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("生产批号")
    @NotBlank
    private String planBatchNo;

    @ApiModelProperty("生产开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("流程id")
    @NotBlank
    private String instanceId;
}
