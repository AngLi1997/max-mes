package com.bmos.mes.service.process.mapper;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.process.model.ProcedureModelMaterial;
import com.bmos.mes.service.process.vo.ProcedureModelMaterialVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ProcedureModelMaterialMapper extends BaseMapperX<ProcedureModelMaterial> {
    default List<ProcedureModelMaterial> selectByProcedureModelIds(Collection<Long> procedureModelIds){
        return selectList(new LambdaQueryWrapperX<ProcedureModelMaterial>()
                .in(ProcedureModelMaterial::getProcedureModelId, procedureModelIds));
    }

    default void deleteByProcedureModelIds(Collection<Long> ids){
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        delete(new LambdaQueryWrapperX<ProcedureModelMaterial>().in(ProcedureModelMaterial::getProcedureModelId, ids));
    }

    List<ProcedureModelMaterial> selectByProcessVersionId(@Param("processVersionId") Long id);

    List<ProcedureModelMaterialVO> getMaterialListByProcedureModelIds(@Param("procedureModelIdList") List<Long> procedureModelIdList);
}
