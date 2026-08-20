package config;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.constant.RequestConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

/**
 * @author yigaohui
 * @date 2024/4/26
 **/
public class BmosLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader(RequestConstant.LANGUAGE);
        if (StrUtil.isNotBlank(lang)) {
            List<String> chars = StrUtil.split(lang, StrUtil.C_UNDERLINE, 2);
            return new Locale(chars.get(0), chars.get(1));
        }
        return Locale.getDefault();
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
