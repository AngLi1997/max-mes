package com.bmos.mes.service.log.mapper;

import com.bmos.mes.service.log.dto.ExportOperationLogDTO;
import com.bmos.mes.service.log.dto.OperationLogDetailDTO;
import com.bmos.mes.service.log.dto.QueryLogPageDTO;
import com.bmos.mes.service.log.model.MesLogModel;
import com.bmos.mes.service.log.vo.MesLogPageVO;
import com.bmos.mes.service.log.vo.OperationLogExcelVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MesOperationLogMapper extends BaseMapperX<MesLogModel> {

    List<MesLogPageVO> selectPageList(QueryLogPageDTO dto);

    List<OperationLogExcelVO> selectExportData(ExportOperationLogDTO dto);

    List<OperationLogExcelVO> selectByIds(@Param("list") List<Long> selectIds, @Param("startTime") LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);

    default MesLogModel selectDetail(OperationLogDetailDTO dto){
        return selectOne(new LambdaQueryWrapperX<MesLogModel>()
                .eq(MesLogModel::getId, dto.getId())
                .between(MesLogModel::getCreateTime, dto.getOperationTime(), dto.getOperationTime()));
    }
}
