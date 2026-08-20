package com.bmos.mes.service.platform.role.service;

import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.platform.role.dto.PlatformRoleListQueryDTO;
import com.bmos.mes.service.platform.role.feign.PlatformRoleOpenFeign;
import com.bmos.mes.service.platform.role.role.PlatformRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PlatformRoleService {

    @Autowired
    private PlatformRoleOpenFeign platformRoleOpenFeign;

    public List<PlatformRoleVO> getRoles(PlatformRoleListQueryDTO dto) {
        ResponseInfo<List<PlatformRoleVO>> responseInfo;
        try {
            responseInfo = platformRoleOpenFeign.getRoles(dto);
        } catch (Exception e) {
            log.error("查询平台角色错误：{}",e.getCause() + e.getMessage());
            throw new BmosException(MesResponseCode.PLATFORM_GET_ROLE_ERROR);
        }
        if (responseInfo.isError()){
            log.error("查询平台角色错误：{}",responseInfo);
            throw new BmosException(MesResponseCode.PLATFORM_GET_ROLE_ERROR);
        }
        return responseInfo.getData();
    }
}
