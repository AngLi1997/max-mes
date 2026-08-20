package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


/**
 * 工艺版本实体
 */
@Getter
@Setter
@ToString
@SuperBuilder(toBuilder = true)
@TableName("bm_process_version")
public class ProcessVersion extends BaseDO {

    @Tolerate
    public ProcessVersion(){

    }

    /**
     * 工艺id
     */
    @NotNull
    private Long processId;
    /**
     * 版本号
     */
    @NotNull
    private String version;

    /**
     * 配方版本id
     */
    @NotNull
    private Long productFormulaVersionId;


    /**
     * 流程模型id
     */
    @NotNull
    private String processModelId;

    /**
     * 描述
     */
    private String description;

    /**
     * 操作状态
     */
    @NotBlank
    private String actionState;

    /**
     * 启用/停用
     */
    @NotNull
    private Boolean state;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 生产阶段代码
     */
    private String productionStageCode;

    @ApiModelProperty("生效时间")
    private String effectDate;

    @ApiModelProperty("历史版本状态")
    private String historyState;
}
