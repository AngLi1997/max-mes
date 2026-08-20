package com.bmos.mes.service.signature.service;

import com.bmos.mes.service.platform.user.vo.PlatformUserVO;
import com.bmos.mes.service.signature.controller.dto.SignerQuery;
import com.bmos.mes.service.signature.controller.dto.SignerQueryDTO;
import com.bmos.mes.service.signature.controller.dto.SignerQueryWithStationIdsDTO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/5/24 10:16
 */
public interface DoubleSignatureService {


    List<PlatformUserVO> getSingerListWithPermissionCodeAndComponent(SignerQuery query);

    List<FeignUserVO> getUserListByPermissionCodeAndPlanId(SignerQueryDTO dto);

    List<PlatformUserVO> getSingerListWithPermissionCodeAndStationIds(SignerQueryWithStationIdsDTO query);
}
