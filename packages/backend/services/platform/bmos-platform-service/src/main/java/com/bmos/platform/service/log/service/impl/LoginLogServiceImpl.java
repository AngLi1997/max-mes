package com.bmos.platform.service.log.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.exporter.ExcelWriterUtils;
import com.bmos.common.exporter.bo.SheetDataBo;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.log.convert.PlatformLogConvert;
import com.bmos.platform.service.log.dto.ExportLoginLogDTO;
import com.bmos.platform.service.log.dto.QueryLoginLogDTO;
import com.bmos.platform.service.log.mapper.LoginLogMapper;
import com.bmos.platform.service.log.model.LoginLogModel;
import com.bmos.platform.service.log.service.LoginLogService;
import com.bmos.platform.service.log.vo.LoginLogExcelVO;
import com.bmos.platform.service.log.vo.LoginLogVO;
import com.bmos.platform.service.system.user.enums.LoginActionEnum;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Slf4j
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public void insert(LoginLogModel loginLogModel) {
        loginLogMapper.insert(loginLogModel);
    }

    @Override
    public CommonPage<LoginLogVO> queryLogPage(QueryLoginLogDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        dto.convert2Data();
        List<LoginLogVO> list = loginLogMapper.selectPageList(dto);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        for (LoginLogVO loginLogVO : list) {
            if (ObjectUtil.isNotNull(loginLogVO.getDescriptionCode())) {
                Object[] args = null;
                if (StrUtil.isNotEmpty(loginLogVO.getDescriptionParam())){
                    args = loginLogVO.getDescriptionParam().split(StrUtil.COMMA);
                }
                loginLogVO.setDescription(I18nUtils.getResponseMessage(loginLogVO.getDescriptionCode(), String.valueOf(loginLogVO.getDescriptionCode()), args, request));
                loginLogVO.setOperationAction(I18nUtils.getEnumMessage(CommonEnum.getEnumByValue(LoginActionEnum.class, Integer.valueOf(loginLogVO.getOperationAction()))));
            }
        }
        return CommonPage.convertPage(list);
    }

    @Override
    public void exportLog(ExportLoginLogDTO dto) {
        if (CollUtil.isNotEmpty(dto.getSelectIds())) {
            List<LoginLogModel> loginLogModels = loginLogMapper.selectBatchIds(dto.getSelectIds());
            handleExport(loginLogModels);
            return;
        }
        boolean unfiltered = ObjectUtil.isAllEmpty(dto.getLoginName(), dto.getStartTime(), dto.getStartTime(), dto.getEndTime());
        if (unfiltered) {
            List<LoginLogModel> loginLogModels = loginLogMapper.selectList();
            handleExport(loginLogModels);
            return;
        }
        List<LoginLogModel> list = loginLogMapper.selectExportData(dto);
        handleExport(list);
    }

    private final String EXPORT_NAME = "登录日志";
    private final String EXPORT_SHEET_NAME = "登录日志";

    /**
     * 处理导出
     *
     * @param loginLogModels
     */
    private void handleExport(List<LoginLogModel> loginLogModels) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        try {
            assert response != null;
            List<LoginLogExcelVO> list = PlatformLogConvert.INSTANCE.ConvertToExcelVO(loginLogModels, request);
            ExcelWriterUtils.write(EXPORT_NAME, response, Collections.singletonList(new SheetDataBo(EXPORT_SHEET_NAME, LoginLogExcelVO.class, list, null)));
        } catch (Exception e) {
            log.error("导出登录日志异常:" + e.getCause() + e.getMessage());
        }
    }
}
