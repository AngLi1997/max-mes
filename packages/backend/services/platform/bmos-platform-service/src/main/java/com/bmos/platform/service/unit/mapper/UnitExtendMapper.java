package com.bmos.platform.service.unit.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.material.vo.MaterialBoundExtendUnitListVO;
import com.bmos.platform.service.unit.dto.SaveUnitExtendDTO;
import com.bmos.platform.service.unit.dto.UnitListQueryDTO;
import com.bmos.platform.service.unit.model.UnitExtend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface UnitExtendMapper extends BaseMapperX<UnitExtend> {


    default List<UnitExtend> listUnitExtend(UnitListQueryDTO dto) {
        return selectList(new LambdaQueryWrapperX<UnitExtend>()
                .eq(UnitExtend::getUnitId, dto.getId())
                .orderByDesc(UnitExtend::getCreateTime));
    }

    default List<UnitExtend> queryListByUnitId(Long id) {
        return selectList(new LambdaQueryWrapperX<UnitExtend>()
                .eq(UnitExtend::getUnitId, id));

    }

    default Boolean saveUnitExtend(UnitExtend extend){
        return Db.saveOrUpdate(extend);
    }

    default UnitExtend watchUnitExtend(Long id){
        return selectOne(new LambdaQueryWrapperX<UnitExtend>()
                .eq(UnitExtend::getId,id));
    }

    void deleteUnitExtend(@Param("id") Long id,@Param("userId") String userId);

    default Boolean updateUnitExtend(UnitExtend dto){
        return Db.saveOrUpdate(dto);
    }

    List<MaterialBoundExtendUnitListVO> selectByUnitId(@Param("unitIdList") List<Long> unitId);


    /**
     * 根据单位id查询启用的拓展单位
     *
     * @param unitIds
     * @return
     */
    default List<UnitExtend> queryEnableExtendUnitByIds(Collection<Long> unitIds) {
        if (CollectionUtil.isEmpty(unitIds)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(UnitExtend.class)
                .eq(UnitExtend::getState, true)
                .in(UnitExtend::getId, unitIds)
        );
    }

    List<String> queryExpressionListByUnitId(@Param("unitId") Long unitId);

    default List<UnitExtend> selectValueByUnitIdAndNameAndExpressionValue(Long unitId,String unitExtendName,String value){
        return selectList(Wrappers.lambdaQuery(UnitExtend.class)
                .eq(UnitExtend::getUnitId, unitId)
                .eq(UnitExtend::getExtendUnitName, unitExtendName)
                .eq(UnitExtend::getExpressionValue,value)
        );
    }
}
