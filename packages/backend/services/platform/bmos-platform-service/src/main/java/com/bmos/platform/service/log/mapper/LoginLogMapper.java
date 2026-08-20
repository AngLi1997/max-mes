package com.bmos.platform.service.log.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.log.dto.ExportLoginLogDTO;
import com.bmos.platform.service.log.dto.QueryLoginLogDTO;
import com.bmos.platform.service.log.model.LoginLogModel;
import com.bmos.platform.service.log.vo.LoginLogVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginLogMapper extends BaseMapperX<LoginLogModel> {
    List<LoginLogVO> selectPageList(QueryLoginLogDTO dto);

    List<LoginLogModel> selectExportData(ExportLoginLogDTO dto);
}
