package com.bmos.mes.service.signature.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.permission.service.ResourcePermissionService;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.user.feign.PlatformUserOpenFeign;
import com.bmos.mes.service.platform.user.vo.PlatformUserVO;
import com.bmos.mes.service.signature.controller.dto.SignerQuery;
import com.bmos.mes.service.signature.controller.dto.SignerQueryDTO;
import com.bmos.mes.service.signature.controller.dto.SignerQueryWithStationIdsDTO;
import com.bmos.mes.service.signature.service.DoubleSignatureService;
import com.bmos.mes.service.station.service.IStationService;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.system.user.dto.UserQueryDTO;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/5/24 10:16
 */
@Service
public class DoubleSignatureServiceImpl implements DoubleSignatureService {

    @Resource
    private IStationService stationService;

    @Resource
    private PlatformUserOpenFeign platformUserOpenFeign;

    @Resource
    private FactoryFeign factoryFeign;

    @Resource
    private PlanService planService;

    @Resource
    private ResourcePermissionService permissionService;

    @Resource
    private UserFeign userFeign;

    @Override
    public List<PlatformUserVO> getSingerListWithPermissionCodeAndComponent(SignerQuery query) {
        // 查询工位
        List<Long> stationIds =
                stationService.getStationIdsByProcedureStepModelIdAndComponentId(query.getProcedureStepModelId(),
                        query.getComponentId(), query.getProductPlanId());
        return this.queryByStationIdsAndPermissionCode(query.getPermissionCode(), stationIds);
    }

    @Override
    public List<FeignUserVO> getUserListByPermissionCodeAndPlanId(SignerQueryDTO dto) {
        Plan plan = planService.getById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        // 工艺数据权限
        List<Long> deptList = permissionService.getDeptListByResourceId(plan.getProcessId());
        if (CollectionUtil.isEmpty(deptList)) {
            return new ArrayList<>();
        }
        UserQueryDTO userQueryDTO = new UserQueryDTO();
        userQueryDTO.setDeptIds(deptList);
        userQueryDTO.setMenuId(dto.getPermissionCode());
        return FeignUtils.handleRequest(data -> userFeign.listByMenuIdAndDeptIds(data), userQueryDTO).getData();
    }

    @Override
    public List<PlatformUserVO> getSingerListWithPermissionCodeAndStationIds(SignerQueryWithStationIdsDTO query) {
        return this.queryByStationIdsAndPermissionCode(query.getPermissionCode(), query.getStationIds());
    }

    /**
     * 根据权限吗和工位id列表查询用户
     *
     * @param permissionCode 权限码
     * @param stationIds     工位id列表
     * @return 用户列表
     */
    private @NotNull List<PlatformUserVO> queryByStationIdsAndPermissionCode(Long permissionCode, List<Long> stationIds) {
        if (CollectionUtil.isEmpty(stationIds)) {
            return new ArrayList<>();
        }
        // 根据工位查询用户
        List<String> stationUserIds = FeignUtils.handleRequest(sIds -> factoryFeign.getStationUserByStationIdList(sIds), stationIds).getData();
        stationUserIds = stationUserIds.stream().distinct().collect(Collectors.toList());
        if (permissionCode != null) {
            // 根据权限码查询用户列表
            List<PlatformUserVO> permissionCodeUsers = FeignUtils.handleRequest(pCode -> platformUserOpenFeign.listByMenuId(pCode), permissionCode).getData();
            List<String> finalStationUserIds = stationUserIds;
            return permissionCodeUsers
                    .stream()
                    .filter(item -> CollectionUtil.contains(finalStationUserIds, item.getUserId()))
                    .collect(Collectors.toList());
        } else {
            List<PlatformUserVO> list = new ArrayList<>();
            for (String stationUserId : stationUserIds) {
                PlatformUserVO platformUserVO = new PlatformUserVO();
                BaseUserDO user = UserUtils.getUser(stationUserId);
                if (user == null) {
                    continue;
                }
                platformUserVO.setUserId(user.getUserId());
                platformUserVO.setUserName(user.getUserName());
                platformUserVO.setLoginName(user.getLoginName());
                list.add(platformUserVO);
            }
            return list;
        }
    }
}
