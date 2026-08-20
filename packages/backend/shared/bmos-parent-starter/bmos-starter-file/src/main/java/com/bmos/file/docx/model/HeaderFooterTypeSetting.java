package com.bmos.file.docx.model;

import com.aspose.words.HeaderFooter;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.HorizontalAlignment;
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
public class HeaderFooterTypeSetting {

    /**
     * 页眉页脚
     */
    private HeaderFooter headerFooter;

    /**
     * 链接到上一节
     */
    private Boolean linkedToPrevious;

    /**
     * {@link HorizontalAlignment}
     * 页码对齐位置
     */
    private Integer pageCodeHorizontalAlignment;
}
