package com.bmos.mes.service.signature.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.platform.user.vo.PlatformUserVO;
import com.bmos.mes.service.signature.controller.dto.SignerQuery;
import com.bmos.mes.service.signature.controller.dto.SignerQueryDTO;
import com.bmos.mes.service.signature.controller.dto.SignerQueryWithStationIdsDTO;
import com.bmos.mes.service.signature.service.DoubleSignatureService;
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

    @PostMapping("/getSignerListWithPermissionCodeAndComponent")
    @ApiOperation("根据权限码和组件获取校验签名列表")
    public ResponseInfo<List<PlatformUserVO>> getSingerListWithPermissionCodeAndComponent(@Validated @RequestBody SignerQuery query) {
        return ResponseInfo.success(doubleSignatureService.getSingerListWithPermissionCodeAndComponent(query));
    }

    @PostMapping("/getSingerListWithPermissionCodeAndStationIds")
    @ApiOperation("根据权限码和工位列表获取校验签名列表")
    public ResponseInfo<List<PlatformUserVO>> getSingerListWithPermissionCodeAndStationIds(@Validated @RequestBody SignerQueryWithStationIdsDTO query) {
        return ResponseInfo.success(doubleSignatureService.getSingerListWithPermissionCodeAndStationIds(query));
    }

    @GetMapping("/getUserListByPermissionCodeAndPlanId")
    @ApiOperation("根据权限码和生产计划id获取签名列表")
    public ResponseInfo<List<FeignUserVO>> getUserListByPermissionCodeAndPlanId(@Validated SignerQueryDTO dto) {
        return ResponseInfo.success(doubleSignatureService.getUserListByPermissionCodeAndPlanId(dto));
    }
}
