package com.bmos.mes.service.log.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.bmos.common.convert.ExcelEnumConvert;
import com.bmos.common.convert.I18nTranslateConvert;
import com.bmos.logging.enums.OperationTypeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("操作日志VO")
public class OperationLogExcelVO {

    /**
     * 操作类型代码
     */
    @ExcelProperty(value = "操作类型",converter = ExcelEnumConvert.class)
    private OperationTypeEnum operationType;

    @ExcelIgnore
    private String operationTypeString;

    /**
     * 操作状态
     */
    @ExcelProperty(value = "操作业务", converter = I18nTranslateConvert.class)
    private String operationBusiness;

    /**
     * 操作用户
     */
    @ExcelProperty("操作人")
    private String userName;

    /**
     * 操作对象
     */
//    @ExcelProperty("操作对象")
    @ExcelIgnore
    private String operationObject;

    /**
     * 操作时间
     */
    @ExcelProperty("操作时间")
    private LocalDateTime createTime;

    /**
     * 操作备注
     */
    @ExcelProperty("备注")
    private String remark;

}
