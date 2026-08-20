package com.bmos.mes.service.unit.service;

import com.bmos.mes.service.unit.vo.ExtendUnitPullDownBoxVO;

import java.util.List;

public interface UnitService {

    List<ExtendUnitPullDownBoxVO> listByMaterialId(Long materialId);
}
