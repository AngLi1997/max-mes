package com.bmos.platform.service.signature.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.signature.dto.SignatureDTO;
import com.bmos.platform.service.signature.dto.SignatureExportDTO;
import com.bmos.platform.service.signature.dto.SignatureQueryPageDTO;
import com.bmos.platform.service.signature.service.SignatureService;
import com.bmos.platform.service.signature.vo.SignaturePageVO;
import com.bmos.platform.service.signature.vo.SignatureValidateResVO;
import com.bmos.platform.service.system.user.dto.CheckSignaturePasswordConfigDTO;
import com.bmos.platform.service.system.user.dto.UpdateSignaturePasswordDTO;
import com.bmos.platform.service.system.user.vo.CheckSignaturePasswordConfigResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/signature")
@Validated
@Api(tags = "签名相关接口")
public class SignatureController {

    @Autowired
    private SignatureService signatureService;


    @PostMapping("/validate")
    @ApiOperation("校验签名(校验登陆密码)")
    public ResponseInfo<Boolean> validate(@Validated @RequestBody SignatureDTO dto) {
        return ResponseInfo.success(signatureService.validate(dto));
    }

    @PostMapping("/validate/v2")
    @ApiOperation("校验签名V2(校验登陆密码)")
    public ResponseInfo<SignatureValidateResVO> validateV2(@Validated @RequestBody SignatureDTO dto){
        return ResponseInfo.success(signatureService.validateV2(dto));
    }

    @GetMapping("/page")
    @ApiOperation("签名追溯分页")
    public ResponseInfo<CommonPage<SignaturePageVO>> getPage(@Validated SignatureQueryPageDTO dto) {
        return ResponseInfo.success(signatureService.getPage(dto));
    }

    @GetMapping("/export")
    @ApiOperation("签名追溯导出")
    @OperationLog
    public void exportSignatureLog(@Validated SignatureExportDTO dto) throws IOException {
        signatureService.exportSignatureLog(dto);
    }

    @PostMapping("/checkSignatureConfig")
    @ApiOperation("检查当前用户是否配置了签名密码")
    public ResponseInfo<List<CheckSignaturePasswordConfigResultVO>> checkSignatureConfig(@Validated @RequestBody CheckSignaturePasswordConfigDTO dto) {
        return ResponseInfo.success(signatureService.checkSignaturePasswordConfig(dto));
    }

    @PostMapping("/validateSignaturePassword")
    @ApiOperation("校验签名(V3校验签名密码)")
    public ResponseInfo<SignatureValidateResVO> validateSignaturePassword(@Validated @RequestBody SignatureDTO dto) {
        return ResponseInfo.success(signatureService.validateV3(dto));
    }

    @PutMapping("/updateSignaturePassword")
    @ApiOperation("修改签名密码")
    @OperationLog
    public ResponseInfo<Void> updateSignaturePassword(@Validated @RequestBody UpdateSignaturePasswordDTO dto) {
        signatureService.updateSignaturePassword(dto);
        return ResponseInfo.success();
    }
}
