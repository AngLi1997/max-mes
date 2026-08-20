package com.bmos.mes.service.preparation.produce.service.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 产出报废DTO
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:02
 */
@ApiModel("产出报废dto")
@Data
public class ProduceScrapDTO{

    /**
     * 产出产出流程id
     */
    @ApiModelProperty(value = "产出产出流程id", example = "1", required = true)
    @NotNull
    private Long progressId;


    /**
     * 产出报废物料id列表
     */
    @ApiModelProperty(value = "产出报废物料id列表", required = true)
    @NotEmpty
    private List<Long> scrapStorageMaterialIdList;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    private String remark;

    /**
     * 产出人id
     */
    @ApiModelProperty(value = "产出人id", example = "1")
    private String producerId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1")
    private String reCheckerId;
}
