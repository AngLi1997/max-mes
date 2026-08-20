package com.bmos.mes.service.process.mapper;

import com.bmos.audit.engine.core.db.repository.base.BaseMapperX;
import com.bmos.mes.service.process.model.ProductScheduleProcedureConfig;
import com.bmos.mes.service.query.dto.ProductScheduleProcedureConfigDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 生产进度配置(ProductScheduleProcedureConfig)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-04 11:37:52
 */
@Mapper
public interface ProductScheduleProcedureConfigMapper extends BaseMapperX<ProductScheduleProcedureConfig> {

    List<ProductScheduleProcedureConfigDTO> selectListWithProcesName();
}

