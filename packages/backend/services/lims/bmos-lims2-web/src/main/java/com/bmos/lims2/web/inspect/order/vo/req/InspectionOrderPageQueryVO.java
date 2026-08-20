package com.bmos.lims2.web.inspect.order.vo.req;

import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.EqualsAndHashCode;
import java.util.List;
import java.time.LocalDateTime;

/**
 * 检验单分页查询请求VO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("检验单分页查询请求")
public class InspectionOrderPageQueryVO extends BasePage {

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

	@ApiModelProperty("物料分类ID（仅传分类时由后端解析启用物料ID集；为空则不按分类过滤）")
	private Long categoryId;

	@ApiModelProperty("物料ID（与分类同时传时以物料ID优先）")
	private Long materialId;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("单据状态（请验阶段），支持多选")
    private List<InspectionOrderStatusEnum> orderStatus;

    @ApiModelProperty("创建时间开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionRequestTimeStart;

    @ApiModelProperty("创建时间结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionRequestTimeEnd;

    @ApiModelProperty("请验人名称（模糊查询）")
    private String requestUserName;
}