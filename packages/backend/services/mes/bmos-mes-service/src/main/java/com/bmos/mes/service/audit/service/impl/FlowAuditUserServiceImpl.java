package com.bmos.mes.service.audit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.audit.FlowAuditCodeEnum;
import com.bmos.mes.service.audit.convert.FlowAuditUserConvert;
import com.bmos.mes.service.audit.dto.SaveFlowAuditUserDTO;
import com.bmos.mes.service.audit.mapper.FlowAuditUserMapper;
import com.bmos.mes.service.audit.model.FlowAuditUser;
import com.bmos.mes.service.audit.service.FlowAuditUserService;
import com.bmos.mes.service.audit.vo.FlowAuditUserVO;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Service
public class FlowAuditUserServiceImpl implements FlowAuditUserService {

    @Autowired
    private FlowAuditUserMapper auditUserMapper;

    @Autowired
    private UserFeign userFeign;

    @Override
    public Boolean saveFlowAuditUserList(List<SaveFlowAuditUserDTO> auditUserList, String deploymentId) {
        if (CollUtil.isNotEmpty(auditUserList)) {
            List<String> nodeIdList = CollectionUtils.convertList(auditUserList, SaveFlowAuditUserDTO::getNodeId);
            auditUserMapper.deleteByNodeIdListAndDeploymentId(nodeIdList, deploymentId);
            auditUserList.forEach(item -> item.setDeploymentId(deploymentId));
            List<FlowAuditUser> list = FlowAuditUserConvert.INSTANCE.convertToUserList(auditUserList);
            return auditUserMapper.saveFlowAuditUserList(list);
        }
        return Boolean.TRUE;
    }

    @Override
    public List<FlowAuditUser> findListByDeploymentId(String deploymentId) {
        return auditUserMapper.findListByDeploymentId(deploymentId);
    }

    @Override
    public Boolean deleteByIds(List<FlowAuditUser> userList) {
        if (CollUtil.isEmpty(userList)){
            return Boolean.TRUE;
        }
        List<Long> ids = CollectionUtils.convertList(userList, FlowAuditUser::getId);
        return auditUserMapper.deleteByIds(ids);
    }

    @Override
    public List<FlowAuditUserVO> queryListByDeploymentId(String deploymentId) {
        List<FlowAuditUser> list = findListByDeploymentId(deploymentId);
        if (CollUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        return FlowAuditUserConvert.INSTANCE.convertToUserListVo(list);
    }

    @Override
    public List<FlowAuditUser> findListByKeyAndDeploymentId(String key,String deploymentId) {
        return auditUserMapper.findListByKeyAndDeploymentId(key,deploymentId);
    }

    @Override
    public List<String> selectUserIdListByNodeIdAndDeploymentId(String deploymentId, String nodeId) {
        List<FlowAuditUser> auditUsers = auditUserMapper.findListByKeyAndDeploymentId(nodeId, deploymentId);
        Map<String, List<FlowAuditUser>> userMap = CollectionUtils.convertMultiMap(auditUsers, FlowAuditUser::getAssigneeType);
        List<String> userIds = new ArrayList<>();
        List<FlowAuditUser> list = userMap.get(FlowAuditCodeEnum.ALL_USER.getValue());
        if (CollUtil.isNotEmpty(list)){
            userIds.addAll(CollectionUtils.convertList(list,FlowAuditUser::getAssignee)
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList())
            );
        }
        List<FlowAuditUser> roleList = userMap.get(FlowAuditCodeEnum.ALL_ROLE.getValue());
        if (CollUtil.isNotEmpty(roleList)){
            ResponseInfo<List<FeignUserVO>> listResponseInfo = userFeign.listUserListByRoleIds(CollectionUtils.convertList(roleList, FlowAuditUser::getAssignee));
            userIds.addAll(CollectionUtils.convertList(listResponseInfo.getData(),FeignUserVO::getUserId));
        }
        return userIds.stream().distinct().collect(Collectors.toList());
    }
}
