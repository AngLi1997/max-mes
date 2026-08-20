package com.bmos.mes.service.audit.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mes.service.audit.model.FlowAuditMessage;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author renjinguang
 */
@Mapper
public interface FlowAuditMessageMapper extends BaseMapperX<FlowAuditMessage> {


    default Boolean saveMegUserList(List<FlowAuditMessage> list) {
        return Db.saveOrUpdateBatch(list);
    }

    default List<FlowAuditMessage> findMegListByDeploymentId(String deploymentId) {
        return selectList(new LambdaQueryWrapperX<FlowAuditMessage>().eq(FlowAuditMessage::getDeploymentId, deploymentId));
    }

    default Boolean deleteByIds(List<Long> ids){
        return Db.removeByIds(ids,FlowAuditMessage.class);
    }

    void deleteByNodeIdListAndDeploymentId(@Param("nodeIdList") List<String> nodeIdList,@Param("deploymentId") String deploymentId);

    default List<FlowAuditMessage> queryListByNodId(String elementKey){
        return selectList(new LambdaQueryWrapperX<FlowAuditMessage>()
                .eq(FlowAuditMessage::getNodeId,elementKey));
    }

    default List<FlowAuditMessage> listMakeUser(String nodeId, String deploymentId, String value){
        return selectList(new LambdaQueryWrapperX<FlowAuditMessage>()
                .eq(FlowAuditMessage::getNodeId,nodeId)
                .eq(FlowAuditMessage::getDeploymentId,deploymentId)
                .eq(FlowAuditMessage::getMessageType,value)
                .groupBy(FlowAuditMessage::getUserId));
    }
}
