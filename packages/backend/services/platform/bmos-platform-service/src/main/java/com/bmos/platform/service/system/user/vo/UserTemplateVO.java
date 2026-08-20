package com.bmos.platform.service.system.user.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.bmos.platform.service.system.user.converter.GenderEnumConverter;
import com.bmos.platform.service.system.user.enums.GenderEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.ss.usermodel.Font;

/**
 * @ClassName UserTemplateVO
 * @Description
 * @Author Ren Jin Guang
 * @Date 2024/12/26 9:56
 */
@Setter
@Getter
@ToString
public class UserTemplateVO {

    @ExcelIgnore
    private Long id;

    @ExcelProperty(value = "用户名称(必填)",index = 0)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String userName;

    @ExcelProperty(value = "用户账号(必填)",index = 1)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String loginName;

    @ExcelProperty(value = "性别(男/女)(必填)",index = 2,converter = GenderEnumConverter.class)
    @HeadFontStyle(color = Font.COLOR_RED)
    private GenderEnum genderEnum;

    @ExcelProperty(value = "手机号",index = 3)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String phone;

    @ExcelProperty(value = "用户邮箱",index = 4)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String email;

    @ExcelProperty(value = "备注",index = 5)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String remark;

    @ExcelIgnore
    private Integer gender;
}
