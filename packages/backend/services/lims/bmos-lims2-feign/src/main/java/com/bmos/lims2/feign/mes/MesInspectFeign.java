package com.bmos.lims2.feign.mes;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.feign.mes.dto.MesDocumentConfigFeignVO;
import com.bmos.lims2.feign.mes.dto.MesInitiateInspectFeignDTO;
import com.bmos.lims2.feign.mes.dto.MesRetryInspectFeignDTO;
import com.bmos.lims2.feign.mes.dto.MesSchemeFeignVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/** LIMS 对 MES 暴露的检验对接接口 */
@FeignClient(name = "bmos-lims2-service", contextId = "bmos-lims2-mes-inspect")
public interface MesInspectFeign {

    /** 根据平台物料id查请验单配置（含请验单信息），一个物料可对应多个请验单配置 */
    @GetMapping("/api/app/lims2/mes/inspect/document-config")
    ResponseInfo<List<MesDocumentConfigFeignVO>> queryDocumentConfig(@RequestParam("platformMaterialId") Long platformMaterialId);

    /** 根据平台物料id查检验方案（供前端选择） */
    @GetMapping("/api/app/lims2/mes/inspect/schemes")
    ResponseInfo<List<MesSchemeFeignVO>> querySchemes(@RequestParam("platformMaterialId") Long platformMaterialId);

    /** 接收请验：建单+确认，返回 LIMS 检验单号 */
    @PostMapping("/api/app/lims2/mes/inspect/order")
    ResponseInfo<String> createInspectOrder(@RequestBody MesInitiateInspectFeignDTO dto);

    /** 接收重新发起：作废原单+新建确认，返回新检验单号 */
    @PostMapping("/api/app/lims2/mes/inspect/order/retry")
    ResponseInfo<String> retryInspectOrder(@RequestBody MesRetryInspectFeignDTO dto);
}
