package com.bmos.mes.service.plan.team.vo;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
* 生产计划班组
*/
@Getter
@Setter
@ApiModel("ProductPlanTeamDetailVO:生产计划班组详情查询VO")
public class ProductPlanTeamDetailVO {
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
    private List<String> people;

    @ApiModelProperty("班组人数")
    private Integer peopleNum;

    @ApiModelProperty("产线id列表")
    private List<Long> productionLineIds;

    @ApiModelProperty("产线列表")
    private List<FactoryLineFeignVO> productionLines;

    public Integer getPeopleNum() {
        return CollUtil.isEmpty(getPeople()) ? 0 : getPeople().size();
    }

    @ApiModelProperty("班组人员")
    private List<ProductPlanTeamDetailItemVO> peoples;

    public List<ProductPlanTeamDetailItemVO> getPeoples() {
        return people.stream()
            .map(userId -> new ProductPlanTeamDetailItemVO(UserUtils.getUser(userId)))
            .collect(Collectors.toList());
    }

    @ApiModelProperty("状态 TRUE 启用 FALSE 禁用")
    private BooleanEnum status;

    @ApiModelProperty("状态 TRUE 启用 FALSE 禁用")
    private LocalDateTime createTime;
}
