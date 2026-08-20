package com.bmos.platform.facade.system.dept.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@ApiModel("部门树全量VO")
@Getter
@Setter
@ToString
public class DeptTreeVO implements TreeNode<DeptTreeVO,Long,LocalDateTime> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("父级编码")
    private String parentCode;

    @ApiModelProperty("父级部门名称")
    private String parentName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("子数据")
    private List<DeptTreeVO> children;

    @ApiModelProperty("部门下是否存在用户")
    private Boolean existUser;

    @Override
    public LocalDateTime sort() {
        return createTime;
    }
}
