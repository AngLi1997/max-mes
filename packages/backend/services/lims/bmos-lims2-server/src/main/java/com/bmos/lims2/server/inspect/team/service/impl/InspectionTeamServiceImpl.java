package com.bmos.lims2.server.inspect.team.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.lims2.common.enums.PermissionModuleEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeItemTeams;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeItemTeamMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeMapper;
import com.bmos.lims2.server.inspect.team.convert.InspectionTeamConvert;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamAssignUserDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamPageReqDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamSaveDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamUpdateDTO;
import com.bmos.lims2.server.inspect.team.entity.InspectionTeam;
import com.bmos.lims2.server.inspect.team.entity.InspectionTeamUser;
import com.bmos.lims2.server.inspect.team.mapper.InspectionTeamMapper;
import com.bmos.lims2.server.inspect.team.mapper.InspectionTeamUserMapper;
import com.bmos.lims2.server.inspect.team.service.InspectionTeamService;
import com.bmos.lims2.server.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.lims2.server.permission.service.ResourcePermissionService;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.lims2.server.task.dto.AssignableUserDTO;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InspectionTeamServiceImpl implements InspectionTeamService {

    @Resource
    private InspectionTeamMapper inspectionTeamMapper;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private ResourcePermissionService resourcePermissionService;

    @Resource
    private InspectionTeamUserMapper inspectionTeamUserMapper;

	@Resource
	private InspectionSchemeItemTeamMapper inspectionSchemeItemTeamMapper;

	@Resource
	private InspectionSchemeMapper inspectionSchemeMapper;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private StabilitySchemeVersionMapper stabilitySchemeVersionMapper;

    @Override
    public List<com.bmos.lims2.server.inspect.team.dto.InspectionTeamUserDTO> listUsersBySchemeVersionAndInspectItem(
            Long schemeVersionId, Long inspectItemId) {
        if (schemeVersionId == null || inspectItemId == null) {
            return new ArrayList<>();
        }
        boolean isStability = stabilitySchemeVersionMapper.selectById(schemeVersionId) != null;
        List<Long> teamIds = isStability
                ? taskMapper.selectTeamIdsByInspectItemAndStabilitySchemeVersion(inspectItemId, schemeVersionId)
                : taskMapper.selectTeamIdsByInspectItemAndSchemeVersion(inspectItemId, schemeVersionId);
        if (CollUtil.isEmpty(teamIds)) {
            return new ArrayList<>();
        }
        List<AssignableUserDTO> users = taskMapper.selectUsersByTeamIds(teamIds);
        if (CollUtil.isEmpty(users)) {
            return new ArrayList<>();
        }
        Map<String, AssignableUserDTO> deduped = users.stream().collect(Collectors.toMap(
                AssignableUserDTO::getUserId,
                u -> u,
                (e, r) -> e,
                LinkedHashMap::new
        ));
        return deduped.values().stream().map(u -> {
            com.bmos.lims2.server.inspect.team.dto.InspectionTeamUserDTO dto = new com.bmos.lims2.server.inspect.team.dto.InspectionTeamUserDTO();
            dto.setUserId(u.getUserId());
            dto.setTeamId(u.getTeamId());
            dto.setTeamName(u.getTeamName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInspectionTeam(InspectionTeamSaveDTO dto) {
        InspectionTeam team = inspectionTeamMapper.selectByCode(dto.getCode());
        if (Objects.nonNull(team)) {
            throw new BmosException(LimsResponseCode.TEAM_CODE_EXISTS);
        }
        InspectionTeam insert = InspectionTeamConvert.INSTANCE.convert2TeamDO(dto);
        inspectionTeamMapper.insert(insert);
        resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                .resourceId(insert.getId())
                .deptIds(dto.getDeptIdList())
                .module(PermissionModuleEnum.TEAM.getValue())
                .build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInspectionTeam(InspectionTeamUpdateDTO dto) {
        // 检查班组是否存在
        InspectionTeam existingTeam = getAndValidateTeam(dto.getId());

        // 检查编码是否被其他班组使用
        InspectionTeam teamByCode = inspectionTeamMapper.selectByCode(dto.getCode());
        if (Objects.nonNull(teamByCode) && !Objects.equals(teamByCode.getId(), dto.getId())) {
            throw new BmosException(LimsResponseCode.TEAM_CODE_EXISTS);
        }

        // 更新班组信息
        InspectionTeam updateTeam = InspectionTeamConvert.INSTANCE.convert2TeamDO(dto);
        updateTeam.setStatus(existingTeam.getStatus());
        updateTeam.setNumber(existingTeam.getNumber());
        inspectionTeamMapper.updateById(updateTeam);
    }

    @Override
    public CommonPage<InspectionTeamDTO> getInspectionTeamPage(InspectionTeamPageReqDTO dto) {
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return CommonPage.convertPage(PageInfo.emptyPageInfo());
        }
        dto.setDeptIds(deptIds);
        return CommonPage.convertPage(inspectionTeamMapper.queryPage(dto));
    }

    @Override
    public void enableInspectionTeam(Long id) {
        InspectionTeam inspectionTeam = getAndValidateTeam(id);
        if (inspectionTeam.getStatus()) {
            throw new BmosException(LimsResponseCode.INSPECTION_TEAM_STATUS_UPDATED);
        }
        inspectionTeam.setStatus(Boolean.TRUE);
        inspectionTeamMapper.updateById(inspectionTeam);
    }

    /**
     * 获取并校验班组是否存在
     * @param id
     * @return
     */
    private InspectionTeam getAndValidateTeam(Long id) {
        InspectionTeam inspectionTeam = inspectionTeamMapper.selectById(id);
        if (Objects.isNull(inspectionTeam)) {
            throw new BmosException(LimsResponseCode.INSPECTION_TEAM_NOT_EXISTS);
        }
        return inspectionTeam;
    }

    @Override
    public void disableInspectionTeam(Long id) {
        InspectionTeam inspectionTeam = getAndValidateTeam(id);
        if (!inspectionTeam.getStatus()) {
            throw new BmosException(LimsResponseCode.INSPECTION_TEAM_STATUS_UPDATED);
        }
		// 校验班组是否被检验方案绑定，若绑定则不允许停用
		LambdaQueryWrapper<InspectionSchemeItemTeams> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(InspectionSchemeItemTeams::getTeamId, id)
				.eq(InspectionSchemeItemTeams::getDeleted, false);
		List<InspectionSchemeItemTeams> bindings = inspectionSchemeItemTeamMapper.selectList(wrapper);
		if (CollUtil.isNotEmpty(bindings)) {
			List<Long> schemeIds = bindings.stream()
					.map(InspectionSchemeItemTeams::getSchemeId)
					.filter(Objects::nonNull)
					.distinct()
					.collect(Collectors.toList());
			if (CollUtil.isNotEmpty(schemeIds)) {
				List<InspectionScheme> schemes = inspectionSchemeMapper.selectBatchIds(schemeIds);
				String names = schemes.stream()
						.map(InspectionScheme::getName)
						.filter(Objects::nonNull)
						.distinct()
						.collect(Collectors.joining(","));
				if (StrUtil.isNotBlank(names)) {
					throw new BmosException(LimsResponseCode.TEAM_BINDING_SCHEME_NOT_ALLOW_DISABLE, names);
				}
			}
		}
        inspectionTeam.setStatus(Boolean.FALSE);
        inspectionTeamMapper.updateById(inspectionTeam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inspectionTeamAssignUser(InspectionTeamAssignUserDTO dto) {
        InspectionTeam inspectionTeam = getAndValidateTeam(dto.getId());
        // 删除原绑定关系
        inspectionTeamUserMapper.deleteByInspectionTeamId(inspectionTeam.getId());
        // 班组人数
        inspectionTeam.setNumber(CollUtil.size(dto.getUserIdList()));
        inspectionTeamMapper.updateById(inspectionTeam);
        // 新绑定关系
        if (CollUtil.isNotEmpty(dto.getUserIdList())) {
            inspectionTeamUserMapper.insertBatch(dto.getUserIdList().stream().map(userId -> {
                InspectionTeamUser inspectionTeamUser = new InspectionTeamUser();
                inspectionTeamUser.setUserId(userId);
                inspectionTeamUser.setInspectionTeamId(inspectionTeam.getId());
                return inspectionTeamUser;
            }).collect(Collectors.toList()));
        }
    }

    @Override
    public List<String> getInspectionTeamUserIdList(Long id) {
        List<InspectionTeamUser> list = inspectionTeamUserMapper.selectByInspectionTeamId(id);
        return CollectionUtils.convertList(list, InspectionTeamUser::getUserId);
    }


    @Override
    public List<InspectionTeamDTO> getTeamListByPermission() {
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return new ArrayList<>();
        }
        InspectionTeamPageReqDTO dto=new InspectionTeamPageReqDTO();
        dto.setDeptIds(deptIds);
        return inspectionTeamMapper.queryList(dto);
    }
}
