package com.bmos.mes.service.plan.document.mapper.param;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemplateInfoParam {

    /**
     * 模板信息名称
     */
    private String name;

    /**
     * 部门id集合
     */
    private List<Long> deptIds;

    /**
     * 分类id集合
     */
    private List<Long> categoryIdList;

}
