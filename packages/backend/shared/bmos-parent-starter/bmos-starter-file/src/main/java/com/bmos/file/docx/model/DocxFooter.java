package com.bmos.file.docx.model;

import com.aspose.words.HeaderFooterType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * 页脚
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@ApiModel("页脚")
public class DocxFooter {

    /**
     * 首页页脚
     */
    @ApiModelProperty(value = "首页页脚")
    private DocxHeaderFooterItem footerFirst;

    /**
     * 奇数页页脚/默认页脚
     */
    @ApiModelProperty(value = "奇数页页脚/默认页脚")
    private DocxHeaderFooterItem footerPrimary;

    /**
     * 偶数页页脚
     */
    @ApiModelProperty(value = "偶数页页脚")
    private DocxHeaderFooterItem footerEven;

    /**
     * 是否链接到上一页
     */
    @ApiModelProperty(value = "是否链接到上一页")
    private Boolean linkToPrevious = false;


    public void fillFooter(String html, int pageCodeHorizontalAlignment, int headerFooterType){
        DocxHeaderFooterItem footer = new DocxHeaderFooterItem();
        footer.setPageCodeHorizontalAlignment(pageCodeHorizontalAlignment);
        footer.setContent(html);
        switch (headerFooterType){
            case HeaderFooterType.FOOTER_FIRST:
                this.footerFirst = footer;
                break;
            case HeaderFooterType.FOOTER_PRIMARY:
                this.footerPrimary = footer;
                break;
            case HeaderFooterType.FOOTER_EVEN:
                this.footerEven = footer;
                break;
            default:
                break;
        }
    }
}
