package com.bmos.lims2.server.eln.signature.service;

import com.bmos.lims2.server.eln.signature.dto.SignerQueryDTO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/5/24 10:16
 */
public interface DoubleSignatureService {

    List<FeignUserVO> getUserListByPermissionCodeAndPlanId(SignerQueryDTO dto);
}
