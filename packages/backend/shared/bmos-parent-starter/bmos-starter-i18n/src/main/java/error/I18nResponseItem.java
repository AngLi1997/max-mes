package error;

import com.bmos.common.response.ResponseItem;

/**
 * 18 ==> i18n相关资源错误码
 */
public interface I18nResponseItem {
    ResponseItem RESOURCE_NO_REGISTER = ResponseItem.from(180001, "i18n resource not register", "i18n");
    /**
     * 国际化初始化加载异常
     */
    ResponseItem RESOURCE_INIT_LOAD_ERROR = ResponseItem.from(180002, "i18n resource init load error", "i18n");

    /**
     * 国际化配置动态刷新失败
     */
    ResponseItem RESOURCE_REFRESH_LOAD_ERROR = ResponseItem.from(180003, "i18n resource refresh load error", "i18n");
}
