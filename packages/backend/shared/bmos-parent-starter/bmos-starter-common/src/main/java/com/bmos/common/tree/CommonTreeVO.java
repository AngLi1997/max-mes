package com.bmos.common.tree;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("通用树VO")
public class CommonTreeVO implements TreeNode<CommonTreeVO,Long, LocalDateTime> {

    private Long id;

    private Long parentId;

    private String name;

    private LocalDateTime createTime;

    private List<CommonTreeVO> children;

    @Override
    public LocalDateTime sort() {
        return createTime;
    }
}
