package com.bmos.platform.facade.system.dept.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@ApiModel("部门用户树节点VO")
@Getter
@Setter
@ToString
public class DeptUserTreeVO implements TreeNode<DeptUserTreeVO,String,LocalDateTime> {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("部门或人名称")
    private String name;

    @ApiModelProperty("用户账号")
    private String loginName;

    @ApiModelProperty("父级id")
    private String parentId;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("父级部门名称")
    private String parentName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否是部门")
    private Boolean deptFlag;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("子数据")
    private List<DeptUserTreeVO> children;

    @Override
    public LocalDateTime sort() {
        return createTime;
    }
}
