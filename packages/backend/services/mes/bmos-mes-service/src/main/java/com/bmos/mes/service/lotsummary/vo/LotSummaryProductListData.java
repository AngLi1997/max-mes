package com.bmos.mes.service.lotsummary.vo;

import com.bmos.mes.service.dataset.vo.DatasetPointDataPreviewPageVO;
import com.bmos.mes.service.dataset.vo.DatasetPointDataPreviewTitleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批次摘要生产数据
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 10:42
 */
@Data
@ApiModel("批次摘要生产数据(列表)")
@AllArgsConstructor
@NoArgsConstructor
public class LotSummaryProductListData {

    @ApiModelProperty("动态分页标题")
    private List<DatasetPointDataPreviewTitleVO> titles;

    @ApiModelProperty("动态列表数据")
    private List<DatasetPointDataPreviewPageVO> list;
}
