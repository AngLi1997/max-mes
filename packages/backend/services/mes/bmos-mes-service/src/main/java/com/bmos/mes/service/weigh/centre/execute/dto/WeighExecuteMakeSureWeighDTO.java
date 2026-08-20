package com.bmos.mes.service.weigh.centre.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料称量确认称量人dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 16:25
 */
@ApiModel("物料称量确认称量人dto")
@Data
public class WeighExecuteMakeSureWeighDTO {

    /**
     * 需求id
     */
    @ApiModelProperty(value = "需求id", example = "1", required = true)
    @NotNull
    private Long requirementId;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id(首次确认时必填)", example = "1")
    private String weigherId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id(首次确认时必填)", example = "1")
    private String reCheckerId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    @Length(max = 200)
    private String remark;

    /**
     * 消耗物料件id列表
     */
    @ApiModelProperty(value = "消耗物料件id列表")
    private List<Long> consumeStorateMaterialIdList = new ArrayList<>();
}
