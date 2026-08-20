package com.bmos.platform.service.signature.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.bmos.common.convert.ExcelEnumConvert;
import com.bmos.platform.common.enums.signature.SignatureActionEnum;
import com.bmos.platform.common.enums.signature.SignatureTypeEnum;
import com.bmos.platform.service.signature.converter.SignatureActionConverter;
import com.bmos.platform.service.signature.converter.SignatureSuccessConverter;
import com.bmos.platform.service.signature.converter.SignatureTypeConverter;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("签名追溯excel导出VO")
public class SignatureExcelVO {

    @ExcelProperty("系统名称")
    private String systemName;

    @ExcelProperty(value = "签名类型",converter = ExcelEnumConvert.class)
    private SignatureTypeEnum signatureType;

    @ExcelProperty(value = "签名动作",converter = ExcelEnumConvert.class)
    private SignatureActionEnum signatureAction;

    @ExcelProperty("用户名称")
    private String userName;

    @ExcelProperty("用户账号")
    private String loginName;

    @ExcelProperty("签名时间")
    private LocalDateTime createTime;

    @ExcelProperty(value = "状态",converter = SignatureSuccessConverter.class)
    private Boolean success;

    @ExcelProperty("备注")
    private String remark;
}
