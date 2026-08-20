package com.bmos.platform.service.system.i18n;

import com.bmos.common.response.ResponseInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import property.I18nJson;
import resource.MessageResourceRegister;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "管理后台")
@RestController
@RequestMapping("/i18n")
public class I18nController {

    @Autowired
    MessageResourceRegister messageResourceRegister;

    @GetMapping("/config")
    @ApiOperation("获取前端国际化配置")
    public ResponseInfo<I18nJson> page(HttpServletRequest request) {
        return ResponseInfo.success(messageResourceRegister.getAllProperties(request));
    }

}
