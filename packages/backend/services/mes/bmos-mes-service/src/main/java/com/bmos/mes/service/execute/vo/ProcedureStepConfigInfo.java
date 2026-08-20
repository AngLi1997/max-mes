package com.bmos.mes.service.execute.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcedureStepConfigInfo {

    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

    /**
     * 配方物料id列表
     */
    private List<Long> formulaMaterialIds;

    /**
     * 工位名称
     */
    private List<Long> station;

    private String format;

    private int entryMethod;

}
