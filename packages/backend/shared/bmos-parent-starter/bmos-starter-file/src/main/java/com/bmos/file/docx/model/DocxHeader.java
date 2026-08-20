package com.bmos.file.docx.model;

import com.aspose.words.HeaderFooterType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 页眉
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@ApiModel("页眉")
public class DocxHeader {

    /**
     * 首页页眉
     */
    @ApiModelProperty(value = "首页页眉")
    private DocxHeaderFooterItem headerFirst;

    /**
     * 奇数页页眉/默认页眉
     */
    @ApiModelProperty(value = "奇数页页眉/默认页眉")
    private DocxHeaderFooterItem headerPrimary;

    /**
     * 偶数页页眉
     */
    @ApiModelProperty(value = "偶数页页眉")
    private DocxHeaderFooterItem headerEven;

    /**
     * 是否链接到上一页
     */
    @ApiModelProperty(value = "是否链接到上一页")
    private Boolean linkToPrevious = false;

    public void fillHeader(String html, int pageCodeHorizontalAlignment, int headerFooterType){
        DocxHeaderFooterItem header = new DocxHeaderFooterItem();
        header.setPageCodeHorizontalAlignment(pageCodeHorizontalAlignment);
        header.setContent(html);
        switch (headerFooterType){
            case HeaderFooterType.HEADER_FIRST:
                this.headerFirst = header;
                break;
            case HeaderFooterType.HEADER_PRIMARY:
                this.headerPrimary = header;
                break;
            case HeaderFooterType.HEADER_EVEN:
                this.headerEven = header;
                break;
            default:
                break;
        }
    }
}
