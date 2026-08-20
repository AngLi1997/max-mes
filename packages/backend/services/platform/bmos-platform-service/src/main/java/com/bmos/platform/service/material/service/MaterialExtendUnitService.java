package com.bmos.platform.service.material.service;

import com.bmos.platform.service.material.dto.MaterialBindExtendUnitDTO;
import com.bmos.platform.service.material.vo.MaterialBoundExtendUnitListVO;

import java.util.List;

public interface MaterialExtendUnitService {
    void bindExtendUnit(MaterialBindExtendUnitDTO dto);

    List<MaterialBoundExtendUnitListVO> getMaterialBoundExtendUnitList(Long materialId);

    void deleteBoundRelationByMaterialId(Long materialId);

    void bindExtendUnitBatch(List<MaterialBindExtendUnitDTO> list);
}
