package com.bmos.mes.service.dataset.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * 数据点预览dto(批记录数据)
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:54
 */
@Data
@ApiModel("数据点预览dto(批记录数据)")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DatasetPointDataPreviewPageQuery extends BasePage {

    @ApiModelProperty(value = "工艺id", example = "1")
    private Long processId;

    private String startDate;

    private String endDate;

    private List<String> batchNos;

    @ApiModelProperty(value = "数据点列表")
    private List<Point> points;

    @ApiModelProperty(value = "是否包含复用的数据", example = "true", hidden = true)
    private Boolean hasReuse = false;

    public void setPoints(List<Point> points) {
        this.points = points;
        if (CollectionUtil.isEmpty(points)) {
            return;
        }
        points.stream()
                .filter(p -> p.getProcedureStepId() == 0L)
                .findAny()
                .ifPresent(p -> hasReuse = true);
    }

    @Data
    @ApiModel("数据点预览dto数据点")
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Point {

        @ApiModelProperty(value = "工步id", example = "1")
        private Long procedureStepId;

        @ApiModelProperty(value = "字段id", example = "1")
        private Long fieldId;
    }
}
