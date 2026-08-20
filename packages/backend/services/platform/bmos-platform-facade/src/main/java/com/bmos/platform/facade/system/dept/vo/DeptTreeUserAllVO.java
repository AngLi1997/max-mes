package com.bmos.platform.facade.system.dept.vo;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;


@ApiModel("部门已分配用户封装VO")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeptTreeUserAllVO {
    private List<DeptTreeUserVO> list;
    private Long size;
}
