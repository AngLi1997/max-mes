package com.bmos.mes.service.record.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("bm_batch_record_version")
public class BatchRecordVersion extends BaseDO {

    @ApiModelProperty(value = "记录管理表id")
    private Long recordId;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "文件存放地址")
    private String filePath;

    @ApiModelProperty(value = "流程实例id")
    private String instanceId;

    @ApiModelProperty(value = "记录名称")
    @TableField(exist = false)
    private String recordName;

    @TableField(exist = false)
    private String name;
}
