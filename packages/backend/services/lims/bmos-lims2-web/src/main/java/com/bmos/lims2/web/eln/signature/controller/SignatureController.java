package com.bmos.lims2.web.eln.signature.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.eln.signature.service.SignatureService;
import com.bmos.lims2.server.eln.signature.dto.SignatureValidateDTO;
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
