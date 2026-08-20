package com.bmos.mes.service.trace.material.mapper;

import com.bmos.mes.service.trace.material.entity.MaterialTraceTemplateProcedureStepDO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/20 10:12
 */
@Mapper
public interface IMaterialTraceTemplateProcedureStepMapper extends BaseMapperX<MaterialTraceTemplateProcedureStepDO> {

    default List<MaterialTraceTemplateProcedureStepDO> selectByTemplateId(Long id){
        if (id == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<MaterialTraceTemplateProcedureStepDO>()
                .eq(MaterialTraceTemplateProcedureStepDO::getTemplateId, id)
        );
    }

    default void deleteByTemplateId(Long id){
        if (id == null){
            return;
        }
        delete(new LambdaQueryWrapperX<MaterialTraceTemplateProcedureStepDO>()
                .eq(MaterialTraceTemplateProcedureStepDO::getTemplateId, id)
        );
    }
}
