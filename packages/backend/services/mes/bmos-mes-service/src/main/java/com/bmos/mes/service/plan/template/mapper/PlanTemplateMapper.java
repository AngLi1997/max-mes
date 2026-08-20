package com.bmos.mes.service.plan.template.mapper;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.plan.template.dto.PlanTemplatePageQueryDTO;
import com.bmos.mes.service.plan.template.model.PlanTemplate;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlanTemplateMapper extends BaseMapperX<PlanTemplate> {

    default List<PlanTemplate> queryPage(PlanTemplatePageQueryDTO dto){
        return selectList(new LambdaQueryWrapperX<PlanTemplate>()
                .like(StrUtil.isNotEmpty(dto.getName()), PlanTemplate::getName, dto.getName())
                .eq(dto.getConfirmed() != null, PlanTemplate::getConfirmed, dto.getConfirmed())
                .orderByAsc(PlanTemplate::getConfirmed)
                .orderByDesc(PlanTemplate::getOperationTime));
    }

    default PlanTemplate selectByName(String name){
        return selectOne(new LambdaQueryWrapperX<PlanTemplate>()
                .eq(PlanTemplate::getName, name));
    }

    default List<PlanTemplate> selectEnableTemplateList(){
        return selectList(new LambdaQueryWrapperX<PlanTemplate>()
                .eq(PlanTemplate::getState, true));
    }

    void batchConfirmTemplate(@Param("ids") List<Long> needUpdateTemplate);

    void batchCancelConfirmTemplate(@Param("ids") List<Long> needUpdateTemplate);
}
