package com.bmos.mes.service.process.vo;

import com.bmos.common.tree.TreeNode;
import com.bmos.mes.service.product.vo.ProductCategoryTreeNodeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName ProcessListItemTreeVO
 * @Description 工艺配置关联工工艺返回列表
 * @Author Ren Jin Guang
 * @Date 2024/8/6 13:49
 */
@Setter
@Getter
@ToString
@ApiModel("关联工艺返回列表")
public class ProcessListItemTreeVO implements TreeNode<ProcessListItemTreeVO,Long, String> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("分类或产品名称")
    private String name;

    @ApiModelProperty("是否是分类")
    private Boolean categoryFlag;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty("展示名称")
    private String showName;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("产品标识")
    private String productMark;

    @ApiModelProperty("版本号")
    private String activeVersion;

    @ApiModelProperty("是否是工艺")
    private Boolean isFlag;

    @ApiModelProperty("子集")
    private List<ProcessListItemTreeVO> children;

    @ApiModelProperty("工艺信息")
    private List<ProcessListItemVO> processList;

    @Override
    public String sort() {
        return this.mergeCode;
    }
}
