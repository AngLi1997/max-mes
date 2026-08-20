package com.bmos.mes.service.workflow.dto;

import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.service.plan.info.dto.PlanPageDTO;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlanProgressDTO extends BasePage {

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("产品Id")
    private Long productId;

    @ApiModelProperty("产品分类id")
    private Long productCategoryId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;


    public PlanPageDTO convert2PlanPageDTO() {
        PlanPageDTO pageDTO = new PlanPageDTO();
        pageDTO.setProductId(getProductId());
        pageDTO.setBatchNo(getBatchNo());
        pageDTO.setPageNum(getPageNum());
        pageDTO.setPageSize(getPageSize());
        pageDTO.setOrderBy(getOrderBy());
        pageDTO.setProductName(getProductName());
        pageDTO.setProductCategoryId(getProductCategoryId());
        pageDTO.setStartTime(getStartTime());
        pageDTO.setEndTime(getEndTime());
        pageDTO.setIsStart(ProductPlanStartEnum.STARTING.getValue());
        return pageDTO;
    }

}
