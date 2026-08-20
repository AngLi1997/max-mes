package com.bmos.platform.service.system.code.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionPageDTO;
import com.bmos.platform.service.system.code.vo.CodeRuleVersionPageVO;
import com.bmos.platform.service.system.code.vo.DetailCodeRuleVersionDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codeRuleVersion")
@Api(tags = "编码规则版本详情")
public class CodeRuleVersionDetailController {

}
