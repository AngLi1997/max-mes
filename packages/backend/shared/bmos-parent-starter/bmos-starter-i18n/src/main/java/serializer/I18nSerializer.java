package serializer;

import com.bmos.common.util.i18n.I18nUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class I18nSerializer extends JsonSerializer<String> {

    private static final Logger log = LoggerFactory.getLogger(I18nSerializer.class);

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            // 获取HttpServletRequest
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null){
                // 代表此不是页面请求直接忽略格式化
                jsonGenerator.writeString(s);
                return ;
            }
            HttpServletRequest request = attributes.getRequest();
            jsonGenerator.writeString(I18nUtils.getMenuMessage(s, s, null, request));

        } catch (Exception e){
            log.error("i18n序列化异常 s:{}", s, e);
        }
    }
}
