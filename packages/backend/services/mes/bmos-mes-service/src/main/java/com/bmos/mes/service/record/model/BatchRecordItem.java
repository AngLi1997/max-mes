package com.bmos.mes.service.record.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName(value = "bm_batch_record_item")
public class BatchRecordItem extends BaseDO {

    @ApiModelProperty(value = "记录项名称")
    private String name;

    @ApiModelProperty(value = "业务id")
    private Long itemId;

    @ApiModelProperty(value = "记录管理版本表id")
    private Long recordVersionId;

    @ApiModelProperty(value = "本版号")
    private String version;

    @ApiModelProperty(value = "记录项内容")
    @TableField(exist = false)
    private String fileContent;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "上传单个记录项指令集地址")
    private String itemPath;

    @ApiModelProperty(value = "文档中最大下标")
    private Integer maxNumber;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "记录项类型")
    private String itemType;

    @ApiModelProperty(value = "文档配置")
    private String pageConfig;

    @ApiModelProperty(value = "文档样式")
    @TableField(exist = false)
    private Boolean style;

    @ApiModelProperty(value = "首页不同")
    private Boolean firstDifferent;

    @ApiModelProperty(value = "奇偶不同")
    private Boolean oddAndEvenDifferent;

    @ApiModelProperty(value = "页码样式")
    private Integer pageNumberStyle;

    @ApiModelProperty(value = "页码起始值")
    private Integer pageStartingNumber;

    @ApiModelProperty(value = "页眉json")
    @TableField(exist = false)
    private String docxHeader;

    @ApiModelProperty(value = "页脚json")
    @TableField(exist = false)
    private String docxFooter;

}
