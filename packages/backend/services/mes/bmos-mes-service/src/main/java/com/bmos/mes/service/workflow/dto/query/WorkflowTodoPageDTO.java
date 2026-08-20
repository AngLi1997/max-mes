package com.bmos.mes.service.workflow.dto.query;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.audit.FlowToDoTypeEnum;
import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.service.plan.info.dto.PlanPageDTO;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class WorkflowTodoPageDTO extends BasePage {

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty(value = "产品Id列表字符串", example = "111,222")
    private String productId;

    @ApiModelProperty(value = "产线id列表字符串", example = "111,222")
    private String lineId;

    @ApiModelProperty("权限码")
    @NotBlank
    private String menuCode;

    @ApiModelProperty("待办类型")
    @NotBlank
    private String todoType;

    public List<Long> getProductIdList() {
        List<String> split = StrUtil.split(productId, StrUtil.COMMA);
        if (StrUtil.isBlank(productId) || CollUtil.isEmpty(split)) {
            return new ArrayList<>();
        }
        return split.stream().map(Long::valueOf).collect(Collectors.toList());
    }

    public List<Long> getLineIdList() {
        List<String> split = StrUtil.split(lineId, StrUtil.COMMA);
        if (StrUtil.isBlank(lineId) || CollUtil.isEmpty(split)) {
            return new ArrayList<>();
        }
        return split.stream().map(Long::valueOf).collect(Collectors.toList());
    }

    public PlanPageDTO convert2PlanPageDTO() {
        PlanPageDTO pageDTO = new PlanPageDTO();
        pageDTO.setProductIds(getProductIdList());
        pageDTO.setBatchNo(getBatchNo());
        pageDTO.setLineIdList(getLineIdList());
        pageDTO.setPageNum(getPageNum());
        pageDTO.setPageSize(getPageSize());
        pageDTO.setIsStart(ProductPlanStartEnum.STARTING.getValue());
        return pageDTO;
    }

}
