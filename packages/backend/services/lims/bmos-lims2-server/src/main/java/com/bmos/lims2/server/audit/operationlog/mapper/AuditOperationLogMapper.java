package com.bmos.lims2.server.audit.operationlog.mapper;

import com.bmos.lims2.server.audit.operationlog.dto.OperationLogPageQueryDTO;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.vo.ListLogVO;
import com.bmos.lims2.server.audit.operationlog.vo.OperationLogPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuditOperationLogMapper extends BaseMapperX<AuditOperationLogEntity> {
    List<OperationLogPageVO> selectPageList(OperationLogPageQueryDTO dto);

    List<ListLogVO> listRecordLog(Long versionId);

    /**
     * 查询指定业务ID和模块的操作日志
     * @param businessId 业务ID
     * @param module 业务模块
     * @return 操作日志列表
     */
    List<ListLogVO> listLogByBusinessIdAndModule(@Param("businessId") Long businessId, @Param("module") String module);
}
