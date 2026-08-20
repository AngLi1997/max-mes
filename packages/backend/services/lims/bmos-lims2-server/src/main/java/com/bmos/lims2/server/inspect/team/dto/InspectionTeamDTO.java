package com.bmos.lims2.server.inspect.team.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 检验班组
 */
@Getter
@Setter
@TableName("lm_inspection_team")
public class InspectionTeamDTO extends BaseDO {

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    /**
     * 启停状态
     */
    private Boolean status;

    /**
     * 班组人数
     */
    private Integer number;

}
