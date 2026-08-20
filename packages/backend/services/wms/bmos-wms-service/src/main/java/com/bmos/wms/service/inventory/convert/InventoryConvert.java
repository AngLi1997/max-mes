package com.bmos.wms.service.inventory.convert;

import com.bmos.wms.service.inventory.model.Inventory;
import com.bmos.wms.service.inventory.model.InventoryBatch;
import com.bmos.wms.service.inventory.vo.CargoInventoryBatchDetailVO;
import com.bmos.wms.service.inventory.vo.InventoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/11 15:16
 */
@Mapper
public interface InventoryConvert {

    InventoryConvert INSTANCE = Mappers.getMapper(InventoryConvert.class);

    InventoryVO convertToVO(Inventory inventory);

    List<InventoryVO> convertToVO(List<Inventory> list);

    @Mapping(target = "inventoryBatchNo", source = "batchNo")
    CargoInventoryBatchDetailVO convertToDetailVO(InventoryBatch inventoryBatch);
}
