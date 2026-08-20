package com.bmos.lims2.web.inspect.entry.vo.resp;

import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: 检项查询-表头+分页数据返回VO
 * @Author: yigaohui
 * @Date: 2025/09/10 14:20
 */
@Data
@ApiModel("检项查询-表头+分页数据")
public class DataPointQueryPageRespVO {

    @ApiModelProperty("分组表头：分析项-数据点分组（用于前端渲染一二级表头）")
    private List<DataPointHeaderRespVO.HeaderGroup> headerGroups;

    @ApiModelProperty("分页数据行")
    private CommonPage<DataPointRowRespVO> page;
}


