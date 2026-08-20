package com.bmos.platform.service.unit.service;

import com.bmos.platform.service.unit.dto.SaveUnitDTO;
import com.bmos.platform.service.unit.dto.UnitListQueryDTO;
import com.bmos.platform.service.unit.dto.UpdateUnitDTO;
import com.bmos.platform.service.unit.vo.CommonGlobalUnit;
import com.bmos.platform.service.unit.vo.CommonUnitVO;
import com.bmos.platform.service.unit.vo.UnitPullDownBoxVO;
import com.bmos.platform.service.unit.vo.UnitVO;

import java.util.List;

public interface UnitService {


    List<UnitVO> listUnit(UnitListQueryDTO dto);

    Boolean saveUnit(SaveUnitDTO dto);

    UnitVO watchUnit(Long id);

    Boolean deleteUnit(Long id);

    Boolean updateUnit(UpdateUnitDTO dto);

    List<UnitPullDownBoxVO> listDownBox();

    List<CommonUnitVO> getCommonUnitByIds(List<Long> ids);

    /**
     * 查询全量单位信息
     *
     * @return
     */
    CommonGlobalUnit getAllUnit();

    CommonUnitVO getUnitById(Long id);
}
