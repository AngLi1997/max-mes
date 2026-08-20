package com.bmos.mes.service.process.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@ApiModel("产品工艺树VO")
public class ProductProcessTreeNodeVO implements TreeNode<ProductProcessTreeNodeVO, Long, LocalDateTime> {

    @Tolerate
    public ProductProcessTreeNodeVO(){}

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("父节点id")
    private Long parentId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("子节点集合")
    private List<ProductProcessTreeNodeVO> children;

    @ApiModelProperty("标记是否是产品分类")
    private Boolean productCategoryFlag = false;

    @ApiModelProperty("标记是否是产品")
    private Boolean productFlag = false;

    @ApiModelProperty("标记是否是是工艺")
    private Boolean processFlag = false;

    @Override
    public LocalDateTime sort() {
        return createTime;
    }
}
