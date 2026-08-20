package com.bmos.platform.service.unit.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.material.model.Material;
import com.bmos.platform.service.material.service.MaterialService;
import com.bmos.platform.service.material.vo.MaterialBoundExtendUnitListVO;
import com.bmos.platform.service.unit.constant.UnitConstant;
import com.bmos.platform.service.unit.convert.UnitConvert;
import com.bmos.platform.service.unit.convert.UnitExtendConvert;
import com.bmos.platform.service.unit.dto.*;
import com.bmos.platform.service.unit.mapper.UnitExtendMapper;
import com.bmos.platform.service.unit.mapper.UnitMapper;
import com.bmos.platform.service.unit.model.Unit;
import com.bmos.platform.service.unit.model.UnitExtend;
import com.bmos.platform.service.unit.service.UnitExtendService;
import com.bmos.platform.service.unit.vo.CommonUnitVO;
import com.bmos.platform.service.unit.vo.UnitExtendListVO;
import com.bmos.platform.service.unit.vo.UnitExtendVO;
import com.bmos.platform.service.unit.vo.UnitVO;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UnitExtendServiceImpl implements UnitExtendService {

    @Autowired
    private UnitExtendMapper extendMapper;

    @Autowired
    private UnitMapper unitMapper;

    @Autowired
    @Lazy
    private MaterialService materialService;

    @Resource
    private UnitCache unitCache;

    @Override
    public List<UnitExtendVO> listUnitExtend(UnitListQueryDTO dto) {
        if (ObjectUtil.isNull(dto.getId())) {
            throw new BmosException(BaseResponseCode.ILLEGAL_REQUEST_PARAMETER);
        }
        List<UnitExtendVO> list = UnitExtendConvert.INSTANCE.convertToUnitVoList(extendMapper.listUnitExtend(dto));
        if (CollUtil.isNotEmpty(list)) {
            Unit unit = unitMapper.selectUnitOne(dto.getId());
            list.forEach(unitList -> {
                String expression = UnitConstant.ONE + unitList.getExtendUnitName() + UnitConstant.SYMOL +
                        unitList.getExpressionValue() + unit.getUnitName();
                unitList.setExpression(expression);
            });
        }
        return list;
    }

    @Override
    public List<UnitExtend> queryListByUnitId(Long id) {
        return extendMapper.queryListByUnitId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUnitExtend(SaveUnitExtendDTO dto) {
        List<UnitExtend> extendList = extendMapper.selectValueByUnitIdAndNameAndExpressionValue(dto.getUnitId(), dto.getExtendUnitName(),
                dto.getExpressionValue());
        if (CollUtil.isNotEmpty(extendList)) {
            throw new BmosException(PlatformResponseCode.UNIT_NAME_EXTEND_UNIQUE);
        }
        Boolean result = extendMapper.saveUnitExtend(UnitExtendConvert.INSTANCE.convertToExtendDto(dto));
        unitCache.reload();
        return result;
    }

    @Override
    public UnitExtendVO watchUnitExtend(Long id) {
        UnitExtendVO vo = UnitExtendConvert.INSTANCE.convertToExtendVo(extendMapper.watchUnitExtend(id));
        if (ObjectUtil.isNotEmpty(vo)) {
            Unit unit = unitMapper.selectUnitOne(vo.getUnitId());
            String expression = UnitConstant.ONE + vo.getExtendUnitName() + UnitConstant.SYMOL +
                    vo.getExpressionValue() + unit.getUnitName();
            vo.setExpression(expression);
        }
        return vo;
    }

    @Override
    public Boolean deleteUnitExtend(Long id) {
        List<Material> list = materialService.queryByUnitExtendId(id);
        if (CollUtil.isNotEmpty(list)) {
            throw new BmosException(PlatformResponseCode.MATERIAL_USE_UNIT_EXTEND);
        }
        extendMapper.deleteUnitExtend(id, SysUserHolder.getUser().getUserId());
        unitCache.reload();
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUnitExtend(UpdateUnitExtendDTO dto) {
        List<UnitExtend> extendList = extendMapper.selectValueByUnitIdAndNameAndExpressionValue(dto.getUnitId(),
                dto.getExtendUnitName(), dto.getExpressionValue());
        List<Long> idList = CollectionUtils.convertList(extendList, UnitExtend::getId);
        if (CollUtil.isNotEmpty(extendList) && !idList.contains(dto.getId())) {
            throw new BmosException(PlatformResponseCode.UNIT_NAME_EXTEND_UNIQUE);
        }
        Boolean result = extendMapper.updateUnitExtend(UnitExtendConvert.INSTANCE.convertToUpdateDto(dto));
        unitCache.reload();
        return result;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateExtendState(UpdateUnitExtendDTO dto) {
        Unit unit = unitMapper.selectUnitOne(dto.getUnitId());
        if (dto.getState() && !unit.getState()) {
            throw new BmosException(PlatformResponseCode.UNIT_NOT_STATE);
        }
        Boolean result = extendMapper.updateUnitExtend(UnitExtendConvert.INSTANCE.convertToUpdateDto(dto));
        unitCache.reload();
        return result;
    }

    @Override
    public UnitAndExtendDTO getUnitAndExtend(RemoteQueryDTO remoteQueryDTO) {
        List<Long> unitIds = remoteQueryDTO.getUnitIds();
        List<Long> unitExtendIds = remoteQueryDTO.getUnitExtendIds();
        UnitAndExtendDTO unitAndExtendDTO = new UnitAndExtendDTO();
        if (CollUtil.isNotEmpty(unitIds)) {
            List<Unit> units = unitMapper.selectBatchIds(unitIds);
            List<UnitVO> unitVOS = UnitConvert.INSTANCE.convertToUnitVo(units);
            unitAndExtendDTO.setUnits(unitVOS);
        }
        if (CollUtil.isNotEmpty(unitExtendIds)) {
            List<UnitExtend> unitExtends = extendMapper.selectBatchIds(unitExtendIds);
            List<UnitExtendVO> extendVOS = UnitExtendConvert.INSTANCE.convertToUnitVoList(unitExtends);
            unitAndExtendDTO.setUnitExtends(extendVOS);

        }
        return unitAndExtendDTO;
    }

    @Override
    public CommonUnitVO getUnitById(UnitQueryDTO dto) {
        Boolean isExtend = dto.getIsExtend();
        Unit unit;
        UnitExtend extend = null;
        if (isExtend) {
            extend = extendMapper.selectById(dto.getUnitId());
            if (extend == null) {
                return null;
            }
            unit = unitMapper.selectById(extend.getUnitId());
        } else {
            unit = unitMapper.selectById(dto.getUnitId());
        }
        if (unit == null) {
            return null;
        }
        return CommonUnitVO.builder()
                .unitId(dto.getUnitId())
                .unitName(isExtend ? extend.getExtendUnitName() : unit.getUnitName())
                .rate(isExtend ? extend.getExpressionValue() : "1")
                .rounding(RoundingEnum.getEnumByCode(unit.getRoundCode()))
                .precision(isExtend ? extend.getExtendPrecision() : unit.getUnitPrecision())
                .extend(isExtend)
                .build();
    }

    @Override
    public List<CommonUnitVO> getUnitListByIds(List<UnitQueryDTO> list) {
        List<CommonUnitVO> result = new ArrayList<>();
        Map<Boolean, List<Long>> map = list.stream()
                .collect(Collectors.groupingBy(UnitQueryDTO::getIsExtend, Collectors.mapping(UnitQueryDTO::getUnitId, Collectors.toList())));
        List<Long> extList = map.get(true);
        if (CollectionUtil.isNotEmpty(extList)) {
            List<UnitExtend> unitExtends = extendMapper.selectBatchIds(extList);
            if (CollectionUtil.isNotEmpty(unitExtends)) {
                Map<Long, Unit> unitMap = unitMapper.selectBatchIds(unitExtends.stream()
                        .map(UnitExtend::getUnitId)
                        .collect(Collectors.toList()))
                        .stream()
                        .collect(Collectors.toMap(BaseDO::getId, Function.identity(), (k1, k2) -> k1));
                result.addAll(unitExtends.stream()
                        .map(item -> CommonUnitVO.builder()
                                .unitId(item.getId())
                                .unitName(item.getExtendUnitName())
                                .rate(item.getExpressionValue())
                                // 填充标准单位的精度和舍入规则
                                .rounding(Optional.ofNullable(item.getUnitId())
                                        .map(unitMap::get)
                                        .map(Unit::getRoundCode)
                                        .map(RoundingEnum::getEnumByCode).
                                                orElse(null))
                                .precision(item.getExtendPrecision())
                                .extend(true)
                                .build())
                        .collect(Collectors.toList()));
            }
        }
        List<Long> simpleList = map.get(false);
        if (CollectionUtil.isNotEmpty(simpleList)) {
            List<Unit> units = unitMapper.selectBatchIds(simpleList);
            if (CollectionUtil.isNotEmpty(units)) {
                result.addAll(units.stream()
                        .map(item -> CommonUnitVO.builder()
                                .unitId(item.getId())
                                .unitName(item.getUnitName())
                                .rate("1")
                                // 填充标准单位的精度和舍入规则
                                .rounding(RoundingEnum.getEnumByCode(item.getRoundCode()))
                                .precision(item.getUnitPrecision())
                                .extend(false)
                                .build())
                        .collect(Collectors.toList()));
            }
        }
        return result;
    }

    @Override
    public List<UnitExtend> getCommonUnitByIds(List<Long> ids) {
        return extendMapper.selectBatchIds(ids);
    }

    @Override
    public List<UnitExtendListVO> getExtendUnitListByUnitId(List<Long> unitId) {
        List<MaterialBoundExtendUnitListVO> list = extendMapper.selectByUnitId(unitId);
        List<UnitExtendListVO> res = UnitExtendConvert.INSTANCE.convertToUnitExtendListVO(list);
        res.forEach(vo -> {
            String expression = UnitConstant.ONE + vo.getExtendUnitName() + UnitConstant.SYMOL +
                    vo.getExpressionValue() + vo.getUnitName();
            vo.setExpression(expression);
        });
        return res;
    }

    @Override
    public List<UnitExtend> queryEnableExtendsByIds(List<Long> unitIds) {
        if (CollectionUtil.isEmpty(unitIds)) {
            return new ArrayList<>();
        }
        return extendMapper.queryEnableExtendUnitByIds(unitIds);
    }

    @Override
    public List<UnitExtend> listAll() {
        return extendMapper.selectList();
    }

    @Override
    public List<Unit> selectListByUnitName(List<String> unitName) {
        if (CollUtil.isEmpty(unitName)){
            return new ArrayList<>();
        }
        return unitMapper.selectListByUnitName(unitName);
    }
}
