package com.bmos.platform.service.system.user.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.bmos.platform.service.system.user.converter.ActiveConverter;
import com.bmos.platform.service.system.user.converter.GenderConverter;
import com.bmos.platform.service.system.user.converter.StartConverter;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("用户导入VO")
@Getter
@Setter
@ToString
@ExcelIgnoreUnannotated
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@ColumnWidth(13)
@ContentRowHeight(20)
@HeadRowHeight(30)
public class UserImportVO {
    @ExcelProperty(value = "用户名称")
    private String userName;

    @ExcelProperty(value = "账号")
    private String loginName;

    @ExcelProperty(value = "性别",converter = GenderConverter.class)
    private Integer gender;

    @ExcelProperty(value = "手机号")
    private String phone;

    @ExcelProperty(value = "邮箱")
    private String email;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "激活状态",converter = ActiveConverter.class)
    private Integer status;

    @ExcelProperty(value = "启停状态",converter = StartConverter.class)
    private Integer state;
}
