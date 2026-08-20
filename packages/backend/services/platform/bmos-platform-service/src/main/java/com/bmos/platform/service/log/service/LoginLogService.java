package com.bmos.platform.service.log.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.log.dto.ExportLoginLogDTO;
import com.bmos.platform.service.log.dto.QueryLoginLogDTO;
import com.bmos.platform.service.log.model.LoginLogModel;
import com.bmos.platform.service.log.vo.LoginLogVO;

public interface LoginLogService{

    void insert(LoginLogModel loginLogModel);

    CommonPage<LoginLogVO> queryLogPage(QueryLoginLogDTO dto);

    void exportLog(ExportLoginLogDTO dto);
}
