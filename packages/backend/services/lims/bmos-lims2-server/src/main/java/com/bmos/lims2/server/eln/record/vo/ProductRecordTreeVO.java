package com.bmos.lims2.server.eln.record.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "产品信息批记录树VO")
public class ProductRecordTreeVO implements TreeNode<ProductRecordTreeVO, Long, LocalDateTime> {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "父级id")
    private Long parentId;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("子集")
    private List<ProductRecordTreeVO> children;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @Override
    public LocalDateTime sort() {
        return createTime;
    }
}
