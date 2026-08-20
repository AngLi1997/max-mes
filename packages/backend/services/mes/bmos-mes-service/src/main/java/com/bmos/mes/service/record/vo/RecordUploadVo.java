package com.bmos.mes.service.record.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RecordUploadVo {

    @ApiModelProperty(value = "文件地址")
    private String filePath;

    @ApiModelProperty(value = "记录项集合")
    private List<RecordUploadItemVO> itemVO;

    @ApiModelProperty(value = "记录格式化结果")
    private List<RecordFormatResult> formatResults;

    @ApiModelProperty(value = "带有批注的原始文件")
    private String originFilePath;
}
