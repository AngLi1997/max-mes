package com.bmos.platform.facade.system.execute.parameter.constants;

/**
 * 参数配置code
 */
public interface BusinessParameterCodeConstants {

    /**
     * 平台系统授权码
     */
    String PLATFORM_SYS_LICENSE_IS_REQUIRED = "platform.sys.license.isRequired";

    /**
     * 用户密码字符集（多选，分隔符#），大写字符：U，小写字符：L，数字：N，其他字符：E（!,#,$,%,*,(,),[,],{,},,,.,;,:,-,_,?,=,+,-,|）
     */
    String PLATFORM_USER_PWD_RULE_CHARACTER = "platform.user.pwd-rule.character";

    /**
     * 用户密码最小密码长度，限制6-24内整数
     */
    String PLATFORM_USER_PWD_RULE_MIN_LEN = "platform.user.pwd-rule.minLen";

    /**
     * 用户密码尝试次数，限制0-24内整数
     */
    String PLATFORM_USER_PWD_RULE_TRY_NUM = "platform.user.pwd-rule.tryNum";

    /**
     * 用户历史密码个数
     */
    String PLATFORM_USER_PWD_RULE_HIS_NUM = "platform.user.pwd-rule.hisNum";

    /**
     * 用户密码有效期，单位为：天
     */
    String PLATFORM_USER_PWD_RULE_VALIDITY = "platform.user.pwd-rule.validity";
    String APPLICATION = "application";
    String PLATFORM_SYS_TIME_FORMAT = "platform.sys.time-format";
    String PLATFORM_SYS_APP_LOCK_SCREEN_TIME = "platform.sys.app-lock-screen-time";
    String PLATFORM_SYS_WEB_LOCK_SCREEN_TIME = "platform.sys.web-lock-screen-time";
    String PLATFORM_SYS_WEB_LOCK_SCREEN_HOTKEY = "platform.sys.web-lock-screen-hotkey";
    String PLATFORM_SYS_CLIENT_NAME = "platform.sys.client-name";
    String PLATFORM_SYS_LANGUAGE = "platform.sys.language";
    String PLATFORM_SYS_APP_MSG_POLLING_TIME = "platform.sys.app-msg-polling-time";
    String PLATFORM_SYS_WEB_MSG_POLLING_TIME = "platform.sys.web-msg-polling-time";
    String PLATFORM_SYS_PROJECT_CONFIG = "platform.sys.project-config";
    String PLATFORM_SYS_LICENSE_ISREQUIRED = "platform.sys.license.isRequired";
    String MES_RECORD_MARGIN = "mes.record.margin";
    String MES_RECORD_EMPTY_DATA = "mes.record.empty-data";
    String MES_RECORD_ERROR_DATA = "mes.record.error-data";
    String MES_SCHEDULE_PLAN_INVALID = "mes.schedule.plan-invalid";
    String MES_RELEASE_OVER_LEVEL_DATA = "mes.release.over-level-data";
    String MES_PRODUCTION_PLAN_TYPE = "mes.ProductionPlanType";
    String MES_RECORD_FONT_SIZE = "mes.record.font.size";
    String MES_RECORD_DEFAULT_FONT = "mes.record.default.font";
    String MES_WEIGH_REQUIRE_ADVANCE = "mes.weigh.require.advance";

    /**
     * 物料临期提醒默认提前多少日
     */
    String MES_MATERIAL_DYING_PERIOD = "mes.material.dying.period";

    /**
     * 数值趋势分析
     */
    String MES_FIELD_TREND_ANALYSIS = "mes.field.trend.analysis";

    /**
     * 时间组件计算格式化
     */
    String PLATFORM_SYS_TIME_FORMAT_CONFIGURATION = "platform.sys.time-format.configuration";


    /**
     * 系统已经激活的服务
     */
    String PLATFORM_SYS_ACTIVED_SERVICE = "platform.sys.actived.service";

    /**
     * 签名密码复杂度配置
     */
    String PLATFORM_SIGNATURE_PWD_RULE_CHARACTER = "platform.user.sign-pwd-rule.character";

    /**
     * 签名密码最小长度
     */
    String PLATFORM_SIGNATURE_PWD_RULE_MIN_LEN = "platform.user.sign-pwd-rule.minLen";
;
    /**
     * 密码有效期提醒天数
     */
    String PLATFORM_USER_PWD_EXPIRED_REMIND_PERIOD = "platform.user.pwd-expired.remind-period";

    /**
     * 密码锁定用户自动解锁时间，单位：分钟
     */
    String PLATFORM_USER_LOGIN_AUTO_UNLOCK_TIME = "platform.user.login-auto-unlock-time";

    /**
     * 密码锁定用户自动解锁时间永久锁定标识：-1
     */
    int PERMANENT_UNLOCK_PLACEHOLDER = -1;

    /**
     * 水印字体存放路径
     */
    String PLATFORM_SYS_WATERMARK_FONT_PATH = "platform.sys.watermark-font-path";

    /**
     * 签名时间默认格式
     */
    String  PLATFORM_SIGNATURE_TIME_FORMAT = "platform.sys.signature.time-format";

    /**
     * mes批签发归档占位符正则
     */
    String MES_ARCHIVE_PHOTOS_REGULAR = "mes.record.archive-photos-regular";

    /**
     * MES 对接 LIMS 配置（JSON）。
     * 结构：{"enabled":boolean,"type":"THIRD_PARTY"|"BMOS"}
     * enabled：是否对接 LIMS；type：THIRD_PARTY(三方) / BMOS(自研)。
     * 默认 {"enabled":false,"type":"THIRD_PARTY"}（上线即与现状一致）。
     */
    String INSPECT_LIMS_CONFIG = "inspect.lims.config";
}
