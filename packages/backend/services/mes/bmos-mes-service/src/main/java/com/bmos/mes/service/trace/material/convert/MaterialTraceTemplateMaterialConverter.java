package com.bmos.mes.service.trace.material.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 18:32
 */
@Mapper
public interface MaterialTraceTemplateMaterialConverter {
    MaterialTraceTemplateMaterialConverter INSTANCE = Mappers.getMapper(MaterialTraceTemplateMaterialConverter.class);
}
