package com.bmos.common.exception;

import com.bmos.common.response.ResponseItem;

public interface BaseResponseCode {

    ResponseItem SUCCESS = ResponseItem.from(0, "success", "通用模块");
    ResponseItem UN_ACTIVE = ResponseItem.from(301, "未激活", "通用模块");
    ResponseItem ACTIVE_TIME_DISCARD = ResponseItem.from(302, "激活时间已到期", "通用模块");


    ResponseItem UN_AUTHORIZATION = ResponseItem.from(401, "未授权", "通用模块");

    ResponseItem SERVER_EXCEPTION = ResponseItem.from(500, "服务器异常", "通用模块");

    ResponseItem REGISTER_EXCEPTION = ResponseItem.from(501, "服务注册异常", "通用模块");

    ResponseItem ILLEGAL_REQUEST_PARAMETER = ResponseItem.from(507, "参数异常,", "通用模块");

    ResponseItem DUPLICATE_KEY_ERROR = ResponseItem.from(508,"数据重复","通用模块");

    ResponseItem FEIGN_RESPONSE_READ_ERROR = ResponseItem.from(509,"feign响应读取错误,","通用模块");

    ResponseItem FEIGN_REMOTE_CALL_ERROR = ResponseItem.from(510, "feign调用错误,", "通用模块");

    ResponseItem TRY_AGAIN_LATER = ResponseItem.from(511, "稍后再试,", "通用模块");

    ResponseItem EXCEL_TEMPLATE_ERROR = ResponseItem.from(512, "EXCEL导入模板错误，请检查导入模板", "通用模块");

    ResponseItem REPEAT_REQUEST = ResponseItem.from(513, "服务繁忙，请稍后操作", "通用模块");

    ResponseItem INVALID_NUMBER_OF_ARGUMENTS = ResponseItem.from(600, "函数参数数量异常", "通用模块");

    ResponseItem VAR_NOT_BEEN_SET = ResponseItem.from(601, "参数未指定值", "通用模块");

    ResponseItem MISMATCHED_PARENTHESES = ResponseItem.from(602, "括号不匹配或错误的分隔符", "通用模块");

    ResponseItem UNABLE_PARSE_CHAR = ResponseItem.from(603, "符号无法解析", "通用模块");

    ResponseItem DIVISION_BY_ZERO = ResponseItem.from(604, "0不可作为除数", "通用模块");

    ResponseItem ZERO_ARG_FUN = ResponseItem.from(605, "0不可作为函数参数", "通用模块");

    ResponseItem EMPTY_EXPRESSION = ResponseItem.from(606,"表达式不可为空","通用模块");

    ResponseItem PARSE_EXCEPTION = ResponseItem.from(607, "公式解析错误，请检查公式表达式配置", "通用模块");

    ResponseItem EVALUATE_WRONG_PARAM_EXCEPTION = ResponseItem.from(608, "参数不能以非数字为值", "通用模块");

    ResponseItem SAVE_USER_ERROR = ResponseItem.from(10_01, "用户保存失败", "通用模块");
    ResponseItem UPDATE_USER_ERROR = ResponseItem.from(10_02, "用户编辑失败", "通用模块");
    ResponseItem DELETE_USER_ERROR = ResponseItem.from(10_03, "用户删除失败", "通用模块");
    ResponseItem CHANG_PWD_ERROR = ResponseItem.from(10_04, "修改密码失败", "通用模块");
    ResponseItem QUERY_ROLE_ERROR = ResponseItem.from(10_05, "查询角色错误", "通用模块");
    ResponseItem QUERY_DEPT_ERROR = ResponseItem.from(10_06, "查询部门错误", "通用模块");
    ResponseItem LOGIN_ISC_ERROR = ResponseItem.from(10_07, "登录指令集错误", "通用模块");
    ResponseItem LOGOUT_ISC_ERROR = ResponseItem.from(10_08, "登出指令集错误", "通用模块");
    ResponseItem QUERY_USER_ERROR = ResponseItem.from(10_09, "查询用户错误", "通用模块");



    ResponseItem DOCX_CONVERT_ERROR = ResponseItem.from(11_01, "DOCX文件转换异常", "通用模块");

    ResponseItem UNIT_NOTFOUND = ResponseItem.from(81_03_007, "单位不存在", "通用模块");

    ResponseItem EXPORT_TEMPLATE_HEADER_ERROR = ResponseItem.from(81_00_0001, "与模板表头不匹配，请使用正确的模板导入", "bmosPlatform");
    ResponseItem EXPORT_TEMPLATE_NOT_MATCH = ResponseItem.from(81_00_0002, "请使用对应的模板导入", "bmosPlatform");
    ResponseItem EXPORT_TEMPLATE_ERROR = ResponseItem.from(81_00_0003, "模板错误，请检查导入模板", "bmosPlatform");

}
