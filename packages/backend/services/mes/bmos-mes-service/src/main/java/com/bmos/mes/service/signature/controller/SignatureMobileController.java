package com.bmos.mes.service.signature.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.signature.controller.dto.UserSignComponentSaveDTO;
import com.bmos.mes.service.signature.controller.dto.UserSignSaveDTO;
import com.bmos.mes.service.signature.service.SignatureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mobile/signature")
@Validated
@Api(tags = "手写签名相关接口")
public class SignatureMobileController {

    @Autowired
    private SignatureService signatureService;

    @PostMapping("/save")
    @ApiOperation("保存签名url")
    public ResponseInfo<String> saveUserSignature(@RequestBody UserSignSaveDTO dto) {
        return ResponseInfo.success(signatureService.save(dto));
    }

    @GetMapping("/info")
    @ApiOperation("根据userId获取签名的url")
    public ResponseInfo<String> getUserSignature(@RequestParam("userId") String userId) {
        return ResponseInfo.success(signatureService.getUserSignature(userId));
    }

    @PostMapping("/component/save")
    @ApiOperation("保存手写签名组件")
    public ResponseInfo<Void> saveComponentSignature(@RequestBody UserSignComponentSaveDTO dto) {
        signatureService.saveOrUpdateComponentSignature(dto);
        return ResponseInfo.success();
    }

}
