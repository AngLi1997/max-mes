package com.bmos.lims2.web.inspect.retention.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description: 留样观察任务分页查询请求VO
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("留样观察任务分页查询请求")
public class RetentionObservationTaskPageReqVO extends BasePage {

    @ApiModelProperty("物料ID集合")
    private List<Long> materialIds;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("查询类型：upcoming-临期任务（一周内到期），all-全部")
    private String queryType;
}
