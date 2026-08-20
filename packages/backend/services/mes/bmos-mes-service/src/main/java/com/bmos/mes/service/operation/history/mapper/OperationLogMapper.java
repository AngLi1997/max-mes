package com.bmos.mes.service.operation.history.mapper;

import com.bmos.mes.service.operation.history.dto.OperationLogPageQueryDTO;
import com.bmos.mes.service.operation.history.model.OperationLogModel;
import com.bmos.mes.service.operation.history.vo.OperationLogPageVO;
import com.bmos.mes.service.record.vo.VersionLogVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OperationLogMapper extends BaseMapperX<OperationLogModel> {
    List<OperationLogPageVO> selectPageList(OperationLogPageQueryDTO dto);

    List<VersionLogVO> listRecordLog(Long versionId);
}
