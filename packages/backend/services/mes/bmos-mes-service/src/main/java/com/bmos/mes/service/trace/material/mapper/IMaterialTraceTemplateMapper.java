package com.bmos.mes.service.trace.material.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.trace.material.dto.MaterialTraceTemplatePageQuery;
import com.bmos.mes.service.trace.material.entity.MaterialTraceTemplateDO;
import com.bmos.mes.service.trace.material.vo.MaterialTraceTemplatePageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/20 10:11
 */
@Mapper
public interface IMaterialTraceTemplateMapper extends BaseMapperX<MaterialTraceTemplateDO> {

    List<MaterialTraceTemplatePageVO> queryPage(@Param("pageQuery") MaterialTraceTemplatePageQuery pageQuery);

    default boolean existName(String templateName){
        if (StringUtils.isBlank(templateName)){
            return false;
        }
        return exists(new LambdaQueryWrapper<MaterialTraceTemplateDO>()
                .eq(MaterialTraceTemplateDO::getTemplateName, templateName)
        );
    }

    default MaterialTraceTemplateDO selectEnabledTemplateByProcessId(Long processId){
        if (processId == null){
            return null;
        }
        return selectOne(new LambdaQueryWrapper<MaterialTraceTemplateDO>()
                .eq(MaterialTraceTemplateDO::getProcessId, processId)
                .eq(MaterialTraceTemplateDO::getEnabled, true)
        );
    }
}
