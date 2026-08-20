package com.bmos.lims2.web.eln.signature.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.eln.signature.service.DoubleSignatureService;
import com.bmos.lims2.server.eln.signature.dto.SignerQueryDTO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 双签名相关接口
 * @author liang
 * @version 1.0.0
 * @date 2024/5/24 10:16
 */
@RestController
@RequestMapping("/dSignature")
@Validated
@Api(tags = "双签名相关接口")
public class DoubleSignatureController {

    @Resource
    private DoubleSignatureService doubleSignatureService;

    @GetMapping("/getUserListByPermissionCodeAndPlanId")
    @ApiOperation("根据权限码和生产计划id获取签名列表")
    public ResponseInfo<List<FeignUserVO>> getUserListByPermissionCodeAndPlanId(@Validated SignerQueryDTO dto) {
        return ResponseInfo.success(doubleSignatureService.getUserListByPermissionCodeAndPlanId(dto));
    }
}
