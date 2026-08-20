package com.bmos.platform.service.system.user.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.bmos.platform.service.system.user.converter.ActiveEnumConverter;
import com.bmos.platform.service.system.user.converter.GenderEnumConverter;
import com.bmos.platform.service.system.user.enums.ActiveEnum;
import com.bmos.platform.service.system.user.enums.GenderEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName UserTemplateVO
 * @Description
 * @Author Ren Jin Guang
 * @Date 2024/12/26 9:56
 */
@Setter
@Getter
@ToString
public class UserExportVO {

    @ExcelIgnore
    private Long id;

    @ExcelProperty(value = "用户名称",index = 0)
    private String userName;

    @ExcelProperty(value = "用户账号",index = 1)
    private String loginName;

    @ExcelProperty(value = "性别",index = 2, converter = GenderEnumConverter.class)
    private GenderEnum genderEnum;

    @ExcelProperty(value = "手机号",index = 3)
    private String phone;

    @ExcelProperty(value = "邮箱",index = 4)
    private String email;

    @ExcelProperty(value = "备注",index = 5)
    private String remark;

    @ExcelProperty(value = "状态",index = 6,converter = ActiveEnumConverter.class)
    private ActiveEnum statusEnum;


}
