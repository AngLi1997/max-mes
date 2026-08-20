package com.bmos.wms.service.log.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.wms.service.log.dto.ExportOperationLogDTO;
import com.bmos.wms.service.log.dto.QueryLogPageDTO;
import com.bmos.wms.service.log.model.WmsLogModel;
import com.bmos.wms.service.log.vo.WmsLogPageVO;
import com.bmos.wms.service.log.vo.OperationLogExcelVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WmsOperationLogMapper extends BaseMapperX<WmsLogModel> {

    List<WmsLogPageVO> selectPageList(QueryLogPageDTO dto);

    List<OperationLogExcelVO> selectExportData(ExportOperationLogDTO dto);

    List<OperationLogExcelVO> selectByIds(List<Long> selectIds);
}
