package com.bmos.lims2.web.inspect.mes;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.feign.mes.MesInspectFeign;
import com.bmos.lims2.feign.mes.dto.MesDocumentConfigFeignVO;
import com.bmos.lims2.feign.mes.dto.MesInitiateInspectFeignDTO;
import com.bmos.lims2.feign.mes.dto.MesRetryInspectFeignDTO;
import com.bmos.lims2.feign.mes.dto.MesSchemeFeignVO;
import com.bmos.lims2.server.inspect.mes.MesInspectAdapterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * LIMS 对 MES 暴露的检验对接 feign 实现
 */
@RestController
@RequestMapping("/mes/inspect")
@Api(tags = "MES检验对接feign接口")
@Validated
public class MesInspectProviderController implements MesInspectFeign {

    @Autowired
    private MesInspectAdapterService mesInspectAdapterService;

    @Override
    @GetMapping("/document-config")
    public ResponseInfo<List<MesDocumentConfigFeignVO>> queryDocumentConfig(@RequestParam("platformMaterialId") Long platformMaterialId) {
        return ResponseInfo.success(mesInspectAdapterService.queryDocumentConfig(platformMaterialId));
    }

    @Override
    @GetMapping("/schemes")
    public ResponseInfo<List<MesSchemeFeignVO>> querySchemes(@RequestParam("platformMaterialId") Long platformMaterialId) {
        return ResponseInfo.success(mesInspectAdapterService.querySchemes(platformMaterialId));
    }

    @Override
    @PostMapping("/order")
    public ResponseInfo<String> createInspectOrder(@RequestBody MesInitiateInspectFeignDTO dto) {
        return ResponseInfo.success(mesInspectAdapterService.createInspectOrder(dto));
    }

    @Override
    @PostMapping("/order/retry")
    public ResponseInfo<String> retryInspectOrder(@RequestBody MesRetryInspectFeignDTO dto) {
        return ResponseInfo.success(mesInspectAdapterService.retryInspectOrder(dto));
    }
}
