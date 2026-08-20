package com.bmos.platform.service.system.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.active.RsaVO;
import com.bmos.common.exception.BmosException;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.common.utils.RsaUtils;
import com.bmos.platform.facade.active.constants.LicenseActiveConstants;
import com.bmos.platform.facade.active.dto.LicenseActiveDTO;
import com.bmos.platform.facade.active.dto.LicenseParamDTO;
import com.bmos.platform.service.execute.parameter.service.BusinessParameterService;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterDetailVO;
import com.bmos.platform.service.system.user.mapper.ActiveMapper;
import com.bmos.platform.service.system.user.model.Active;
import com.bmos.platform.service.system.user.service.ActiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class ActiveServiceImpl implements ActiveService {
    @Autowired
    private ActiveMapper activeMapper;

    @Autowired
    private BusinessParameterService businessParameterService;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(String activeStr) {
        try {
            if (!RsaUtils.validateNoDate(activeStr, applicationName)) {
                throw new BmosException(PlatformResponseCode.ACTIVE_ERROR);
            }
        } catch (Exception e) {
            throw new BmosException(PlatformResponseCode.ACTIVE_ERROR);
        }
        activeMapper.delete(new LambdaQueryWrapperX<>());
        activeMapper.insert(new Active(activeStr));
        return RsaUtils.getParseData(activeStr).getDate();
    }

    @Override
    public LicenseActiveDTO valid(LicenseParamDTO paramDTO) {
        LicenseActiveDTO res = new LicenseActiveDTO(Boolean.TRUE, LicenseActiveConstants.PERMANENT_DATE);
        BusinessParameterDetailVO businessParameterDetailVO = businessParameterService.detailByCode(BusinessParameterCodeConstants.PLATFORM_SYS_LICENSE_IS_REQUIRED);
        if (Objects.isNull(businessParameterDetailVO)){
            res.setActive(Boolean.FALSE);
            return res;
        }
        if (!Boolean.TRUE.equals(Boolean.valueOf(businessParameterDetailVO.getValue()))) {
            return res;
        }
        if (StrUtil.isEmpty(paramDTO.getActiveCode())){
            return new LicenseActiveDTO(Boolean.FALSE, StrUtil.EMPTY);
        }
        try{
            if (!RsaUtils.validateNoDate(paramDTO.getActiveCode(), paramDTO.getApplicationName())) {
                res.setActive(Boolean.FALSE);
            }
        } catch (Exception e ){
           return new LicenseActiveDTO(Boolean.FALSE, StrUtil.EMPTY);
        }
        res.setDate(RsaUtils.getParseData(paramDTO.getActiveCode()).getDate());
        return res;
    }

    @Override
    public String getActiveCode() {
        Active active = activeMapper.selectOne(new LambdaQueryWrapperX<>());
        if (Objects.isNull(active)) {
            return "";
        }
        return active.getActiveCode();
    }

    @Override
    public RsaVO actived() {
        Active active = activeMapper.selectOne(new LambdaQueryWrapperX<>());
        RsaVO rsaVO = new RsaVO();
        LicenseParamDTO licenseParamDTO = new LicenseParamDTO(Objects.isNull(active) ? null : active.getActiveCode(), applicationName);
        LicenseActiveDTO licenseActiveDTO = valid(licenseParamDTO);
        if (StrUtil.isEmpty(licenseActiveDTO.getDate())){
            // 代表开关打开 且未激活
            rsaVO.setActive(Boolean.FALSE);
        } else {
            rsaVO.setActive(licenseActiveDTO.getActive());
            rsaVO.setDate(licenseActiveDTO.getDate());
        }
        return rsaVO;
    }
}
