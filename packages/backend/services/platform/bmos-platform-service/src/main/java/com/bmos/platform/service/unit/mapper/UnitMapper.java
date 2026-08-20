package com.bmos.platform.service.unit.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.unit.dto.UnitListQueryDTO;
import com.bmos.platform.service.unit.model.Unit;
import com.bmos.platform.service.unit.vo.UnitVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface UnitMapper extends BaseMapperX<Unit> {


    List<UnitVO> listUnit(UnitListQueryDTO dto);

    default Unit selectUnitOne(Long id){
        return selectOne(new LambdaQueryWrapperX<Unit>().eq(Unit::getId,id));
    }

    default Boolean saveUnit(Unit unit){
        return Db.saveOrUpdate(unit);
    }

     void deleteUnit(@Param("id") Long id,@Param("userId") String userId);

    default Boolean updateUnit(Unit dto){
        return Db.saveOrUpdate(dto);
    }

    default List<Unit> selectUnitList(Boolean status){
        return selectList(new LambdaQueryWrapperX<Unit>().eq(Unit::getState,status));
    }

    default List<Unit> queryByUnitName(String unitName){
        return selectList(new LambdaQueryWrapperX<Unit>()
                .eq(Unit::getUnitName,unitName));
    }

    /**
     * 根据单位id查询启用的单位
     * @param unitIds
     * @return
     */
    default List<Unit> queryEnableUnitByIds(Collection<Long> unitIds) {
        if (CollectionUtil.isEmpty(unitIds)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(Unit.class)
                .eq(Unit::getState, true)
                .in(Unit::getId, unitIds)
        );
    }

    default List<Unit> selectListByUnitName(List<String> unitName){
        return selectList(new LambdaQueryWrapperX<Unit>()
                .in(Unit::getUnitName,unitName));
    }
}
