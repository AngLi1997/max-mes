package com.bmos.lims2.web.eln.signature.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.eln.signature.service.SignatureService;
import com.bmos.lims2.server.eln.signature.dto.UserSignComponentSaveDTO;
import com.bmos.lims2.server.eln.signature.dto.UserSignSaveDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    @ApiOperation("保存手写签名组件（单个）")
    public ResponseInfo<Void> saveComponentSignature(@RequestBody @Valid UserSignComponentSaveDTO dto) {
        signatureService.saveOrUpdateComponentSignature(dto);
        return ResponseInfo.success();
    }

}
