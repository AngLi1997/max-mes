package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("文件上传")
@ToString
public class ExecuteAttachmentUploadDTO {


    @ApiModelProperty(value = "文件",required = true)
    @NotNull
    private MultipartFile file;

    @ApiModelProperty(value = "文件类型",required = true)
    private String type;

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull
    private Long productPlanId;

    /**
     * 批号
     */
    @ApiModelProperty(value = "批号",required = true)
    @NotEmpty
    private String batchNo;

    /**
     * 工艺id
     */
    @ApiModelProperty(value = "工艺id",required = true)
    @NotNull
    private Long processId;

    /**
     * 工艺版本
     */
    @ApiModelProperty(value = "工艺版本",required = true)
    @NotEmpty
    private String processVersion;

    /**
     * 记录项id
     */
    @ApiModelProperty(value = "记录项id",required = true)
    @NotNull
    private Long recordItemId;  /**
     * 记录项id
     */
    @ApiModelProperty(value = "记录版本id",required = true)
    @NotNull
    private Long recordVersionId;

    /**
     * 历史工序步骤id
     */
    @ApiModelProperty(value = "工序步骤id",required = true)
    @NotNull
    private Long procedureStepId;

    /**
     * 是否复用
     */
    @ApiModelProperty(value = "是否复用",required = true)
    @NotNull
    private Boolean reuse;

    /**
     * 复制版本（默认0）
     */
    @ApiModelProperty(value = "复制版本号",required = true)
    @NotNull
    private Long copyVersion;

    /**
     * 工艺换班次数
     */
    @ApiModelProperty(value = "工艺换班次数",required = true)
    @NotNull
    private Integer processChangeNumber;

    /**
     * 工序换班次数
     */
    @ApiModelProperty(value = "工序换班次数",required = true)
    @NotNull
    private Integer procedureChangeNumber;

    @ApiModelProperty("备注信息")
    private String remark;
}
