package com.bmos.platform.service.system.user.constant;

public interface UserConstant {

    /**
     * 用户默认密码
     */
    String USER_PASSWORD = "Bmos1018";


    String DEFAULT_ROLE = "1";


    PasswordValidate PASSWORD_VALIDATE = new PasswordValidate();

    String PULL_DOWN_NAME = "性别(男/女)(必填)";

    String IMPORT_NAME = "用户管理";

    String TEMPLATE_NAME = "用户导入模板";

    String REGEX = "^[a-zA-Z0-9]+$";

    String EXPORT_ERROR_FILE_NAME = "用户信息_导入失败错误信息";

    String USER_EXPORT_NAME = "用户信息导出";

}
