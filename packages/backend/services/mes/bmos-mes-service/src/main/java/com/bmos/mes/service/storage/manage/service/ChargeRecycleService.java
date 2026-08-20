package com.bmos.mes.service.storage.manage.service;

import com.bmos.mes.service.storage.manage.dto.ChargeStorageMaterialDTO;
import com.bmos.mes.service.storage.manage.dto.ComponentChargeRecycleListQueryDTO;
import com.bmos.mes.service.storage.manage.dto.RecycleStorageMaterialDTO;
import com.bmos.mes.service.storage.manage.vo.ComponentChargeListVO;
import com.bmos.mes.service.storage.manage.vo.ComponentChargeRecycleVO;
import com.bmos.mes.service.tag.dto.ScanChargeRecycleDeviceCodeDTO;
import com.bmos.mes.service.tag.dto.ScanMaterialOrDeviceDTO;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;
import com.bmos.mes.service.tag.vo.ScanMaterialOrDeviceVO;

import java.util.List;

public interface ChargeRecycleService {
    ComponentChargeRecycleVO getComponentChargeRecycleList(ComponentChargeRecycleListQueryDTO dto);

    void chargeStorageMaterial(ChargeStorageMaterialDTO dto);

    List<ComponentChargeListVO> getComponentChargeList(Long chargeRecycleComponentId);

    void recycleStorageMaterial(RecycleStorageMaterialDTO dto);

    ScanMaterialOrDeviceVO scanMaterialOrDevice(ScanMaterialOrDeviceDTO dto);

    ScanDeviceVO scanChargeRecycleDeviceCode(ScanChargeRecycleDeviceCodeDTO dto);

    ScanDeviceVO scanChargeRecycleContainer(ScanChargeRecycleDeviceCodeDTO dto);
}
