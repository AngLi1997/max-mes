package com.bmos.lims2.server.eln.signature.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.signature.service.DoubleSignatureService;
import com.bmos.lims2.server.eln.signature.dto.SignerQueryDTO;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeVersionMapper;
import com.bmos.lims2.common.enums.InspectionOrderSourceEnum;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.lims2.server.permission.service.ResourcePermissionService;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.platform.facade.system.user.dto.UserQueryDTO;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/5/24 10:16
 */
@Service
public class DoubleSignatureServiceImpl implements DoubleSignatureService {

    @Resource
    private ResourcePermissionService permissionService;

    @Resource
    private UserFeign userFeign;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private InspectionSchemeVersionMapper inspectionSchemeVersionMapper;

    @Autowired
    private StabilitySchemeVersionMapper stabilitySchemeVersionMapper;

    @Override
    public List<FeignUserVO> getUserListByPermissionCodeAndPlanId(SignerQueryDTO dto) {
        InspectionOrder inspectionOrder = inspectionOrderMapper.selectById(dto.getInspectionOrderId());
        if (inspectionOrder == null) {
            throw new BmosException(LimsResponseCode.CHECK_ORDER_NOT_FOUND);
        }
        // 先按来源判断，再查对应的方案版本表——避免双查回退
        Long schemeId;
        if (InspectionOrderSourceEnum.STABILITY.equals(inspectionOrder.getSchemeSource())) {
            com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeVersion ssv =
                    stabilitySchemeVersionMapper.selectById(inspectionOrder.getSchemeVersionId());
            if (ssv == null) {
                return new ArrayList<>();
            }
            schemeId = ssv.getSchemeId();
        } else {
            InspectionSchemeVersion inspectionSchemeVersion =
                    inspectionSchemeVersionMapper.selectById(inspectionOrder.getSchemeVersionId());
            if (inspectionSchemeVersion == null) {
                return new ArrayList<>();
            }
            schemeId = inspectionSchemeVersion.getSchemeId();
        }
        List<Long> deptList = permissionService.getDeptListByResourceId(schemeId);
        if (CollectionUtil.isEmpty(deptList)) {
            return new ArrayList<>();
        }
        UserQueryDTO userQueryDTO = new UserQueryDTO();
        userQueryDTO.setDeptIds(deptList);
        userQueryDTO.setMenuId(dto.getPermissionCode());
        return FeignUtils.handleRequest(data -> userFeign.listByMenuIdAndDeptIds(data), userQueryDTO).getData();
    }
}
