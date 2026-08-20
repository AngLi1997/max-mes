package com.bmos.mes.service.record.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName(value = "bm_batch_record_parse")
public class BatchRecordParse extends BaseDO {

    @ApiModelProperty(value = "页眉")
    private String docxHeader;

    @ApiModelProperty(value = "页脚")
    private String docxFooter;

    @ApiModelProperty(value = "html内容")
    private String fileContent;
}
