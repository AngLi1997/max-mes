package com.bmos.platform.service.factory.mapper.param;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineParam {

    /**
     * 产线编码
     */
    private String code;

    /**
     * 产线启停状态
     */
    private Boolean enable;

    /**
     * 产线名称
     */
    private String name;

    /**
     * 产线模型id
     */
    private Long moduleId;

    /**
     * 产线模型id集合
     */
    private List<Long> moduleIdList;

    /**
     * 部门权限
     */
    private List<Long> deptIds;

    /**
     * 是否为移动端请求来源
     */
    private Boolean mobile = false;
    
}
