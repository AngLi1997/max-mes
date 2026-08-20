package com.bmos.lims2.web.inspect.scheme.vo.request;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 检验方案查询请求VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案查询请求")
public class InspectionSchemeQueryReqVO extends BasePage {

    @ApiModelProperty("方案编码")
    private String code;

    @ApiModelProperty("方案名称")
    private String name;

    @ApiModelProperty("物料分类ID（仅传分类时由后端解析启用物料ID集）")
    private Long categoryId;

    @ApiModelProperty("物料ID")
    private Long materialId;

    @ApiModelProperty("仅返回存在生效版本的方案（默认false）")
    private Boolean onlyActive = false;
} 