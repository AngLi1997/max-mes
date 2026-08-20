package com.bmos.lims2.web.inspect.division.vo.req;


import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 分样列表查询请求VO
 * @Author: yigaohui
 * @Date: 2025/01/29 16:45
 */
@Getter
@Setter
@ApiModel("分样列表查询请求")
public class SampleDivisionPageReqVO extends BasePage {

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("请验开始时间")
    private LocalDateTime inspectionRequestTimeStart;

    @ApiModelProperty("请验结束时间")
    private LocalDateTime inspectionRequestTimeEnd;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("物料分类ID（仅传分类时由后端解析启用检品ID集；为空则不按分类过滤）")
    private Long categoryId;

    @ApiModelProperty("物料ID（与分类同时传时以物料ID优先）")
    private Long materialId;

    // 显式getter，避免部分静态分析器对lombok识别异常
    public Long getCategoryId() {
        return categoryId;
    }

    public Long getMaterialId() {
        return materialId;
    }
}
