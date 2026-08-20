package com.bmos.mes.service.record.business.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("拓展表格信息类")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpandTableInfo {

    @ApiModelProperty("列属性列表")
    private List<ExpandTableColumn> tableList;

    @Data
    public static class ExpandTableColumn {

        @ApiModelProperty("列名")
        private String colName;

        @ApiModelProperty("列自定义字段code")
        private String colData;
    }

}
