package com.bmos.lims2.server.audit.message.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.lims2.server.audit.FlowAuditUserService;
import com.bmos.lims2.server.audit.vo.AuditMessageVO;
import com.bmos.lims2.server.permission.service.ResourcePermissionService;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Service
public abstract class AbstractAuditMessageStrategy {

    protected PlatformApiAdaptor platformApiAdaptor;

    public AbstractAuditMessageStrategy(PlatformApiAdaptor platformApiAdaptor){
        this.platformApiAdaptor = platformApiAdaptor;
    }

    public List<String> queryBusinessListByCategory(){
        // 该查询如果为管理员 则返回所有部门
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return new ArrayList<>();
        }
        return getAuditBusinessKey(deptIds);
    }

    abstract List<String> getAuditBusinessKey(List<Long> deptIdList);

    /**
     * 查询审核人员
     * @param deploymentId
     * @param nodeId
     * @return
     */
    public AuditMessageVO getReceiveMessageUserId(String deploymentId, String nodeId, Long businessId){
        FlowAuditUserService userService = SpringUtil.getBean(FlowAuditUserService.class);
        List<String> userIdList = new ArrayList<>();
        if (StrUtil.isNotBlank(deploymentId) && StrUtil.isNotBlank(nodeId)){
            userIdList.addAll(userService.selectUserIdListByNodeIdAndDeploymentId(deploymentId,nodeId));
        }
        AuditMessageVO permissionHandle = this.getPermissionHandle(businessId);
        ResourcePermissionService bean = SpringUtil.getBean(ResourcePermissionService.class);
        List<Long> deptIdList = bean.getDeptListByResourceId(permissionHandle.getBusinessId());
        if (CollUtil.isNotEmpty(deptIdList)){
            UserFeign userFeign = SpringUtil.getBean(UserFeign.class);
            ResponseInfo<List<FeignUserVO>> userList = userFeign.listUserListByDeptIds(deptIdList);
            List<String> deptUserId = CollectionUtils.convertList(userList.getData(), FeignUserVO::getUserId)
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
            permissionHandle.setUserIdList((StrUtil.isNotBlank(deploymentId) && StrUtil.isNotBlank(nodeId)) ?
                    CollectionUtils.filterList(userIdList, deptUserId::contains) : deptUserId);
        }
        return permissionHandle;
    }

    /**
     * 根据数据权限处理接收消息人
     * @param businessId 业务id
     * @return
     */
    abstract AuditMessageVO getPermissionHandle(Long businessId);


}
