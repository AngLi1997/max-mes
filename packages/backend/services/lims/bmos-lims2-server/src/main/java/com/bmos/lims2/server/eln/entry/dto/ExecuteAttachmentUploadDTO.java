package com.bmos.lims2.server.eln.entry.dto;

import com.bmos.lims2.server.eln.entry.enums.AttachmentTypeEnum;
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

    @ApiModelProperty("请验单id")
    private Long inspectionOrderId;

    @ApiModelProperty(value = "请验单编号")
    private String inspectionOrderNo;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 批号
     */
    @ApiModelProperty(value = "批号",required = true)
    @NotEmpty
    private String batchNo;


    @ApiModelProperty(value = "方法id",required = true)
    @NotNull
    private Long recordId;

    @ApiModelProperty(value = "方法版本id",required = true)
    private Long recordVersionId;

    @ApiModelProperty(value = "方案id",required = true)
    private Long schemeId;

    @ApiModelProperty(value = "方案版本id",required = true)
    private Long schemeVersionId;

    @ApiModelProperty(value = "分析项配置id",required = true)
    private Long parameterConfigId;

    @ApiModelProperty("任务id")
    private Long taskId;

    @ApiModelProperty("附件类型")
    @NotNull
    private AttachmentTypeEnum attachmentType;

    @ApiModelProperty("备注信息")
    private String remark;
}
