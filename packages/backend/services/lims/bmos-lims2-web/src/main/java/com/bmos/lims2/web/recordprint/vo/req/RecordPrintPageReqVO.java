package com.bmos.lims2.web.recordprint.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

/**
 * @Description: 记录打印-检验单分页请求VO
 * @Author: yigaohui
 * @Date: 2025/11/25 10:25
 */
@Getter
@Setter
@ApiModel("记录打印-检验单分页请求VO")
public class RecordPrintPageReqVO {

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("检品分类ID")
    private Long categoryId;

    @ApiModelProperty("请验时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionRequestTimeStart;

    @ApiModelProperty("请验时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionRequestTimeEnd;

    @ApiModelProperty(value = "页码", required = true)
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页大小", required = true)
    @Min(value = 1, message = "每页大小必须大于0")
    private Integer pageSize = 20;
}


