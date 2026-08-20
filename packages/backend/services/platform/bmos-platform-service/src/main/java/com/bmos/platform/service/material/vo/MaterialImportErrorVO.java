package com.bmos.platform.service.material.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.ss.usermodel.Font;

/**
 * @ClassName MaterialImportErrorVO
 * @Description TODO
 * @Author Ren Jin Guang
 * @Date 2024/12/24 16:34
 */
@Setter
@Getter
@ToString
public class MaterialImportErrorVO extends MaterialTemplateVO{

    @ExcelProperty(value = "错误信息", index = 7)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String errorMsg;

}
