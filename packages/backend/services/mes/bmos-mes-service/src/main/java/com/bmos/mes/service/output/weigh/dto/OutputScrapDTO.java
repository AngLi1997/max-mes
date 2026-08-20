package com.bmos.mes.service.output.weigh.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 称量报废DTO
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:02
 */
@ApiModel("称量报废dto")
@Data
public class OutputScrapDTO {

    /**
     * 产出称量流程id
     */
    @ApiModelProperty(value = "产出称量流程id", example = "1", required = true)
    @NotNull
    private Long outputWeighProcessId;


    /**
     * 称量报废物料id列表
     */
    @ApiModelProperty(value = "称量报废物料id列表", required = true)
    @NotEmpty
    private List<Long> scrapStorageMaterialIdList;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    private String remark;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1")
    private String weigherId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1")
    private String reCheckerId;
}
