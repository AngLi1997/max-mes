package com.bmos.mes.service.requisition.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.mcp.dto.WmsStorageInventoryDataQuery;
import com.bmos.mes.service.mcp.vo.WmsStorageInventoryFeignVO;
import com.bmos.mes.service.requisition.dto.InventoryAvailableQuantityQueryDTO;
import com.bmos.mes.service.requisition.dto.InventoryBatchQueryDTO;
import com.bmos.mes.service.requisition.dto.SendSubmitDTO;
import com.bmos.mes.service.requisition.vo.InventoryAvailableQuantityListVO;
import com.bmos.mes.service.requisition.vo.InventoryBatchListVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "bmos-wms-service")
public interface WmsFeignClient {

    @PostMapping("/api/app/wms/inventory/batchList")
    ResponseInfo<List<InventoryBatchListVO>> queryBatchByMaterial(InventoryBatchQueryDTO dto);

    @PostMapping("/api/app/wms/inventory/availableQuantityList")
    ResponseInfo<List<InventoryAvailableQuantityListVO>> queryAvailableQuantityList(InventoryAvailableQuantityQueryDTO dto);

    @PostMapping("/api/app/wms/sendOut/submit")
    ResponseInfo<Void> submitSendOutOrderByBatch(@Validated @RequestBody SendSubmitDTO dto);

    @PostMapping("/api/app/wms/mcp/inventory")
    ResponseInfo<List<WmsStorageInventoryFeignVO>> queryInventoryData(@Validated @RequestBody WmsStorageInventoryDataQuery dto);
}
