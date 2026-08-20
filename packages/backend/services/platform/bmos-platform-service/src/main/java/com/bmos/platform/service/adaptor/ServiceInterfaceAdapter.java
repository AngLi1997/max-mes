package com.bmos.platform.service.adaptor;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.constant.RequestConstant;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/14 19:34
 */
@Component
@Slf4j
public class ServiceInterfaceAdapter {

    @Resource
    private RestTemplate restTemplate;

    /**
     * 调用指定服务的指定方法
     *
     * @param serviceName 服务名
     * @param methodName  方法名
     * @param body       参数json
     * @return 返回值json
     */
    public JSONObject invoke(String serviceName, String methodName, Map<String, Object> body) {
        SysUser user = SysUserHolder.getUser();
        if (user == null) {
            throw new RuntimeException("请携带token");
        }
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(RequestConstant.BMOS_TOKEN, user.getToken());
        headers.add("Content-Type", "application/json");
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        String path = "http://" + serviceName + methodName;
        log.info("{}", path);
        try {
            ResponseEntity<ResponseInfo> responseEntity = restTemplate.postForEntity(path, httpEntity, ResponseInfo.class);
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("调用服务失败: " + responseEntity.getStatusCode());
            }
            ResponseInfo responseInfo = responseEntity.getBody();
            if (responseInfo == null) {
                throw new RuntimeException("调用服务失败: 返回值为空");
            }
            return JSONUtil.parseObj(responseInfo.getData());
        } catch (Exception e) {
            log.error("调用服务异常", e);
            throw new RuntimeException("调用服务异常: " + e.getMessage());
        }
    }
}
