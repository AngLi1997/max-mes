package com.bmos.mes.service.lotrelease.template.vo;

import com.bmos.mes.service.lotrelease.template.enums.LotReleaseTemplateVersionStatus;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批签发模板版本分页vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 14:40
 */
@Data
@ApiModel("批签发模板版本分页vo")
public class LotReleaseTemplateVersionPageVO {

    @ApiModelProperty(value = "批签发模板版本id", example = "1")
    private Long id;

    @ApiModelProperty(value = "批签发模板版本号", example = "V1")
    private String version;

    @ApiModelProperty(value = "批签发模板名称", example = "模板名称")
    private String templateName;

    @ApiModelEnumProperty(value = "批签发模板版本状态", enumClass = LotReleaseTemplateVersionStatus.class)
    private LotReleaseTemplateVersionStatus status;

    @ApiModelProperty(value = "是否默认版本", example = "true")
    private Boolean isDefault;

    @ApiModelProperty(value = "备注", example = "备注")
    private String remark;

    @ApiModelProperty(value = "模板文件地址", example = "http://www.baidu.com")
    private String templateUrl;
}
