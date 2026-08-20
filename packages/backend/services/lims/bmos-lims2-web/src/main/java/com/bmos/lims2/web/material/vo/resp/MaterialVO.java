package com.bmos.lims2.web.material.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("检品管理列表 分页查询结果返回")
public class MaterialVO {

    @ApiModelProperty("检品当前系统id")
    private Long id;

    @ApiModelProperty("检品名称")
    private String name;

    @ApiModelProperty("检品编码")
    private String code;

    @ApiModelProperty("规格")
    private String specification;

    @ApiModelProperty("启停状态")
    private Boolean status;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("分类级联名称")
    private String categoryName;
    /**
     * 所属物料id
     */
    @ApiModelProperty("所属物料id")
    private Long principalMaterialId;
    /**
     * 单位
     */
    @ApiModelProperty("单位id")
    private Long unitId;
    /**
     * 是否是成员物料
     */
    @ApiModelProperty("是否是成员物料")
    private Boolean subMaterial;

    /**
     * 合并编码:分类合并编码+自身编码
     */
    @ApiModelProperty("合并编码")
    private String mergeCode;
    /**
     * 检品备注
     */
    @ApiModelProperty("检品备注")
    private String remark;
    /**
     * 平台物料关联id
     */
    @ApiModelProperty("平台物料关联id")
    private Long platformMaterialId;
    /**
     * 拓展信息 json字符串
     */
    @ApiModelProperty("拓展信息")
    private String expandInfo;

    @ApiModelProperty("描述")
    private String description;

}
