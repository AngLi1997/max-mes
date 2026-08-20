package com.bmos.platform.service.system.code.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.code.dto.BatchConfirmByCodeDTO;
import com.bmos.platform.facade.code.dto.BatchCodeNoDTO;
import com.bmos.platform.facade.code.dto.ReleaseConfirmedNoDTO;
import com.bmos.platform.facade.code.feign.CodeRuleFeign;
import com.bmos.platform.facade.code.vo.BatchCodeNoVO;
import com.bmos.platform.service.system.code.service.CodeRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/codeRule/feign")
@Api(tags = "编码规则Feign")
@RestController
public class CodeRuleFeignController implements CodeRuleFeign {

    @Autowired
    private CodeRuleService codeRuleService;


    @Override
    @PostMapping("/batchConfirmNoByIdList")
    @ApiOperation("根据id批量确认编号")
    public ResponseInfo<Void> batchConfirmByIdList(List<Long> list) {
        codeRuleService.batchConfirmByIdList(list);
        return ResponseInfo.success();
    }

    @Override
    @PostMapping("/batchConfirmNoByCodeList")
    public ResponseInfo<Void> batchConfirmByCodeList(@RequestBody @Validated BatchConfirmByCodeDTO dto) {
        codeRuleService.batchConfirmByCodeList(dto);
        return ResponseInfo.success();
    }

    @Override
    @PostMapping("/releaseNo")
    @ApiOperation("释放已确认的编号")
    public ResponseInfo<Void> releaseConfirmedNO(@RequestBody ReleaseConfirmedNoDTO dto) {
        codeRuleService.releaseConfirmedNO(dto);
        return ResponseInfo.success();
    }

    @Override
    @PostMapping("/sameTypeBatchNo")
    @ApiOperation("批量获取同类型编号")
    public ResponseInfo<BatchCodeNoVO> batchGetSameTypeNo(@RequestBody @Validated BatchCodeNoDTO dto) {
        return ResponseInfo.success(codeRuleService.batchGetSameTypeNo(dto));
    }
}
