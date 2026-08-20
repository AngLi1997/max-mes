package com.bmos.lims2.web.inspect.audit.vo;


import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Description: 样品审核分页查询请求
 * @Author: yigaohui
 * @Date: 2025/10/20 15:00
 */
@Getter
@Setter
@ApiModel("样品审核分页查询请求")
public class SampleAuditPageReqVO extends BasePage {

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("请验时间起")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionRequestTimeStart;

    @ApiModelProperty("请验时间止")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionRequestTimeEnd;

    @ApiModelProperty("物料分类ID（仅传分类时由后端解析启用检品ID集；为空则不按分类过滤）")
    private Long categoryId;

    @ApiModelProperty("物料ID（与分类同时传时以物料ID优先）")
    private Long materialId;
}


