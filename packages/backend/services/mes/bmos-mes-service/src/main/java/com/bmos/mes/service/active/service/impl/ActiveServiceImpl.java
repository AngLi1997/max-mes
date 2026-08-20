package com.bmos.mes.service.active.service.impl;

import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.active.RsaVO;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.active.feign.ActiveValidFeignClient;
import com.bmos.mes.service.active.mapper.ActiveMapper;
import com.bmos.mes.service.active.model.Active;
import com.bmos.mes.service.active.service.ActiveService;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.facade.active.dto.LicenseActiveDTO;
import com.bmos.platform.facade.active.dto.LicenseParamDTO;
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
    ActiveValidFeignClient activeValidFeignClient;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(String activeStr) {
        RsaVO rsaVO = validActiveCode(activeStr);
        if (!rsaVO.getActive()){
            throw new BmosException(MesResponseCode.ACTIVE_ERROR);
        }
        activeMapper.delete(new LambdaQueryWrapperX<>());
        activeMapper.insert(new Active(activeStr));
        return rsaVO.getDate();
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
        if (Objects.isNull(active)){
            return validActiveCode(null);
        }
        return validActiveCode(active.getActiveCode());
    }

    /**
     * 校验
     * @param code
     * @return
     */
    private RsaVO validActiveCode(String code){
        RsaVO rsaVO = new RsaVO();
        LicenseParamDTO licenseParamDTO = new LicenseParamDTO();
        licenseParamDTO.setActiveCode(code);
        licenseParamDTO.setApplicationName(applicationName);
        LicenseActiveDTO licenseActiveDTO = activeValidFeignClient.activeValid(licenseParamDTO);
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
