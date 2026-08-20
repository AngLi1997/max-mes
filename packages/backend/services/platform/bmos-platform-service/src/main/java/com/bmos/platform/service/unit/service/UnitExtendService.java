package com.bmos.platform.service.unit.service;

import com.bmos.platform.service.unit.dto.*;
import com.bmos.platform.service.unit.model.Unit;
import com.bmos.platform.service.unit.model.UnitExtend;
import com.bmos.platform.service.unit.vo.CommonUnitVO;
import com.bmos.platform.service.unit.vo.UnitExtendListVO;
import com.bmos.platform.service.unit.vo.UnitExtendVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author renjinguang
 */
public interface UnitExtendService {


    List<UnitExtendVO> listUnitExtend(UnitListQueryDTO dto);

    List<UnitExtend> queryListByUnitId(Long id);

    Boolean saveUnitExtend(SaveUnitExtendDTO dto);

    UnitExtendVO watchUnitExtend(Long id);

    Boolean deleteUnitExtend(Long id);

    Boolean updateUnitExtend(UpdateUnitExtendDTO dto);

    Boolean updateExtendState(UpdateUnitExtendDTO dto);

    UnitAndExtendDTO getUnitAndExtend(RemoteQueryDTO remoteQueryDTO);

    /**
     * 根据单位id查询单位信息
     *
     * @param dto 查询参数
     * @return
     */
    CommonUnitVO getUnitById(UnitQueryDTO dto);

    /**
     * 根据单位id集合查询单位信息
     *
     * @param list
     * @return
     */
    List<CommonUnitVO> getUnitListByIds(List<UnitQueryDTO> list);

    List<UnitExtend> getCommonUnitByIds(List<Long> ids);

    List<UnitExtendListVO> getExtendUnitListByUnitId(List<Long> unitId);

    /**
     * 根据id查询启用的扩展单位列表
     *
     * @param unitIds
     * @return
     */
    List<UnitExtend> queryEnableExtendsByIds(@Param("unitIds") List<Long> unitIds);

    /**
     * 查询全量扩展单位
     *
     * @return
     */
    List<UnitExtend> listAll();

    List<Unit> selectListByUnitName(List<String> unitName);
}
