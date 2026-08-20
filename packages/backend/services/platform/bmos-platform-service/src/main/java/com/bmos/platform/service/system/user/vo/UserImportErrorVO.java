package com.bmos.platform.service.system.user.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.ss.usermodel.Font;

/**
 * @ClassName UserImportErrorVO
 * @Description
 * @Author Ren Jin Guang
 * @Date 2024/12/26 10:31
 */
@Setter
@Getter
@ToString
public class UserImportErrorVO extends UserTemplateVO{

    @ExcelProperty(value = "错误信息", index = 6)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String errorMsg;
}
