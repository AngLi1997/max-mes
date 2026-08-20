package com.bmos.file.docx.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 拓展表格表头
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpandTableInfo {

    @ApiModelProperty("默认行数")
    private int rowNum;

    @ApiModelProperty("列属性列表")
    private List<ExpandTableColumn> tableList;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExpandTableColumn {

        @ApiModelProperty("列名")
        private String colName;

        @ApiModelProperty("列自定义字段code")
        private String colData;
    }

}
