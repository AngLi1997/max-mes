package com.bmos.platform.service.material.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.platform.service.material.dto.MaterialBindExtendUnitDTO;
import com.bmos.platform.service.material.service.MaterialExtendUnitService;
import com.bmos.platform.service.material.vo.MaterialBoundExtendUnitListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/material/extendUnit")
@Api(tags = "物料单位配置")
public class MaterialExtendUnitController {

    @Autowired
    private MaterialExtendUnitService materialExtendUnitService;

    @PostMapping("/extendUnit/bind")
    @ApiOperation("绑定拓展单位")
    @OperationLog
    public ResponseInfo<Void> bindExtendUnit(@RequestBody MaterialBindExtendUnitDTO dto){
        materialExtendUnitService.bindExtendUnit(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/extendUnit/list")
    @ApiOperation("物料绑定的拓展单位列表")
    public ResponseInfo<List<MaterialBoundExtendUnitListVO>> getMaterialBoundExtendUnitList(@NotNull Long materialId){
        return ResponseInfo.success(materialExtendUnitService.getMaterialBoundExtendUnitList(materialId));
    }

}
