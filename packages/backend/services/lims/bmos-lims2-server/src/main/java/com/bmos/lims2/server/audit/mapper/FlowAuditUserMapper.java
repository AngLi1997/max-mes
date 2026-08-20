package com.bmos.lims2.server.audit.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.lims2.server.audit.entity.FlowAuditUser;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author renjinguang
 */
@Mapper
public interface FlowAuditUserMapper extends BaseMapperX<FlowAuditUser> {


    default Boolean saveFlowAuditUserList(List<FlowAuditUser> list) {
        return Db.saveOrUpdateBatch(list);
    }

    default List<FlowAuditUser> findListByDeploymentId(String deploymentId) {
        return selectList(new LambdaQueryWrapperX<FlowAuditUser>().eq(FlowAuditUser::getDeploymentId, deploymentId));
    }

    default Boolean deleteByIds(List<Long> ids) {
        return Db.removeByIds(ids, FlowAuditUser.class);
    }

    default List<FlowAuditUser> findListByKeyAndDeploymentId(String key,String deploymentId) {
        return selectList(new LambdaQueryWrapperX<FlowAuditUser>()
                .eq(FlowAuditUser::getNodeId, key)
                .eq(FlowAuditUser::getDeploymentId,deploymentId));
    }

    void deleteByNodeIdListAndDeploymentId(@Param("nodeIdList") List<String> nodeIdList,@Param("deploymentId") String deploymentId);
}
