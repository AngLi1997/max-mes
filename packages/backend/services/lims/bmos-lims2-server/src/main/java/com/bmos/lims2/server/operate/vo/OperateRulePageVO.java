package com.bmos.lims2.server.operate.vo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "操作规程返回vo")
public class OperateRulePageVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件编号")
    private String code;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("生效版本")
    private String version;

    public String getVersion() {
        return StrUtil.isBlank(version) ? "-" : version;
    }

    @ApiModelProperty("已绑定的方法记录ID集合")
    private List<Long> recordIdList;


}
