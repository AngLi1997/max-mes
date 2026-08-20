package com.bmos.lims2.server.eln.entry.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@TableName("lm_eln_attachment")
public class ExecuteAttachment extends BaseDO {

    private String type;


    private String path;

    @ApiModelProperty("请验单id")
    private Long inspectionOrderId;

    @ApiModelProperty(value = "请验单编号")
    private String inspectionOrderNo;

    /**
     * 批号
     */
    private String batchNo;

    private Long recordId;

    private Long recordVersionId;

    private Long schemeId;

    private Long schemeVersionId;

    private Long parameterConfigId;

    private Long taskId;

    private String fileName;

    /**
     * 附件类型
     */
    private String attachmentType;

    /**
     * 备注信息
     */
    private String remark;
}
