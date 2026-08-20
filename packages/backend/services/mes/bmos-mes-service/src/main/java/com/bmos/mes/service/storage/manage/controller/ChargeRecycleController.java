package com.bmos.mes.service.storage.manage.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.storage.manage.dto.ChargeStorageMaterialDTO;
import com.bmos.mes.service.storage.manage.dto.ComponentChargeRecycleListQueryDTO;
import com.bmos.mes.service.storage.manage.dto.RecycleStorageMaterialDTO;
import com.bmos.mes.service.storage.manage.service.ChargeRecycleService;
import com.bmos.mes.service.storage.manage.vo.ComponentChargeListVO;
import com.bmos.mes.service.storage.manage.vo.ComponentChargeRecycleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/chargeRecycle")
@Api(tags = "投料/回收")
public class ChargeRecycleController {

    @Resource
    private ChargeRecycleService chargeRecycleService;

    @PostMapping("/charge")
    @ApiOperation("投料")
    @OperationLog
    public ResponseInfo<Void> chargeStorageMaterial(@Validated @RequestBody ChargeStorageMaterialDTO dto) {
        chargeRecycleService.chargeStorageMaterial(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/recycle")
    @ApiOperation("回收")
    @OperationLog
    public ResponseInfo<Void> recycleStorageMaterial(@Validated @RequestBody RecycleStorageMaterialDTO dto) {
        chargeRecycleService.recycleStorageMaterial(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/material/list")
    @ApiOperation("获取组件信息id及已投料及已回收列表")
    public ResponseInfo<ComponentChargeRecycleVO> getComponentChargeRecycleList(@Validated ComponentChargeRecycleListQueryDTO dto) {
        return ResponseInfo.success(chargeRecycleService.getComponentChargeRecycleList(dto));
    }

    @GetMapping("/material/chargeList")
    @ApiOperation("获取该组件已投料的物料列表")
    public ResponseInfo<List<ComponentChargeListVO>> getComponentChargeList(@Validated @ApiParam(name = "chargeRecycleComponentId",
            value = "投料回收主键id", required = true) Long chargeRecycleComponentId) {
        return ResponseInfo.success(chargeRecycleService.getComponentChargeList(chargeRecycleComponentId));
    }


}

