package com.bmos.mes.service.signature.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.signature.controller.dto.SignatureValidateDTO;
import com.bmos.mes.service.signature.service.SignatureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signature")
@Validated
@Api(tags = "签名相关接口")
public class SignatureController {

    @Autowired
    private SignatureService signatureService;


    @PostMapping("/validate")
    @ApiOperation("校验签名")
    public ResponseInfo<Boolean> validate(@Validated @RequestBody SignatureValidateDTO dto) {
        return ResponseInfo.success(signatureService.validate(dto));
    }

}
