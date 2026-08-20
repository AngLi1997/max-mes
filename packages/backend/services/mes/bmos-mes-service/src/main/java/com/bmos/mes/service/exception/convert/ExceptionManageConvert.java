package com.bmos.mes.service.exception.convert;

import com.bmos.mes.service.exception.dto.ExceptionManualRecordDTO;
import com.bmos.mes.service.exception.dto.ExceptionOperationLogDTO;
import com.bmos.mes.service.exception.model.ExecuteException;
import com.bmos.mes.service.exception.vo.ExceptionPageVO;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExceptionManageConvert {

    ExceptionManageConvert INSTANCE = Mappers.getMapper(ExceptionManageConvert.class);

    CommonPage<ExceptionPageVO> convert2Page(CommonPage<ExecuteException> list);

    ExecuteException convert2ExceptionModel(ExceptionManualRecordDTO dto);

    @Mapping(target = "recordMode", ignore = true)
    ExceptionOperationLogDTO convert2ExceptionLogDTO(ExecuteException model);
}
