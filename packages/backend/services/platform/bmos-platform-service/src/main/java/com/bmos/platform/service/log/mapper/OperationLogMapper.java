package com.bmos.platform.service.log.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.log.dto.ExportOperationLogDTO;
import com.bmos.platform.service.log.dto.OperationLogDetailDTO;
import com.bmos.platform.service.log.dto.QueryOperationLogPageDTO;
import com.bmos.platform.service.log.model.OperationLogModel;
import com.bmos.platform.service.log.vo.OperationLogDetailVO;
import com.bmos.platform.service.log.vo.OperationLogExcelVO;
import com.bmos.platform.service.log.vo.OperationLogPageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OperationLogMapper extends BaseMapperX<OperationLogModel> {
    List<OperationLogPageVO> selectPageList(QueryOperationLogPageDTO dto);

    List<OperationLogExcelVO> selectExportData(ExportOperationLogDTO dto);

    List<OperationLogExcelVO> selectByIds(List<Long> selectIds);

    default OperationLogModel getOperationLogDetailInfo(OperationLogDetailDTO dto){
        return selectOne(new LambdaQueryWrapperX<OperationLogModel>()
                .eq(OperationLogModel::getId, dto.getId())
                .between(OperationLogModel::getCreateTime,  dto.getOperationTime(), dto.getOperationTime()));
    }
}
