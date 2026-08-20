package com.bmos.platform.service.equipment.service.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import lombok.Data;
import org.apache.poi.ss.usermodel.Font;

/**
 * @author yigaohui
 * @date 2024/4/25
 **/
@Data
public class AcquisitionPointImportErrorDTO extends AcquisitionPointDTO {
    @ExcelProperty(value = "错误信息", index = 7)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String errorMsg;
}
