package com.bmos.mes.service.plan.team.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;
import java.util.List;

/**
* 生产计划班组
*/
@Getter
@Setter
@ApiModel("ProductPlanPageTeamVO:生产计划班组分页查询VO")
public class ProductPlanPageTeamVO {
    @Tolerate
    public ProductPlanPageTeamVO() {}
    @ApiModelProperty("班组名称")
    private Long id;

    @ApiModelProperty("班组名称")
    private String name;

    @ApiModelProperty("班组编码")
    private String code;

    @ApiModelProperty("班组描述")
    private String description;

    @JsonIgnore
    @ApiModelProperty("班组人员 json数据")
    private String people;

    @ApiModelProperty("班组人员")
    private List<String> peoples;

    public List<String> getPeoples() {
        return JsonUtils.parseArray(people, String.class);
    }

    @ApiModelProperty("班组人数")
    private Integer peopleNum;

    public Integer getPeopleNum() {
        return CollUtil.isEmpty(getPeoples()) ? 0 : getPeoples().size();
    }

    @ApiModelProperty("状态 TRUE 启用 FALSE 禁用")
    private BooleanEnum status;

    @ApiModelProperty("状态 TRUE 启用 FALSE 禁用")
    private LocalDateTime createTime;
}
