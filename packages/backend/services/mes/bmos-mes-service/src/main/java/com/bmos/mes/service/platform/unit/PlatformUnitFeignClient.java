package com.bmos.mes.service.platform.unit;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.unit.dto.RemoteQueryDTO;
import com.bmos.mes.service.unit.dto.UnitAndExtendDTO;
import com.bmos.mes.service.unit.dto.UnitQueryDTO;
import com.bmos.mes.service.unit.vo.CommonUnitVO;
import com.bmos.mes.service.unit.vo.ExtendUnitPullDownBoxVO;
import com.bmos.mes.service.unit.vo.UnitPullDownBoxVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "bmos-platform-service", contextId = "bmos-adaptor-platform-unit")
public interface PlatformUnitFeignClient {

    @GetMapping("/api/app/platform/unit/list/down/box")
    ResponseInfo<List<UnitPullDownBoxVO>> listStandardDownBox();

    @GetMapping("/api/app/platform/unit/list/extendUnit")
    ResponseInfo<List<ExtendUnitPullDownBoxVO>> listExtendDownBox(@NotNull @RequestParam("unitId") Long unitId);

    @PostMapping("/api/app/platform/unit/list/unitAndExtend")
    ResponseInfo<UnitAndExtendDTO> getUnitAndExtend(@RequestBody RemoteQueryDTO remoteQueryDTO);

    @PostMapping("/api/app/platform/unit/getUnitById")
    ResponseInfo<CommonUnitVO> getUnitById(@RequestBody @Validated UnitQueryDTO dto);

    @PostMapping("/api/app/platform/unit/getUnitListByIds")
    ResponseInfo<List<CommonUnitVO>> getUnitListByIds(@RequestBody @Valid List<UnitQueryDTO> list);

    @PostMapping("/api/app/platform/unit/getCommonUnitByIds")
    ResponseInfo<List<CommonUnitVO>> getCommonUnitByIds(@RequestBody List<Long> ids);

    @GetMapping("/api/app/platform/material/extendUnit/extendUnit/list")
    ResponseInfo<List<ExtendUnitPullDownBoxVO>> getExtendUnitByMaterialId(@RequestParam("materialId") Long materialId);
}
